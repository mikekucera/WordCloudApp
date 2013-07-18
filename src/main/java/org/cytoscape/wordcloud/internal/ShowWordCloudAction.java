package org.cytoscape.wordcloud.internal;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNode;
import org.cytoscape.wordcloud.internal.ui.WordCloudDialog;
import org.cytoscape.wordcloud.internal.util.WordCloudUtility;
import org.w3c.dom.views.AbstractView;

public class ShowWordCloudAction extends AbstractCyAction {

	private WordCloudDialog wordCloudDialog;
	
	private CySwingApplication cySwingApplication;
	private CyApplicationManager cyApplicationManager;
	
	private WordCloudSettingsHolder wordCloudSettingsHolder;
	
	public ShowWordCloudAction(CySwingApplication cySwingApplication,
			WordCloudDialog wordCloudDialog,
			CyApplicationManager cyApplicationManager,
			WordCloudSettingsHolder wordCloudSettingsHolder) {
		super("Show Word Cloud");
		setPreferredMenu("Apps.WordCloud");
		
		this.cySwingApplication = cySwingApplication;
		this.cyApplicationManager = cyApplicationManager;
		
		this.wordCloudSettingsHolder = wordCloudSettingsHolder;
		
		if (wordCloudDialog == null) {
			throw new RuntimeException("wordCloudDialog must not be null");
		}
		
		this.wordCloudDialog = wordCloudDialog;
		
		this.wordCloudSettingsHolder.addSettingsChangedListener(new SettingsChangeListener() {
			
			@Override
			public void settingsChanged(WordCloudSettingsHolder wordCloudSettingsHolder) {
				updateWordCloud();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// System.out.println("Word Cloud clicked");
		
		if (this.wordCloudDialog.isFirstTimeShown()) {
		
			this.wordCloudDialog.setLocationRelativeTo(this.cySwingApplication.getJFrame());
	
			// Offset the dialog slightly
			Point currentLocation = this.wordCloudDialog.getLocation();
			this.wordCloudDialog.setLocation((int) currentLocation.getX() + 450, (int) currentLocation.getY() - 230);
				
			this.wordCloudDialog.setFirstTimeShown(false);
		}
		
		// Update the word cloud
		updateWordCloud();
		
		this.wordCloudDialog.setVisible(true);
	}
	
	private void updateWordCloud() {
		List<CyNode> selectedNodes = WordCloudUtility.fetchSelectedNodes(this.cyApplicationManager.getCurrentNetwork());
		
		// Notes:
		// 1. Use CyNetworkTableManager to get network from table
		// 2. Use cyTable.getRow(cyNode) to get row
		
		if (selectedNodes != null && selectedNodes.size() > 0) {
			
			Map<String, Collection<CyNode>> nodesPerWordMap = new HashMap<String, Collection<CyNode>>();
			Map<String, Integer> wordCounts = WordCloudUtility.getWordCounts(selectedNodes, 
					this.cyApplicationManager.getCurrentNetwork(),
					nodesPerWordMap,
					this.wordCloudSettingsHolder);
			
			// System.out.println("Populating wordcloud with wordCounts: " + wordCounts);
			
			this.wordCloudDialog.populateWordCloud(wordCounts, wordCloudSettingsHolder, nodesPerWordMap);
		} else {
			this.wordCloudDialog.clearWordCloud();
		}
	}
}
