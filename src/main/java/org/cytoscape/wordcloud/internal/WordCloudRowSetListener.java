package org.cytoscape.wordcloud.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
import org.cytoscape.wordcloud.internal.ui.WordCloudDialog;
import org.cytoscape.wordcloud.internal.util.WordCloudUtility;
import org.cytoscape.wordcloud.internal.util.WordTokenizer;

public class WordCloudRowSetListener implements RowsSetListener {

	private CyApplicationManager cyApplicationManager;
	
	private WordCloudDialog wordCloudDialog;
	private WordCloudSettingsHolder wordCloudSettingsHolder;
	
	private RowSetListenerToggle rowSetListenerToggle;
	
	public WordCloudRowSetListener(CyApplicationManager cyApplicationManager,
			WordCloudDialog wordCloudDialog,
			WordCloudSettingsHolder wordCloudSettings,
			RowSetListenerToggle rowSetListenerToggle) {
		this.cyApplicationManager = cyApplicationManager;
		
		this.wordCloudDialog = wordCloudDialog;
		this.wordCloudSettingsHolder = wordCloudSettings;
		
		this.rowSetListenerToggle = rowSetListenerToggle;
	}
	
	@Override
	public void handleEvent(RowsSetEvent e) {
		
		// If the toggle says we are not listening to events, then don't
		if (!this.rowSetListenerToggle.isListening()) {
			return;
		}
		
		// Only take events that contain the "selected" column, and come from the default node table of the current network
		if (e.containsColumn("selected") == false
				|| e.getSource() != this.cyApplicationManager.getCurrentNetwork().getDefaultNodeTable()) {
			return;
		}
		
		// Get selected nodes
		// TODO: cyApplicationManager's current network may not be the triggering network
		List<CyNode> selectedNodes = WordCloudUtility.fetchSelectedNodes(this.cyApplicationManager.getCurrentNetwork());
		//System.out.println("Number of selected nodes on current network: " + selectedNodes.size());
		
		// Get the table for the current network
		CyTable cyTable = e.getSource();
		
		// Notes:
		// 1. Use CyNetworkTableManager to get network from table
		// 2. Use cyTable.getRow(cyNode) to get row
		
		if (selectedNodes != null && selectedNodes.size() > 0) {
		
			CyNode selectedNode = selectedNodes.iterator().next();
			
			Map<String, Collection<CyNode>> nodesPerWordMap = new HashMap<String, Collection<CyNode>>();
			Map<String, Integer> wordCounts = WordCloudUtility.getWordCounts(selectedNodes, 
					this.cyApplicationManager.getCurrentNetwork(),
					nodesPerWordMap,
					this.wordCloudSettingsHolder);
			
			if (this.wordCloudDialog.isVisible()) {
				this.wordCloudDialog.populateWordCloud(wordCounts, wordCloudSettingsHolder, nodesPerWordMap);
			}
		} else {
			if (this.wordCloudDialog.isVisible()) {
				this.wordCloudDialog.clearWordCloud();
			}
		}
	}
	
	
	
}
