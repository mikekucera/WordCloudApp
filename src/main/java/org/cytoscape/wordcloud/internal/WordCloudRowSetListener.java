package org.cytoscape.wordcloud.internal;

import java.util.List;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
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
		CyTable cyTable = cyApplicationManager.getCurrentTable(); // e.getSource();
		
		CyNode selectedNode = selectedNodes.iterator().next();
		System.out.println("Selected Node: " + selectedNode);
		
		System.out.println("cyTable.getRow(): " + cyTable);
		
		System.out.println("cyTable.getRow(): " + cyTable.getRow(selectedNode));
		System.out.println("Row for a given selected node: " + cyTable.getRow(selectedNode).getAllValues());
	}
	
	private List<CyNode> fetchSelectedNodes(CyNetwork network) {
		return CyTableUtil.getNodesInState(network, "selected", true);
	}

}
