package org.cytoscape.wordcloud.internal;

import java.awt.Point;
import java.awt.event.ActionEvent;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.wordcloud.internal.ui.WordCloudDialog;
import org.w3c.dom.views.AbstractView;

public class ShowWordCloudAction extends AbstractCyAction {

	WordCloudDialog wordCloudDialog;
	
	CySwingApplication cySwingApplication;
	
	public ShowWordCloudAction(CySwingApplication cySwingApplication, WordCloudDialog wordCloudDialog) {
		super("Show Word Cloud");
		setPreferredMenu("Apps.WordCloud");
		
		this.cySwingApplication = cySwingApplication;
		
		if (wordCloudDialog == null) {
			throw new RuntimeException("wordCloudDialog must not be null");
		}
		
		this.wordCloudDialog = wordCloudDialog;
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
		
		this.wordCloudDialog.setVisible(true);
	}
}
