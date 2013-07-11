package org.cytoscape.wordcloud.internal.ui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.cytoscape.application.CyApplicationConfiguration;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.wordcloud.internal.WordCloudSettingsHolder;
import org.cytoscape.wordcloud.internal.swing.WrapLayout;

public class WordCloudDialog extends JDialog {

	private static final long serialVersionUID = -7903863195663449806L;
	
	private WordCloudSettingsDialog wordCloudSettingsDialog;
	private CySwingApplication cySwingApplication;
	private CyApplicationManager cyApplicationManager;
	
	javax.swing.JButton settingsButton;
    javax.swing.JPanel wordCloudPanel;
    javax.swing.JScrollPane wordCloudScrollPane;
    
    // Used to position the dialog for the first time it is shown
    private boolean firstTimeShown = true;
	
	public WordCloudDialog(JFrame owner, 
			WordCloudSettingsDialog wordCloudSettingsDialog, 
			CySwingApplication cySwingApplication,
			CyApplicationManager cyApplicationManager) {
		super(owner);
		
		initComponents();
		
		this.wordCloudSettingsDialog = wordCloudSettingsDialog;
		this.cySwingApplication = cySwingApplication;
		this.cyApplicationManager = cyApplicationManager;
		
		this.setSize(450, 450);
	}
	
	public boolean isFirstTimeShown() {
		return this.firstTimeShown;
	}
	
	public void setFirstTimeShown(boolean firstTimeShown) {
		this.firstTimeShown = firstTimeShown;
	}
	
	private void initComponents() {

        wordCloudScrollPane = new javax.swing.JScrollPane();
        wordCloudPanel = new javax.swing.JPanel();
        settingsButton = new javax.swing.JButton();

        setTitle("Word Cloud");

        wordCloudScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout wordCloudPanelLayout = new javax.swing.GroupLayout(wordCloudPanel);
        wordCloudPanel.setLayout(wordCloudPanelLayout);
        wordCloudPanelLayout.setHorizontalGroup(
            wordCloudPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 458, Short.MAX_VALUE)
        );
        wordCloudPanelLayout.setVerticalGroup(
            wordCloudPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 309, Short.MAX_VALUE)
        );

        wordCloudScrollPane.setViewportView(wordCloudPanel);

        settingsButton.setText("Settings");
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(wordCloudScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(settingsButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(wordCloudScrollPane)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsButton)
                .addContainerGap())
        );

        pack();
    }
	
	private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {
		
		if (this.wordCloudSettingsDialog.isFirstTimeShown()) {
			this.wordCloudSettingsDialog.setLocationRelativeTo(this.cySwingApplication.getJFrame());
	
			// Offset the dialog slightly
			Point currentLocation = this.wordCloudSettingsDialog.getLocation();
			this.wordCloudSettingsDialog.setLocation((int) currentLocation.getX() - 350, (int) currentLocation.getY() - 200);
			this.wordCloudSettingsDialog.setFirstTimeShown(false);
		}
		
		this.wordCloudSettingsDialog.updateIncludedColumns(cyApplicationManager);
		this.wordCloudSettingsDialog.setVisible(true);
    }
	
	public void populateWordCloud(Map<String, Integer> wordCounts, WordCloudSettingsHolder wordCloudSettings) {
		if (!this.isVisible()) {
			// Word cloud updated while not visible
			return;
		}
		
		this.wordCloudPanel.removeAll();
		this.wordCloudPanel.setLayout(new WrapLayout());
		
		// Obtain the min and maximum word appearance counts
		int minAppearCount = 0;
		int maxAppearCount = 0;
		
		if (wordCounts.size() > 0) {
			minAppearCount = wordCounts.values().iterator().next();
			maxAppearCount = minAppearCount;
		}
		
		for (Integer appearCount : wordCounts.values()) {
			if (appearCount < minAppearCount) {
				minAppearCount = appearCount;
			}
			
			if (appearCount > maxAppearCount) {
				maxAppearCount = appearCount;
			}
		}
		
		for (Entry<String, Integer> entry : wordCounts.entrySet()) {
			String word = entry.getKey();
			int count = entry.getValue();
			
			int fontSize = calculateFontSize(count, minAppearCount, maxAppearCount,
					wordCloudSettings.getMinWordCloudFontSize(),
					wordCloudSettings.getMaxWordCloudFontSize());
			
			// Shorten the word if necessary
			JLabel label = new JLabel(wordCloudSettings.getWordShortener().shortenWord(word));
			
			Font defaultLabelFont = label.getFont();
			Font font = new Font(defaultLabelFont.getFontName(), defaultLabelFont.getStyle(), fontSize);
			
			label.setFont(font);
			
			this.wordCloudPanel.add(label);
		}
		
		// this.pack();
		this.wordCloudScrollPane.validate();
		this.wordCloudScrollPane.repaint();
	}
	
	public void clearWordCloud() {
		this.wordCloudPanel.removeAll();
		
		this.wordCloudScrollPane.validate();
		this.wordCloudScrollPane.repaint();
	}
	
	private int calculateFontSize(int appearCount, 
			int minAppearCount, int maxAppearCount, int minFontSize, int maxFontSize) {
		
		if (minAppearCount == maxAppearCount) {
			return (int) Math.round((minFontSize + maxFontSize) / 2.0);
		}
		
		return (int) Math.round(
					(appearCount - minAppearCount) / (1.0 * maxAppearCount - minAppearCount) 
					* (maxFontSize - minFontSize) + minFontSize
				);
	}
}
