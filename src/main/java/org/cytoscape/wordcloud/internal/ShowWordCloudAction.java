package org.cytoscape.wordcloud.internal;

import java.awt.Point;
import java.awt.event.ActionEvent;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.w3c.dom.views.AbstractView;

public class ShowWordCloudAction extends AbstractCyAction {

	WordCloudDialog wordCloudDialog;
	
	CySwingApplication cySwingApplication;
	
	public ShowWordCloudAction(CySwingApplication cySwingApplication) {
		super("Show Word Cloud");
		setPreferredMenu("Apps.WordCloud");
		
		this.cySwingApplication = cySwingApplication;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("Word Cloud clicked");
		
		if (this.wordCloudDialog == null) {
			this.wordCloudDialog = new WordCloudDialog(this.cySwingApplication.getJFrame());
		}
		
		this.wordCloudDialog.setVisible(true);
		
		this.wordCloudDialog.setLocationRelativeTo(this.cySwingApplication.getJFrame());

		// Offset the dialog slightly
		Point currentLocation = this.wordCloudDialog.getLocation();
		this.wordCloudDialog.setLocation((int) currentLocation.getX() + 400, (int) currentLocation.getY() - 240);
		
	}
}
