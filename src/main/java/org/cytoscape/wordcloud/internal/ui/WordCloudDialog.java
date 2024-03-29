package org.cytoscape.wordcloud.internal.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.cytoscape.application.CyApplicationConfiguration;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.wordcloud.internal.RowSetListenerToggle;
import org.cytoscape.wordcloud.internal.WordCloudSettingsHolder;
import org.cytoscape.wordcloud.internal.swing.WrapLayout;
import org.cytoscape.wordcloud.internal.util.WordCloudUtility;

public class WordCloudDialog extends JDialog {

	private static final long serialVersionUID = -7903863195663449806L;
	
	private WordCloudSettingsDialog wordCloudSettingsDialog;
	private CySwingApplication cySwingApplication;
	private CyApplicationManager cyApplicationManager;
	private CyEventHelper cyEventHelper;
	private CyNetworkViewManager cyNetworkViewManager;
	
	javax.swing.JButton settingsButton;
    javax.swing.JPanel wordCloudPanel;
    javax.swing.JScrollPane wordCloudScrollPane;
    
    // Used to position the dialog for the first time it is shown
    private boolean firstTimeShown = true;

	public WordCloudDialog(JFrame owner, 
			WordCloudSettingsDialog wordCloudSettingsDialog, 
			CySwingApplication cySwingApplication,
			CyApplicationManager cyApplicationManager,
			CyEventHelper cyEventHelper,
			CyNetworkViewManager cyNetworkViewManager) {
		super(owner);
		
		initComponents();
		
		this.wordCloudSettingsDialog = wordCloudSettingsDialog;
		this.cySwingApplication = cySwingApplication;
		this.cyApplicationManager = cyApplicationManager;
		this.cyEventHelper = cyEventHelper;
		this.cyNetworkViewManager = cyNetworkViewManager;
		
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
		
		this.wordCloudSettingsDialog.updateToSettings();
		this.wordCloudSettingsDialog.setVisible(true);
    }
	
