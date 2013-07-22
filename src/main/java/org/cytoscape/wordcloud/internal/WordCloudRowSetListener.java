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
		
		if (this.wordCloudDialog.isVisible()) {
			WordCloudUtility.updateWordCloud(this.cyApplicationManager, this.wordCloudSettingsHolder, this.wordCloudDialog);
		}
	}
	
	
	
}
