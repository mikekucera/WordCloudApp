package org.cytoscape.wordcloud.internal.ui;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.cytoscape.wordcloud.internal.WordCloudSettings;

public class WordCloudSettingsDialog extends JDialog {
	
	private WordCloudSettings wordCloudSettings;
	
	public WordCloudSettingsDialog(JFrame owner, WordCloudSettings wordCloudSettings) {
		this.wordCloudSettings = wordCloudSettings;
	}
}