	/**
	 * Populate the word cloud
	 * @param wordCounts The word counts used to get words and their font sizes
	 * @param wordCloudSettings The settings used to adjust output
	 * @param nodesPerWordMap The nodes per word map used for the 'click on word to get selection of containing nodes' 
	 * feature
	 */
	public void populateWordCloudNormalized(
			Map<String, Integer> wordCounts, 
			Map<String, Integer> networkWordCounts,
			int selectedNodeCount,
			int networkNodeCount,
			WordCloudSettingsHolder wordCloudSettings,
			Map<String, Collection<CyNode>> nodesPerWordMap) {
		
		this.wordCloudPanel.removeAll();
		this.wordCloudPanel.setLayout(new WrapLayout());
		
		
		// Create a map of each word to its network normalized font size factor
		// --------------------------------------------------------------------
		
		final Map<String, Double> networkNormalizedSizeFactors = new HashMap<String, Double>();
		
		double normalizationCoefficient = wordCloudSettings.getNormalizationCoefficient();
		double normalizedFontFactor;
		double minNormalizedFontFactor = 0;
		double maxNormalizedFontFactor = 0;
		boolean firstIteration = true;
		
		for (Entry<String, Integer> entry : wordCounts.entrySet()) {
			
			/*
			System.out.println("Normalized call");
			System.out.println("entry.getValue() " + entry.getValue());
			System.out.println("selectedNodeCount " + entry.getValue());
			System.out.println("networkWordCounts.get(entry.getKey()) + entry.getValue() " + networkWordCounts.get(entry.getKey()) + entry.getValue());
			System.out.println("networkNodeCount" + networkNodeCount);
			System.out.println("normalizationCoefficient" + normalizationCoefficient);
			*/
			
			int wordNetworkAppearanceCount = entry.getValue();
			
			if (networkWordCounts.get(entry.getKey()) != null) {
				wordNetworkAppearanceCount += networkWordCounts.get(entry.getKey());
			}
			
			normalizedFontFactor = calculateNormalizedFontSizeFactor(
					entry.getValue(), 
					selectedNodeCount, 
					wordNetworkAppearanceCount, 
					networkNodeCount,
					normalizationCoefficient);
			
			networkNormalizedSizeFactors.put(entry.getKey(), normalizedFontFactor);
			
			if (firstIteration) {
				minNormalizedFontFactor = normalizedFontFactor;
				maxNormalizedFontFactor = normalizedFontFactor;
				
				firstIteration = false;
			} else {
				if (normalizedFontFactor > maxNormalizedFontFactor) {
					maxNormalizedFontFactor = normalizedFontFactor;
				}
				
				if (normalizedFontFactor < minNormalizedFontFactor) {
					minNormalizedFontFactor = normalizationCoefficient;
				}
			}
		}
		
		
		ArrayList<Entry<String, Integer>> wordCountEntryArray = new ArrayList<Entry<String, Integer>>();
		wordCountEntryArray.addAll(wordCounts.entrySet());
		
		Collections.sort(wordCountEntryArray, new Comparator<Object>() {

			@Override
			public int compare(Object first, Object second) {
				double firstFactor = networkNormalizedSizeFactors.get(((Entry<String, Integer>) first).getKey());
				double secondFactor = networkNormalizedSizeFactors.get(((Entry<String, Integer>) second).getKey());
			
				if (Math.abs(secondFactor - firstFactor) < 0.00001) {
					return 0;
				} else {
					return (int) Math.signum(secondFactor - firstFactor);
				}
			}
			
		});
		
		int maxWordCount = wordCloudSettings.getMaxWordCount();
		
		
		// If we're using normalization, calculate font size-related values for each word
		/*
		Map<String, >
		if (wordCloudSettings.isUsingNormalization()) {
			
		}
		*/
		
		for (int i = 0; i < maxWordCount; i++) {
			if (i >= wordCountEntryArray.size()) {
				break;
			}
			
			Entry<String, Integer> entry = wordCountEntryArray.get(i);
			String word = entry.getKey();
			int count = entry.getValue();
			
			
			int fontSize;
			
			fontSize = (int) Math.round(linearInterpolate(networkNormalizedSizeFactors.get(word), 
					minNormalizedFontFactor, 
					maxNormalizedFontFactor,
					wordCloudSettings.getMinWordCloudFontSize(),
					wordCloudSettings.getMaxWordCloudFontSize()));
			/*
			System.out.println("normalized font size for " + word + ": " + fontSize);
			System.out.println("networkNormalizedSizeFactors.get(word)" + " " + networkNormalizedSizeFactors.get(word));
			System.out.println("minNormalizedFontFactor" + " " + minNormalizedFontFactor);
			System.out.println("maxNormalizedFontFactor" + " " + maxNormalizedFontFactor);
			System.out.println("wordCloudSettings.getMinWordCloudFontSize()" + " " + wordCloudSettings.getMinWordCloudFontSize());
			System.out.println("wordCloudSettings.getMaxWordCloudFontSize()" + " " + wordCloudSettings.getMaxWordCloudFontSize());
			*/
			
			// Shorten the word if necessary
			JLabel label = new JLabel(wordCloudSettings.getWordShortener().shortenWord(word));
			
			Font defaultLabelFont = label.getFont();
			Font font = new Font(defaultLabelFont.getFontName(), defaultLabelFont.getStyle(), fontSize);
			
			label.setFont(font);
			label.addMouseListener(new WordCloudLabelMouseListener(label, word, nodesPerWordMap, 
					this.cyApplicationManager,
					this.cyEventHelper,
					this.cyNetworkViewManager));
			
			this.wordCloudPanel.add(label);
		}
		
		// this.pack();
		this.wordCloudScrollPane.validate();
		this.wordCloudScrollPane.repaint();
	}
	
