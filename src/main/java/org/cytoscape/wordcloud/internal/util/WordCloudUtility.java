package org.cytoscape.wordcloud.internal.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.wordcloud.internal.WordCloudSettingsHolder;
import org.cytoscape.wordcloud.internal.stemmer.Stemmer;
import org.cytoscape.wordcloud.internal.ui.WordCloudDialog;
import org.cytoscape.work.TaskMonitor;

/**
 * Set of utility functions used for WordCloud
 */
public class WordCloudUtility {
	
	/**
	 * A map that maps the stemmed version of a word to the first seen unstemmed version of that word.
	 * This is useful for displaying words that have been stemmed, as displaying the stemmed versions
	 * will cause non-words to be displayed.
	 */
	private static Map<String, String> reverseStemmingMap = new HashMap<String, String>();
	
	public static List<CyNode> fetchSelectedNodes(CyNetwork network) {
		return CyTableUtil.getNodesInState(network, "selected", true);
	}
	
	public static List<CyNode> fetchUnselectedNodes(CyNetwork network) {
		return CyTableUtil.getNodesInState(network, "selected", false);
	}
	
	// Make sure the right CyTable is given, if multiple tables contain the nodes
	public static Map<String, Integer> getWordCounts(
			Collection<CyNode> nodes, 
			CyNetwork network, 
			Map<String, Collection<CyNode>> nodesPerWordMap,
			WordCloudSettingsHolder wordCloudSettingsHolder) {
		
		Map<String, Integer> wordCounts = new HashMap<String, Integer>();
		nodesPerWordMap.clear();

		for (CyNode node : nodes) {
			
			List<String> nodeWords = WordCloudUtility.getWords(node, network,
					wordCloudSettingsHolder.getWordTokenizer(),
					wordCloudSettingsHolder.getExcludedColumnsMap(),
					wordCloudSettingsHolder.isUsingStemming());
			
			for (String word : nodeWords) {
				// Add to the 'nodes per word' map
				{
					Collection<CyNode> nodesForWord = nodesPerWordMap.get(word);
					
					if (nodesForWord == null) {
						nodesForWord = new HashSet<CyNode>();
						
						nodesPerWordMap.put(word, nodesForWord);
					}
					
					nodesForWord.add(node);
				}
				
				if (wordCounts.get(word) == null) {
					wordCounts.put(word, 1);
				} else {
					wordCounts.put(word, wordCounts.get(word) + 1);
				}
			}
		}
		
		
		return wordCounts;
	}
	
	public static void buildReverseStemmingMap(CyNetwork network,
			WordTokenizer wordTokenizer) {
		
		reverseStemmingMap.clear();
		
		for (CyNode node : network.getNodeList()) {
			List<String> nodeWords = WordCloudUtility.getWords(node, network, wordTokenizer, 
					new HashMap<CyNetwork, Collection<String>>(), false);
			
			for (String nodeWord : nodeWords) {
				Stemmer stemmer = new Stemmer();
				
				stemmer.add(nodeWord.toCharArray(), nodeWord.length());
				stemmer.stem();
				String stemmed = stemmer.toString();
				
				if (reverseStemmingMap.get(stemmed) == null) {
					reverseStemmingMap.put(stemmed, nodeWord);
				} else {
					if (nodeWord.length() < reverseStemmingMap.get(stemmed).length()) {
						reverseStemmingMap.put(stemmed, nodeWord);
					}
				}
			}
		}
		
	}
	
	/**
	 * Get the words for a given node
	 * 
	 * @param node The node whose attribute words to get
	 * @param network The network that the node belongs to, used to retrieve attributes
	 * @return
	 */
	public static List<String> getWords(CyNode node, CyNetwork network, 
			WordTokenizer wordTokenizer, 
			Map<CyNetwork, Collection<String>> excludedColumnsMap,
			boolean isUsingStemming) {
		
		List<String> words = new LinkedList<String>();
		
		CyRow correspondingRow = network.getRow(node);
		
		Map<String, Object> rowValues = correspondingRow.getAllValues();
		//System.out.println("getWords() call, getAllValues: " + correspondingRow.getAllValues());
		
		Collection<String> excludedColumns = excludedColumnsMap.get(network);

		for (Entry<String, Object> entry : rowValues.entrySet()) {
			
			String columnName = entry.getKey();
			Object value = entry.getValue();
			
			boolean columnIsExcluded = false;
			
			// Skip this column if it was excluded
			if (excludedColumns != null) {
				for (String excludedColumn : excludedColumns) {
					
					if (excludedColumn.equals(columnName)) {
						
						columnIsExcluded = true;
					}
				}
			}
			
			if (!columnIsExcluded) {
				if (value instanceof String) {
					// Split the string based on punctuation and spaces, then add the individual words
					Collection<String> splitWords = wordTokenizer.tokenize((String) value);

					WordCloudUtility.addSplitToTotal(splitWords, words, isUsingStemming);
					
				} else if (value instanceof List) {
					
					List<?> list = (List<?>) value;
					
					for (Object listObject : list) {
						if (listObject instanceof String) {
							// Split the string based on punctuation and spaces, then add the individual words
							Collection<String> splitWords = wordTokenizer.tokenize((String) listObject);
	
							WordCloudUtility.addSplitToTotal(splitWords, words, isUsingStemming);
						}
					}
				}
			}
		}
		
		return words;
	}
	
