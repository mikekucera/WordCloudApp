package org.cytoscape.wordcloud.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;


import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.event.CyListener;

import org.osgi.framework.BundleContext;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.model.events.RowSetRecord;
import org.cytoscape.model.events.RowsSetEvent;
import org.cytoscape.model.events.RowsSetListener;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.wordcloud.internal.MyCytoPanel;
import org.cytoscape.wordcloud.internal.Sample28;

import java.util.Collection;
import java.util.List;
import java.util.Properties;


public class CyActivator extends AbstractCyActivator {
	public CyActivator() {
		super();
	}


	public void start(BundleContext bc) {
		
		// Create the word cloud dialog
		CySwingApplication cySwingApplication = getService(bc, CySwingApplication.class);
		WordCloudDialog wordCloudDialog = new WordCloudDialog(cySwingApplication.getJFrame());
		
		// Create & register menu item
		ShowWordCloudAction showWordCloudAction = new ShowWordCloudAction(cySwingApplication, wordCloudDialog);
		registerService(bc, showWordCloudAction, CyAction.class, new Properties());
		
		// Create & register node selection listener
		CyApplicationManager cyApplicationManager = getService(bc, CyApplicationManager.class);
		WordCloudRowSetListener wordCloudRowSetListener = new WordCloudRowSetListener(cyApplicationManager, wordCloudDialog);
		registerAllServices(bc, wordCloudRowSetListener, new Properties());
		
		
		
		System.out.println("rowSetListener registered");
	}
	

}

