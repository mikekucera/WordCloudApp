package org.cytoscape.wordcloud.internal;

import org.cytoscape.application.swing.CySwingApplication;


import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.event.CyListener;

import org.osgi.framework.BundleContext;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.model.events.RowSetRecord;
import org.cytoscape.model.events.RowsSetEvent;
import org.cytoscape.model.events.RowsSetListener;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.wordcloud.internal.MyCytoPanel;
import org.cytoscape.wordcloud.internal.Sample28;

import java.util.Collection;
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
		CyListener a;
		
		RowsSetListener rowsSetListener = new RowsSetListener() {
			
			@Override
			public void handleEvent(RowsSetEvent e) {
				System.out.println("rowSetListener alerted");
				
				System.out.println("Selection event? " + e.containsColumn("selected"));
				
				System.out.println("id Event? " + e.containsColumn("o_id28349278l"));
				
				// Collection<RowSetRecord> rowSetRecords = e.getColumnRecords("selected");

				/*
				System.out.println("Start Print RowSetRecords");
				for (RowSetRecord rowSetRecord : rowSetRecords) {
					System.out.println(rowSetRecord.getValue());
				}
				System.out.println("End Print RowSetRecords");
				*/
			}
		};
		
		registerAllServices(bc, rowsSetListener, new Properties());
		System.out.println("rowSetListener registered");
	}
	
	private Collection<CyNode> fetchSelectedNodes(CyNetwork network) {
		return CyTableUtil.getNodesInState(network,"selected",true);
	}
}

