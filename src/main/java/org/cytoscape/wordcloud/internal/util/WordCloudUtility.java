package org.cytoscape.wordcloud.internal.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.wordcloud.internal.WordCloudSettingsHolder;
import org.cytoscape.work.TaskMonitor;

/**
 * Set of utility functions used for WordCloud
 */
public class WordCloudUtility {
	public static List<CyNode> fetchSelectedNodes(CyNetwork network) {
		return CyTableUtil.getNodesInState(network, "selected", true);
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
					wordCloudSettingsHolder.getExcludedColumnsMap());
			
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
	

	/**
	 * Get the words for a given node
	 * 
	 * @param node The node whose attribute words to get
	 * @param network The network that the node belongs to, used to retrieve attributes
	 * @return
	 */
	public static List<String> getWords(CyNode node, CyNetwork network, 
			WordTokenizer wordTokenizer, 
			Map<CyNetwork, Collection<String>> excludedColumnsMap) {
		
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
	
					words.addAll(splitWords);
					
				} else if (value instanceof List) {
					
					List<?> list = (List<?>) value;
					
					for (Object listObject : list) {
						if (listObject instanceof String) {
							// Split the string based on punctuation and spaces, then add the individual words
							Collection<String> splitWords = wordTokenizer.tokenize((String) listObject);
	
							words.addAll(splitWords);
						}
					}
				}
			}
		}
		
		return words;
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
}