	private static void addSplitToTotal(Collection<String> splitWords, Collection<String> totalWords, boolean isUsingStemming) {
		if (!isUsingStemming) {
			totalWords.addAll(splitWords);
		} else {
			for (String splitWord : splitWords) {
				Stemmer stemmer = new Stemmer();
				
				stemmer.add(splitWord.toCharArray(), splitWord.length());
				stemmer.stem();
				String stemmed = stemmer.toString();
				
				/* Implementation for not using prebuilt reverse stemming map
				if (WordCloudUtility.reverseStemmingMap.get(stemmed) == null) {
					WordCloudUtility.reverseStemmingMap.put(stemmed, splitWord);
				}
				
			//	System.out.println("Stemmed word " + splitWord + " to: " + stemmed);
				
				totalWords.add(WordCloudUtility.reverseStemmingMap.get(stemmed));
				*/
				
				if (WordCloudUtility.reverseStemmingMap.get(stemmed) != null) {
					totalWords.add(WordCloudUtility.reverseStemmingMap.get(stemmed));
				} else {
					// This case should not happen if the reverse stemming map was prebuilt
					totalWords.add(splitWord);
				}
			}
		}
	}
	
	/**
	 * Set the selection to the given nodes
	 * @param cyNodes The nodes to be selected
	 * @param network The network containing the nodes
	 * @param cyEventHelper The event helper used to flush events after modifying node selection state
	 */
	public static void setNodeSelection(Collection<CyNode> cyNodes, CyNetwork network, 
			CyEventHelper cyEventHelper, CyNetworkViewManager cyNetworkViewManager) {

		HashSet<Long> toBeSelectedNodeIds = new HashSet<Long>();
		
		for (CyNode node : cyNodes) {
			toBeSelectedNodeIds.add(node.getSUID());
		}
		
		for (CyNode networkNode : network.getNodeList()) {
			
			if (toBeSelectedNodeIds.contains(networkNode.getSUID())) {
				if (network.getRow(networkNode).get(CyNetwork.SELECTED, Boolean.class) == false) {
					network.getRow(networkNode).set(CyNetwork.SELECTED, true);
				}
			} else {
				if (network.getRow(networkNode).get(CyNetwork.SELECTED, Boolean.class) == true) {
					network.getRow(networkNode).set(CyNetwork.SELECTED, false);
				}
			}
		}
		
		// TODO Need to call CyEventHelper.flushPayloadEvents()?
		// cyEventHelper.flushPayloadEvents();
		
		// Redraw the view for selection
		for (CyNetworkView networkView : cyNetworkViewManager.getNetworkViews(network)) {
			networkView.updateView();
		}
	}
	
	public static void updateWordCloud(
			CyApplicationManager cyApplicationManager,
			WordCloudSettingsHolder wordCloudSettingsHolder,
			WordCloudDialog wordCloudDialog) {
		
		List<CyNode> selectedNodes = WordCloudUtility.fetchSelectedNodes(cyApplicationManager.getCurrentNetwork());
		
		// Notes:
		// 1. Use CyNetworkTableManager to get network from table
		// 2. Use cyTable.getRow(cyNode) to get row
		
		if (selectedNodes != null && selectedNodes.size() > 0) {
			
			Map<String, Collection<CyNode>> nodesPerWordMap = new HashMap<String, Collection<CyNode>>();
			
			// Prebuild the network reverse word stemming map if we're using stemming
			if (wordCloudSettingsHolder.isUsingStemming()) {
				WordCloudUtility.buildReverseStemmingMap(cyApplicationManager.getCurrentNetwork(), wordCloudSettingsHolder.getWordTokenizer());
			}
			
			Map<String, Integer> wordCounts = WordCloudUtility.getWordCounts(selectedNodes, 
					cyApplicationManager.getCurrentNetwork(),
					nodesPerWordMap,
					wordCloudSettingsHolder);
			
			// Check if we need to get word counts for the network
			if (wordCloudSettingsHolder.isUsingNormalization()) {
				Map<String, Integer> networkWordCounts = new HashMap<String, Integer>();
				
				List<CyNode> unselectedNodes = WordCloudUtility.fetchUnselectedNodes(cyApplicationManager.getCurrentNetwork());
				Map<String, Collection<CyNode>> networkNodesPerWordMap = new HashMap<String, Collection<CyNode>>();
				
				networkWordCounts = WordCloudUtility.getWordCounts(
						unselectedNodes, cyApplicationManager.getCurrentNetwork(), nodesPerWordMap, wordCloudSettingsHolder);
				
//				System.out.println("Normalized call");
				wordCloudDialog.populateWordCloudNormalized(
						wordCounts, 
						networkWordCounts, 
						selectedNodes.size(), 
						cyApplicationManager.getCurrentNetwork().getNodeCount(), 
						wordCloudSettingsHolder, 
						nodesPerWordMap);
			} else {
				wordCloudDialog.populateWordCloud(wordCounts, wordCloudSettingsHolder, nodesPerWordMap);
			}
		} else {
			wordCloudDialog.clearWordCloud();
		}
	}
}
