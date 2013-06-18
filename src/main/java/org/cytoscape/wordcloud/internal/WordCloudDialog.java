package org.cytoscape.wordcloud.internal;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class WordCloudDialog extends JDialog {

	private static final long serialVersionUID = -7903863195663449806L;
	
	public WordCloudDialog(JFrame owner) {
		super(owner);
		
		//this.setAlwaysOnTop(true);
		this.setSize(400, 400);
	}
}
