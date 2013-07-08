package org.cytoscape.wordcloud.internal.ui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.cytoscape.wordcloud.internal.WordCloudSettings;
import org.cytoscape.wordcloud.internal.swing.WrapLayout;

public class WordCloudDialog extends JDialog {

	private static final long serialVersionUID = -7903863195663449806L;
	
	private javax.swing.JButton settingsButton;
    private javax.swing.JPanel wordCloudPanel;
	
	public WordCloudDialog(JFrame owner) {
		super(owner);
		
		initComponents();
		
		//this.setAlwaysOnTop(true);
		this.setSize(400, 400);
	}
	
	private void initComponents() {

        settingsButton = new javax.swing.JButton();
        wordCloudPanel = new javax.swing.JPanel();

//        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        settingsButton.setText("Settings");
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });

        wordCloudPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Output"));

        javax.swing.GroupLayout wordCloudPanelLayout = new javax.swing.GroupLayout(wordCloudPanel);
        wordCloudPanel.setLayout(wordCloudPanelLayout);
        wordCloudPanelLayout.setHorizontalGroup(
            wordCloudPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        wordCloudPanelLayout.setVerticalGroup(
            wordCloudPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 325, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 293, Short.MAX_VALUE)
                        .addComponent(settingsButton))
                    .addComponent(wordCloudPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(wordCloudPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsButton)
                .addContainerGap())
        );

        pack();
    }
	
	private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
	
	public void populateWordCloud(Map<String, Integer> wordCounts, WordCloudSettings wordCloudSettings) {
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
			
			int fontSize = calculateFontSize(count, minAppearCount, maxAppearCount, 10, 20);
			
			JLabel label = new JLabel(word);
			
			Font defaultLabelFont = label.getFont();
			Font font = new Font(defaultLabelFont.getFontName(), defaultLabelFont.getStyle(), fontSize);
			
			label.setFont(font);
			
			this.wordCloudPanel.add(label);
		}
		
		// this.pack();
		this.wordCloudPanel.validate();
		this.wordCloudPanel.repaint();
	}
	
	public void clearWordCloud() {
		this.wordCloudPanel.removeAll();
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
