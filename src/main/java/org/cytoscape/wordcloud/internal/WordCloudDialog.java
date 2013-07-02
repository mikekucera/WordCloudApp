package org.cytoscape.wordcloud.internal;

import java.awt.FlowLayout;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
	
	public void populateWordCloud(Map<String, Integer> wordCounts) {
		if (!this.isVisible()) {
//			System.out.println("Word cloud updated while not visible, doing nothing ..");
			return;
		}
		
		JLabel contents = new JLabel(wordCounts.toString());
		
		this.wordCloudPanel.removeAll();
		this.wordCloudPanel.setLayout(new FlowLayout());
		this.wordCloudPanel.add(contents);
		
		System.out.println("Updated word cloud3");
		
		// this.pack();
		this.wordCloudPanel.validate();
		this.wordCloudPanel.repaint();
		
		// TODO: Consider using wraplayout
		// TODO: Need to split map into strings
	}
	
	public void clearWordCloud() {
		this.wordCloudPanel.removeAll();
	}
}