	/**
	 * Populate the word cloud
	 * @param wordCounts The word counts used to get words and their font sizes
	 * @param wordCloudSettings The settings used to adjust output
	 * @param nodesPerWordMap The nodes per word map used for the 'click on word to get selection of containing nodes' 
	 * feature
	 */
	public void populateWordCloud(
			Map<String, Integer> wordCounts,
			WordCloudSettingsHolder wordCloudSettings,
			Map<String, Collection<CyNode>> nodesPerWordMap) {
		
		this.wordCloudPanel.removeAll();
		this.wordCloudPanel.setLayout(new WrapLayout());
		
		// Obtain the min and maximum word appearance counts for calculating font size
		// ---------------------------------------------------------------------------
		
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
		
		
		ArrayList<Entry<String, Integer>> wordCountEntryArray = new ArrayList<Entry<String, Integer>>();
		wordCountEntryArray.addAll(wordCounts.entrySet());
		Collections.sort(wordCountEntryArray, new Comparator<Object>() {

			@Override
			public int compare(Object first, Object second) {
				int firstCount = ((Entry<String, Integer>) first).getValue();
				int secondCount = ((Entry<String, Integer>) second).getValue();
			
				return secondCount - firstCount;
			}
			
		});
		
		int maxWordCount = wordCloudSettings.getMaxWordCount();
		
		
		// If we're using normalization, calculate font size-related values for each word
		/*
		Map<String, >
		if (wordCloudSettings.isUsingNormalization()) {
			
		}
		*/
		
		for (int i = 0; i < maxWordCount; i++) {
			if (i >= wordCountEntryArray.size()) {
				break;
			}
			
			Entry<String, Integer> entry = wordCountEntryArray.get(i);
			String word = entry.getKey();
			int count = entry.getValue();
			
			
			int fontSize;
			
//			if (wordCloudSettings.isUsingNormalization()) {
				
//			} else {
				fontSize = calculateFontSize(count, minAppearCount, maxAppearCount,
						wordCloudSettings.getMinWordCloudFontSize(),
						wordCloudSettings.getMaxWordCloudFontSize());
//			}
			
			// Shorten the word if necessary
			JLabel label = new JLabel(wordCloudSettings.getWordShortener().shortenWord(word));
			
			Font defaultLabelFont = label.getFont();
			Font font = new Font(defaultLabelFont.getFontName(), defaultLabelFont.getStyle(), fontSize);
			
			label.setFont(font);
			label.addMouseListener(new WordCloudLabelMouseListener(label, word, nodesPerWordMap, 
					this.cyApplicationManager,
					this.cyEventHelper,
					this.cyNetworkViewManager));
			
			this.wordCloudPanel.add(label);
		}
		
		// this.pack();
		this.wordCloudScrollPane.validate();
		this.wordCloudScrollPane.repaint();
	}
	
	private class WordCloudLabelMouseListener implements MouseListener {

		private JLabel label;
		private Map<String, Collection<CyNode>> nodesPerWordMap;
		
		private String originalWord;
		
		private CyApplicationManager cyApplicationManager;
		private CyEventHelper cyEventHelper;
		private CyNetworkViewManager cyNetworkViewManager;
		
		public WordCloudLabelMouseListener(
				JLabel label,
				String originalWord,
				Map<String, Collection<CyNode>> nodesPerWordMap,
				CyApplicationManager cyApplicationManager,
				CyEventHelper cyEventHelper,
				CyNetworkViewManager cyNetworkViewManager) {
			
			this.label = label;
			this.originalWord = originalWord;
			this.nodesPerWordMap = nodesPerWordMap;
			
			this.cyApplicationManager = cyApplicationManager;
			this.cyEventHelper = cyEventHelper;
			this.cyNetworkViewManager = cyNetworkViewManager;
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			
			label.setForeground(new Color(0,200,255));
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			
			label.setForeground(Color.BLACK);
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			RowSetListenerToggle.getInstance().stopListening(200);
			
			Collection<CyNode> nodesForWord = this.nodesPerWordMap.get(originalWord);
			
			WordCloudUtility.setNodeSelection(
					nodesForWord, 
					this.cyApplicationManager.getCurrentNetwork(), 
					this.cyEventHelper,
					this.cyNetworkViewManager);
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
		
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
	
	private double linearInterpolate(double inputValue,
			double inputMin, double inputMax, double outputMin, double outputMax) {
		
		if (Math.abs(inputMin - inputMax) < 0.0001) {
			return (outputMin + outputMax) / 2;
		}
		
//		System.out.println("linearInterpolate: " + (inputValue - inputMin) / (inputMax - inputMin) * (outputMax - outputMin) + outputMin);
		// Assumes inputMin <= inputValue <= inputMax
		return (inputValue - inputMin) / (inputMax - inputMin) * (outputMax - outputMin) + outputMin;
	}
	
	private double calculateNormalizedFontSizeFactor(
			int selectedCount,
			int selectedTotal, 
			int networkCount,
			int networkTotal,
			double normalizationCoefficient) {
		
		return (1.0 * selectedCount / selectedTotal) / Math.pow(1.0 * networkCount / networkTotal, normalizationCoefficient);
	}
}
