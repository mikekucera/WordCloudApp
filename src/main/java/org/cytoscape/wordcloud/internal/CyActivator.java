package org.cytoscape.wordcloud.internal;

import org.cytoscape.application.swing.CySwingApplication;


import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CyAction;

import org.osgi.framework.BundleContext;

import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.wordcloud.internal.MyCytoPanel;
import org.cytoscape.wordcloud.internal.Sample28;

import java.util.Properties;


public class CyActivator extends AbstractCyActivator {
	public CyActivator() {
		super();
	}


	public void start(BundleContext bc) {

//		CySwingApplication cytoscapeDesktopService = getService(bc,CySwingApplication.class);
		
//		MyCytoPanel myCytoPanel = new MyCytoPanel("Chart Test","This is my Chart");
//		Sample28 sample28Action = new Sample28(cytoscapeDesktopService,myCytoPanel);
//		
//		registerService(bc,myCytoPanel,CytoPanelComponent.class, new Properties());
//		registerService(bc,sample28Action,CyAction.class, new Properties());
		
//		System.out.println("SampleApp 28 loaded");
		
		ShowWordCloudAction showWordCloudAction = new ShowWordCloudAction();
		
		registerService(bc, showWordCloudAction, CyAction.class, new Properties());
	}
}

