package org.cytoscape.sample.internal;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import javax.swing.JLabel;

//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.plot.PiePlot3D;
//import org.jfree.data.general.DefaultPieDataset;
//import org.jfree.data.general.PieDataset;
//import org.jfree.util.Rotation;

public class MyCytoPanel extends JPanel implements CytoPanelComponent {
	
	
	private static final long serialVersionUID = 8292806967891823933L;

//	public MyCytoPanel(String applicationTitle, String chartTitle) {
//        super();
//        // This will create the dataset 
//        PieDataset dataset = createDataset();
//        // based on the dataset we create the chart
//        JFreeChart chart = createChart(dataset, chartTitle);
//        // we put the chart into a panel
//        ChartPanel chartPanel = new ChartPanel(chart);
//        // default size
//        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
//        // add it to our application
//        add(chartPanel);
//
//		this.setVisible(true);
//	}
//
//
	public Component getComponent() {
		return this;
	}


	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.WEST;
	}


	public String getTitle() {
		return "MyPanel";
	}


	public Icon getIcon() {
		return null;
	}

}
