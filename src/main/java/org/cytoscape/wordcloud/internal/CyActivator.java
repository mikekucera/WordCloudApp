package org.cytoscape.wordcloud.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;


import org.cytoscape.application.swing.CyAction;
import org.osgi.framework.BundleContext;

import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.wordcloud.internal.ui.WordCloudDialog;
import org.cytoscape.wordcloud.internal.ui.WordCloudSettingsDialog;

import java.util.Properties;


public class CyActivator extends AbstractCyActivator {
	public CyActivator() {
		super();
	}


	public void start(BundleContext bc) {
		
		CySwingApplication cySwingApplication = getService(bc, CySwingApplication.class);
		CyApplicationManager cyApplicationManager = getService(bc, CyApplicationManager.class);
		
		// Create word cloud settings object
		WordCloudSettingsHolder wordCloudSettings = new WordCloudSettingsHolder();
		
		// Create the word cloud settings dialog
		WordCloudSettingsDialog wordCloudSettingsDialog = new WordCloudSettingsDialog(cySwingApplication.getJFrame(), wordCloudSettings, cySwingApplication);
		
		// Create the word cloud dialog
		WordCloudDialog wordCloudDialog = new WordCloudDialog(cySwingApplication.getJFrame(), 
				wordCloudSettingsDialog, cySwingApplication);
		
		// Create & register menu item
		ShowWordCloudAction showWordCloudAction = new ShowWordCloudAction(cySwingApplication, wordCloudDialog);
		registerService(bc, showWordCloudAction, CyAction.class, new Properties());
		
		// Create & register node selection listener
		WordCloudRowSetListener wordCloudRowSetListener = new WordCloudRowSetListener(cyApplicationManager, wordCloudDialog, wordCloudSettings);
		registerAllServices(bc, wordCloudRowSetListener, new Properties());
		
		
		// System.out.println("rowSetListener registered");
	}
	

}

