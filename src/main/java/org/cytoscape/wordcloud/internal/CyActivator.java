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

//		CySwingApplication cytoscapeDesktopService = getService(bc,CySwingApplication.class);
		
//		MyCytoPanel myCytoPanel = new MyCytoPanel("Chart Test","This is my Chart");
//		Sample28 sample28Action = new Sample28(cytoscapeDesktopService,myCytoPanel);
//		
//		registerService(bc,myCytoPanel,CytoPanelComponent.class, new Properties());
//		registerService(bc,sample28Action,CyAction.class, new Properties());
		
//		System.out.println("SampleApp 28 loaded");
		
		ShowWordCloudAction showWordCloudAction = new ShowWordCloudAction();
		
		registerService(bc, showWordCloudAction, CyAction.class, new Properties());
		
		CyApplicationManager cyApplicationManager = getService(bc, CyApplicationManager.class);
		
		WordCloudRowSetListener wordCloudRowSetListener = new WordCloudRowSetListener(cyApplicationManager);
		registerAllServices(bc, wordCloudRowSetListener, new Properties());
		
		RowsSetListener rowsSetListener = new RowsSetListener() {
			
			@Override
			public void handleEvent(RowsSetEvent e) {
				System.out.println("rowSetListener alerted");
				
				System.out.println("Selection event? " + e.containsColumn("selected"));
				
//				System.out.println("id Event? " + e.containsColumn("o_id28349278l"));
				
				//RowSetRecord a;
				
				RowSetRecord a1;
		
				
//				e.getSource();
		
				// System.out.println("Source table: " + e.getSource().getSUID());
				
				/*
				List<CyNode> selectedNodes = fetchSelectedNodes(cyApplicationManager.getCurrentNetwork());
				System.out.println("Number of selected nodes on current network: " + selectedNodes.size());
				
				CyTable cyTable = cyApplicationManager.getCurrentTable();
				*/
				
				//cyTable.
				
//				cyTable.getRow(primaryKey);
				
				/*
				System.out.println("Start printing all attributes");
				for (CyRow cyRow : cyTable.getAllRows()) { 
					System.out.println(cyRow.getAllValues());
				}
				*/
				// CyTable cyTable = e.getSource();
				
				
				/*
				CyNetwork a;
				CyApplicationManager c1;
//				c1.
				CyNetworkManager c2;
				//c2.
				// Collection<RowSetRecord> rowSetRecords = e.getColumnRecords("selected");
				CyTableManager c3;
				
				CySwingApplication c4;
				*/
				
				/*
				System.out.println("Start Print RowSetRecords");
				for (RowSetRecord rowSetRecord : rowSetRecords) {
					System.out.println(rowSetRecord.getValue());
				}
				System.out.println("End Print RowSetRecords");
				*/
			}
		};
		
//		registerAllServices(bc, rowsSetListener, new Properties());
		System.out.println("rowSetListener registered");
	}
	

}

