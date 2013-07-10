package org.cytoscape.wordcloud.internal.ui;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.cytoscape.wordcloud.internal.WordCloudSettingsHolder;

public class WordCloudSettingsDialog extends JDialog {
	
	private WordCloudSettingsHolder wordCloudSettings;
	
	private javax.swing.JLabel excludedColumnsLabel;
    private javax.swing.JPanel excludedColumnsPanel;
    private javax.swing.JScrollPane excludedColumnsScrollPane;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.JScrollPane settingsScrollPane;
	
 // Used to position the dialog for the first time it is shown
    private boolean firstTimeShown = true;
    
	public WordCloudSettingsDialog(JFrame owner, WordCloudSettingsHolder wordCloudSettings) {
		super(owner, false);
		 
		this.wordCloudSettings = wordCloudSettings;
		 
	    initComponents();
	}
	
	public boolean isFirstTimeShown() {
		return this.firstTimeShown;
	}
	
	public void setFirstTimeShown(boolean firstTimeShown) {
		this.firstTimeShown = firstTimeShown;
	}
	
	private void initComponents() {

        settingsScrollPane = new javax.swing.JScrollPane();
        settingsPanel = new javax.swing.JPanel();
        excludedColumnsLabel = new javax.swing.JLabel();
        excludedColumnsScrollPane = new javax.swing.JScrollPane();
        excludedColumnsPanel = new javax.swing.JPanel();

        setTitle("Word Cloud Settings");

        settingsScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        settingsPanel.setPreferredSize(new java.awt.Dimension(400, 350));

        excludedColumnsLabel.setText("Use following columns to generate word cloud:");

        excludedColumnsScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout excludedColumnsPanelLayout = new javax.swing.GroupLayout(excludedColumnsPanel);
        excludedColumnsPanel.setLayout(excludedColumnsPanelLayout);
        excludedColumnsPanelLayout.setHorizontalGroup(
            excludedColumnsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );
        excludedColumnsPanelLayout.setVerticalGroup(
            excludedColumnsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );

        excludedColumnsScrollPane.setViewportView(excludedColumnsPanel);

        javax.swing.GroupLayout settingsPanelLayout = new javax.swing.GroupLayout(settingsPanel);
        settingsPanel.setLayout(settingsPanelLayout);
        settingsPanelLayout.setHorizontalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, settingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(excludedColumnsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(excludedColumnsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
                .addContainerGap())
        );
        settingsPanelLayout.setVerticalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(excludedColumnsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(excludedColumnsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(213, Short.MAX_VALUE))
        );

        settingsScrollPane.setViewportView(settingsPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(settingsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(settingsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }
}
