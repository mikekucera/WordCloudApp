package org.cytoscape.wordcloud.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkTableManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.model.events.RowsSetEvent;
import org.cytoscape.model.events.RowsSetListener;

public class WordCloudRowSetListener implements RowsSetListener {

	private CyApplicationManager cyApplicationManager;
	
	public WordCloudRowSetListener(CyApplicationManager cyApplicationManager) {
		this.cyApplicationManager = cyApplicationManager;
	}
	
	@Override
	public void handleEvent(RowsSetEvent e) {
		
		// Get selected nodes
		// TODO: cyApplicationManager's current network may not be the triggering network
		List<CyNode> selectedNodes = fetchSelectedNodes(this.cyApplicationManager.getCurrentNetwork());
		System.out.println("Number of selected nodes on current network: " + selectedNodes.size());
		
		// Get the table for the current network
		CyTable cyTable = e.getSource();
		
		// Notes:
		// 1. Use CyNetworkTableManager to get network from table
		// 2. Use cyTable.getRow(cyNode) to get row
		
		if (selectedNodes != null && selectedNodes.size() > 0) {
		
			CyNode selectedNode = selectedNodes.iterator().next();
			System.out.println("Selected Node: " + selectedNode);
			
			System.out.println("cyTable: " + cyTable);
			
			System.out.println("cyTable.getRow(): " + cyTable.getRow(selectedNode.getSUID()));
			System.out.println("Row for a given selected node: " + cyTable.getRow(selectedNode.getSUID()).getAllValues());
			
			System.out.println("Test1");
			
			System.out.println("getWordCounts(): " + this.getWordCounts(selectedNodes, this.cyApplicationManager.getCurrentNetwork()));
		}
	}
	
	private List<CyNode> fetchSelectedNodes(CyNetwork network) {
		return CyTableUtil.getNodesInState(network, "selected", true);
	}
	
	// Make sure the right CyTable is given, if multiple tables contain the nodes
	private Map<String, Integer> getWordCounts(Collection<CyNode> nodes, CyNetwork network) {
		
		Map<String, Integer> wordCounts = new HashMap<String, Integer>();
		
		for (CyNode node : nodes) {
			List<String> nodeWords = this.getWords(node, network);
			
			for (String word : nodeWords) {
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
	private List<String> getWords(CyNode node, CyNetwork network) {
		List<String> words = new LinkedList<String>();
		
		CyRow correspondingRow = network.getRow(node);
		
		Map<String, Object> rowValues = correspondingRow.getAllValues();
		//System.out.println("getWords() call, getAllValues: " + correspondingRow.getAllValues());
		
		for (Object value : rowValues.values()) {
			if (value instanceof String) {
				words.add((String) value);
			} else if (value instanceof List) {
				
				List<?> list = (List<?>) value;
				
				for (Object listObject : list) {
					if (listObject instanceof String) {
						words.add((String) listObject);
					}
				}
			}
		}
		
		return words;
	}

}
