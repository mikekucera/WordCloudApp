package org.cytoscape.wordcloud.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;


import org.cytoscape.application.swing.CyAction;
import org.osgi.framework.BundleContext;

import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.wordcloud.internal.ui.WordCloudDialog;

import java.util.Properties;


public class CyActivator extends AbstractCyActivator {
	public CyActivator() {
		super();
	}


	public void start(BundleContext bc) {
		
		// Create word cloud settings object
		WordCloudSettings wordCloudSettings = new WordCloudSettings();
		
		// Create the word cloud dialog
		CySwingApplication cySwingApplication = getService(bc, CySwingApplication.class);
		WordCloudDialog wordCloudDialog = new WordCloudDialog(cySwingApplication.getJFrame());
		
		// Create & register menu item
		ShowWordCloudAction showWordCloudAction = new ShowWordCloudAction(cySwingApplication, wordCloudDialog);
		registerService(bc, showWordCloudAction, CyAction.class, new Properties());
		
		// Create & register node selection listener
		CyApplicationManager cyApplicationManager = getService(bc, CyApplicationManager.class);
		WordCloudRowSetListener wordCloudRowSetListener = new WordCloudRowSetListener(cyApplicationManager, wordCloudDialog, wordCloudSettings);
		registerAllServices(bc, wordCloudRowSetListener, new Properties());
		
		
		// System.out.println("rowSetListener registered");
	}
	

}

