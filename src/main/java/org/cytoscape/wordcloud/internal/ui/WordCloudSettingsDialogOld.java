package org.cytoscape.wordcloud.internal.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.util.swing.CheckBoxJList;


import org.cytoscape.wordcloud.internal.ui.old.CollapsiblePanel;
import org.cytoscape.wordcloud.internal.ui.old.ModifiedFlowLayout;
import org.cytoscape.wordcloud.internal.ui.old.SliderBarPanel;
import org.cytoscape.wordcloud.internal.ui.old.WidestStringComboBoxModel;
import org.cytoscape.wordcloud.internal.ui.old.WidestStringComboBoxPopupMenuListener;
import org.cytoscape.wordcloud.internal.WordCloudSettingsHolder;

public class WordCloudSettingsDialogOld extends JDialog {
	
	private WordCloudSettingsHolder wordCloudSettings;
	
	private DecimalFormat decFormat; //used in formatted text fields with decimals
	private NumberFormat intFormat; //used in formatted text fields with integers
	
	private javax.swing.JLabel excludedColumnsLabel;
    private javax.swing.JPanel excludedColumnsPanel;
    private javax.swing.JScrollPane excludedColumnsScrollPane;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.JScrollPane settingsScrollPane;
	
    private CySwingApplication cySwingApplication;
    
 // Used to position the dialog for the first time it is shown
    private boolean firstTimeShown = true;

	private CheckBoxJList attributeList;
	private JPopupMenu attributeSelectionPopupMenu;
	private JTextArea attNames;
	private JFormattedTextField maxWordsTextField;
	private JFormattedTextField clusterCutoffTextField;
	private JCheckBox useNetworkCounts;
	private SliderBarPanel sliderPanel;
	private JFormattedTextField addWordTextField;
	private JButton addWordButton;
	private JComboBox cmbRemoval;
	private JButton removeWordButton;
	private JCheckBox numExclusion;
	
	private JComboBox cmbDelimiterAddition;
	private JButton addDelimiterButton;
	private JComboBox cmbDelimiterRemoval;
	private JButton removeDelimiterButton;
	private JCheckBox stemmer;
	private JComboBox cmbStyle;
	private JButton createNetworkButton;
	private JButton saveCloudButton;
    
	public WordCloudSettingsDialogOld(JFrame owner, WordCloudSettingsHolder wordCloudSettings, CySwingApplication cySwingApplication) {
		super(owner, false);
		 
		this.wordCloudSettings = wordCloudSettings;
		this.cySwingApplication = cySwingApplication;
		 
	    initComponents();
	    
	    setupPanel();
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

        setTitle("Word Cloud Settings");

        settingsScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        settingsPanel.setPreferredSize(new java.awt.Dimension(400, 350));

        javax.swing.GroupLayout settingsPanelLayout = new javax.swing.GroupLayout(settingsPanel);
        settingsPanel.setLayout(settingsPanelLayout);
        settingsPanelLayout.setHorizontalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 443, Short.MAX_VALUE)
        );
        settingsPanelLayout.setVerticalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 373, Short.MAX_VALUE)
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
	
	private void setupPanel() {
		decFormat = new DecimalFormat();
		decFormat.setParseIntegerOnly(false);
		
		intFormat = NumberFormat.getIntegerInstance();
		intFormat.setParseIntegerOnly(true);
		
		settingsPanel.setLayout(new BorderLayout());
		
		//INITIALIZE PARAMETERS
		
		//Create the three main panels: CloudList, Options, and Bottom
		
		// Put the CloudList in a scroll pane
	//	JPanel cloudList = createCopyOfWordCloudSettingsDialogCloudListPanel();
	//	JScrollPane cloudListScroll = new JScrollPane(cloudList);
		
		//Put the Options in a scroll pane
		JPanel optionsPanel = createOptionsPanel();
		JScrollPane optionsScroll = new JScrollPane(optionsPanel);
		optionsScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		//Add button to bottom
	//	JPanel bottomPanel = createBottomPanel();
		
		//Add all the vertically aligned components to the main panel
	//	add(cloudListScroll,BorderLayout.NORTH);
		settingsPanel.add(optionsScroll,BorderLayout.CENTER);
		
	//	settingsPanel.add(bottomPanel,BorderLayout.SOUTH);
		
		settingsScrollPane.validate();
		settingsScrollPane.repaint();
	}
	
	public JPanel createOptionsPanel()
	{
		CollapsiblePanel collapsiblePanel = new CollapsiblePanel("Cloud Parameters");
		collapsiblePanel.setCollapsed(false);
		collapsiblePanel.getTitleComponent().setToolTipText("Parameters that can be set differently for each individual cloud");
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		//Semantic Analysis Panel
		CollapsiblePanel semAnalysis = createSemAnalysisPanel();
		semAnalysis.setCollapsed(false);
		
		//Display Settings
		CollapsiblePanel displaySettings = createDisplaySettingsPanel();
		displaySettings.setCollapsed(true);
		
		//Cloud Layout
		CollapsiblePanel cloudLayout = createCloudLayoutPanel();
		cloudLayout.setCollapsed(true);
		
		//Add all Panels
		panel.add(semAnalysis);
		panel.add(displaySettings);
		panel.add(cloudLayout);
		
		collapsiblePanel.getContentPane().add(panel, BorderLayout.NORTH);
		
		//Network Level Panel
		CollapsiblePanel collapsiblePanel2 = new CollapsiblePanel("Text Processing Parameters");
		collapsiblePanel2.setCollapsed(false);
		collapsiblePanel2.getTitleComponent().setToolTipText("Text processing parameters that will be applied to all clouds created from the current network");
		
		JPanel networkPanel = new JPanel();
		networkPanel.setLayout(new BoxLayout(networkPanel, BoxLayout.Y_AXIS));
		
		//Word Exclusion
		CollapsiblePanel exclusionList = createExclusionListPanel();
		exclusionList.setCollapsed(true);
		
		networkPanel.add(exclusionList);
		
		//Delimiter/Tokenization Panel
		CollapsiblePanel tokenizationPanel = createTokenizationPanel();
		tokenizationPanel.setCollapsed(true);
		
		networkPanel.add(tokenizationPanel);
		
		//Stemmer Panel
		CollapsiblePanel stemmingPanel = createStemmingPanel();
		stemmingPanel.setCollapsed(true);
		
		networkPanel.add(stemmingPanel);
		
		//Add to collapsible panel
		collapsiblePanel2.getContentPane().add(networkPanel, BorderLayout.NORTH);
		
		//Container Panel for Cloud and Network parameters
		JPanel newPanel = new JPanel();
		newPanel.setLayout(new BorderLayout());
		newPanel.add(collapsiblePanel, BorderLayout.NORTH);
		newPanel.add(collapsiblePanel2, BorderLayout.CENTER);
				
		return newPanel;
	}
	
	/**
	 * Creates a CollapsiblePanel that holds the Semantic Analysis information.
	 * @return CollapsiblePanel - semantic analysis panel interface.
	 */
	private CollapsiblePanel createSemAnalysisPanel()
	{
		CollapsiblePanel collapsiblePanel = new CollapsiblePanel("Table Column Choice");
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		
		
		JPanel attributePanel = new JPanel();
		attributePanel.setLayout(new GridBagLayout());

	    //Testing of stuff
	    attributeList = new CheckBoxJList();
	    DefaultListModel model = new DefaultListModel();
	    attributeList.setModel(model);
	    attributeList.addListSelectionListener(new ListSelectionListener()
	    {
			public void valueChanged(ListSelectionEvent e) {
				//TODO updateAttNames();
			}
	    });
	    
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setPreferredSize(new Dimension(300, 200));
	    scrollPane.setViewportView(attributeList);
	    
	    attributeSelectionPopupMenu = new JPopupMenu();
	    attributeSelectionPopupMenu.add(scrollPane);
	    
	    JButton attributeButton = new JButton("Edit");
	    attributeButton.setToolTipText("Edit node attribute table columns to use for semantic analysis");
	    attributeButton.addMouseListener(new MouseAdapter()
	    {
	    	public void mouseClicked(MouseEvent e)
	    	{
	    		attributeSelectionPopupMenu.show(e.getComponent(), 0,e.getComponent().getPreferredSize().height);
	    	}
	    }
	    );

	    attNames = new JTextArea();
	    attNames.setColumns(15);
	    attNames.setRows(1);
	    attNames.setEditable(false);
	    JScrollPane attListPane = new JScrollPane();
	    attListPane.setPreferredSize(attNames.getPreferredSize());
	    attListPane.setViewportView(attNames);
	    attListPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    attListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	    JLabel attributeLabel = new JLabel("Current Values:");
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(5, 0, 0, 0);
		attributePanel.add(attributeLabel, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 10, 0, 0);
		attributePanel.add(attListPane, gridBagConstraints);	
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(5, 10, 0, 0);
		attributePanel.add(attributeButton, gridBagConstraints);
		    
	    //TODO refreshAttributeCMB();
		
		panel.add(attributePanel);
		
		collapsiblePanel.getContentPane().add(panel,BorderLayout.NORTH);
		return collapsiblePanel;
	}
	
	/**
	 * Creates a CollapsiblePanel that holds the display settings information.
	 * @return CollapsiblePanel - display settings panel interface.
	 */
	private CollapsiblePanel createDisplaySettingsPanel()
	{
		CollapsiblePanel collapsiblePanel = new CollapsiblePanel("Advanced");
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		//Max words input
		JLabel maxWordsLabel = new JLabel("Max Number of Words");
		maxWordsTextField = new JFormattedTextField(intFormat);
		maxWordsTextField.setColumns(10);
		maxWordsTextField.setValue(this.wordCloudSettings.getMaxWordCount()); //Set to default initially
		maxWordsTextField.addPropertyChangeListener(new FormattedTextFieldAction());
		
		StringBuffer buf = new StringBuffer();
		buf.append("<html>" + "Sets a limit on the number of words to display in the cloud" + "<br>");
		buf.append("<b>Acceptable Values:</b> greater than or equal to 0" + "</html>");
		maxWordsTextField.setToolTipText(buf.toString());
		
		//Max words panel
		JPanel maxWordsPanel = new JPanel();
		maxWordsPanel.setLayout(new BorderLayout());
		maxWordsPanel.add(maxWordsLabel, BorderLayout.WEST);
		maxWordsPanel.add(maxWordsTextField, BorderLayout.EAST);
		
		
		//buf = new StringBuffer();
		//buf.append("<html>" + "Determines how much weight to give the whole network when normalizing the selected nodes" + "<br>");
		//buf.append("<b>Acceptable Values:</b> greater than or equal to 0 and less than or equal to 1" + "</html>");
		
		
		//Clustering Cutoff
		JLabel clusterCutoffLabel = new JLabel("Word Aggregation Cutoff");
		clusterCutoffTextField = new JFormattedTextField(decFormat);
		clusterCutoffTextField.setColumns(3);
		clusterCutoffTextField.setValue(3); //Set to default initially
		clusterCutoffTextField.addPropertyChangeListener(new FormattedTextFieldAction());
		
		buf = new StringBuffer();
		buf.append("<html>" + "Cutoff for placing two words in the same cluster - ratio of the observed joint probability of the words to their joint probability if the words appeared independently of each other" + "<br>");
		buf.append("<b>Acceptable Values:</b> greater than or equal to 0" + "</html>");
		clusterCutoffTextField.setToolTipText(buf.toString());
		
		//Clustering Cutoff Panel
		JPanel clusterCutoffPanel = new JPanel();
		clusterCutoffPanel.setLayout(new BorderLayout());
		clusterCutoffPanel.add(clusterCutoffLabel, BorderLayout.WEST);
		clusterCutoffPanel.add(clusterCutoffTextField, BorderLayout.EAST);
		
		
		//New Network Normalization Panel
		JPanel netNormalizationPanel = new JPanel();
		netNormalizationPanel.setLayout(new GridBagLayout());
		
		//Checkbox
		useNetworkCounts = new JCheckBox("Normalize word size using selection/network ratios");
		useNetworkCounts.setToolTipText("Enables word size to be calculated using using counts over the entire network, rather than just selected nodes");
		// useNetworkCounts.addActionListener( this);
		useNetworkCounts.setSelected(false);
//		useNetworkCounts.setEnabled(false);
		
		sliderPanel = new SliderBarPanel(0,1,"Network Normalization", "Network Normalization", 10);
//		sliderPanel.setEnabled(false);
		sliderPanel.setVisible(false);
		
		buf = new StringBuffer();
		buf.append("<html>" + "Determines how much weight to give the whole network when normalizing the selected nodes" + "<br>");
		buf.append("<b>Acceptable Values:</b> greater than or equal to 0 and less than or equal to 1" + "</html>");
		sliderPanel.setToolTipText(buf.toString());
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5,0,0,0);
		netNormalizationPanel.add(useNetworkCounts, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(0,0,0,0);
		netNormalizationPanel.add(sliderPanel, gridBagConstraints);
		
		//Add components to main panel
		panel.add(maxWordsPanel);
		panel.add(clusterCutoffPanel);
		panel.add(netNormalizationPanel);
		
		
		//Add to collapsible panel
		collapsiblePanel.getContentPane().add(panel, BorderLayout.NORTH);
		
		return collapsiblePanel;
	}
	
	/**
	 * Creates a CollapsiblePanel that holds the word exclusion list information.
	 * @return CollapsiblePanel - word exclusion list panel interface.
	 */
	private CollapsiblePanel createExclusionListPanel()
	{
		CollapsiblePanel collapsiblePanel = new CollapsiblePanel("Word Exclusion List");
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		
		//Add Word
		JLabel addWordLabel = new JLabel("Add Word");
		addWordTextField = new JFormattedTextField();
		addWordTextField.setColumns(15);
		
		addWordTextField.setEditable(true);
		
		addWordTextField.setText("");
		addWordTextField.addPropertyChangeListener(new FormattedTextFieldAction());
		
		StringBuffer buf = new StringBuffer();
		buf.append("<html>" + "Allows for specification of an additional word to be excluded when doing semantic analysis" + "<br>");
		buf.append("<b>Acceptable Values:</b> Only alpha numeric values - no spaces allowed" + "</html>");
		addWordTextField.setToolTipText(buf.toString());
		
		addWordButton = new JButton();
		addWordButton.setText("Add");
//		addWordButton.setEnabled(false);
		// addWordButton.addActionListener(this);
		
		//Word panel
		JPanel wordPanel = new JPanel();
		wordPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5,0,0,0);
		wordPanel.add(addWordLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5,10,0,10);
		wordPanel.add(addWordTextField, gridBagConstraints);
		
		//Button stuff
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(5,0,0,0);
		wordPanel.add(addWordButton, gridBagConstraints);
		
		
		// Word Removal Label
		JLabel removeWordLabel = new JLabel("Remove Word");
		
		//Word Removal Combo Box
		WidestStringComboBoxModel wscbm = new WidestStringComboBoxModel();
		cmbRemoval = new JComboBox(wscbm);
		cmbRemoval.addPopupMenuListener(new WidestStringComboBoxPopupMenuListener());
		cmbRemoval.setEditable(false);
	    Dimension d = cmbRemoval.getPreferredSize();
	    cmbRemoval.setPreferredSize(new Dimension(15, d.height));
//	    cmbRemoval.addItemListener(this);
	    cmbRemoval.setToolTipText("Allows for selection a word to remove from the semantic analysis exclusion list");

	    //Word Removal Button
	    removeWordButton = new JButton();
	    removeWordButton.setText("Remove");
//	    removeWordButton.setEnabled(false);
//	    removeWordButton.addActionListener(this);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5,0,0,0);
		wordPanel.add(removeWordLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 10, 0, 10);
		wordPanel.add(cmbRemoval, gridBagConstraints);
		
		//Button stuff
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
		wordPanel.add(removeWordButton, gridBagConstraints);

//		refreshRemovalCMB();
		
		//Number Exclusion Stuff
		
		//Checkbox
		numExclusion = new JCheckBox("Add the numbers 0 - 999 to the word exclusion list");
		
		buf = new StringBuffer();
		buf.append("<html>" + "Causes numbers in the range 0 - 999 that appear as <b>separate words</b> to be excluded" + "<br>");
		buf.append("<b>Hint:</b> To exclude numbers that appear within a word, either add the entire word to the exclusion list" + "<br>");
		buf.append("or add the specific number to the list of delimiters used for word tokenization" + "</html>");
		numExclusion.setToolTipText(buf.toString());
//		numExclusion.addActionListener(this);
		numExclusion.setSelected(false);
//		numExclusion.setEnabled(false);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5,0,0,0);
		wordPanel.add(numExclusion, gridBagConstraints);
		
		//Add components to main panel
		panel.add(wordPanel);
		
		//Add to collapsible panel
		collapsiblePanel.getContentPane().add(panel, BorderLayout.NORTH);
		
		return collapsiblePanel;
	}
	
	/**
	 * Creates a CollapsiblePanel that holds the word tokenization information.
	 * @return CollapsiblePanel - word tokenization panel interface.
	 */
	private CollapsiblePanel createTokenizationPanel()
	{
		CollapsiblePanel collapsiblePanel = new CollapsiblePanel("Word Tokenization");
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		
		//Add Delimiter
		JLabel addDelimiterLabel = new JLabel("Add Delimiter");

		//Delimiter Addition Combo Box
		WidestStringComboBoxModel wscbm = new WidestStringComboBoxModel();
		cmbDelimiterAddition = new JComboBox(wscbm);
		cmbDelimiterAddition.addPopupMenuListener(new WidestStringComboBoxPopupMenuListener());
		cmbDelimiterAddition.setEditable(false);
	    Dimension d = cmbDelimiterAddition.getPreferredSize();
	    cmbDelimiterAddition.setPreferredSize(new Dimension(15, d.height));
//	    cmbDelimiterAddition.addItemListener(this);
	    
	    StringBuffer buf = new StringBuffer();
		buf.append("<html>" + "Allows for specification of an additional delimiter to be used when tokenizing nodes" + "<br>");
		buf.append("<b>Acceptable Values:</b> Values entered must have proper escape character if necessary" + "</html>");
		cmbDelimiterAddition.setToolTipText(buf.toString());
		
		addDelimiterButton = new JButton();
		addDelimiterButton.setText("Add");
//		addDelimiterButton.setEnabled(false);
//		addDelimiterButton.addActionListener(this);
		
		//Word panel
		JPanel wordPanel = new JPanel();
		wordPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5,0,0,0);
		wordPanel.add(addDelimiterLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5,10,0,10);
		wordPanel.add(cmbDelimiterAddition, gridBagConstraints);
		
		//Button stuff
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(5,0,0,0);
		wordPanel.add(addDelimiterButton, gridBagConstraints);
		
		
		// Word Removal Label
		JLabel removeDelimiterLabel = new JLabel("Remove Delimiter");
		
		//Word Removal Combo Box
		wscbm = new WidestStringComboBoxModel();
		cmbDelimiterRemoval = new JComboBox(wscbm);
		cmbDelimiterRemoval.addPopupMenuListener(new WidestStringComboBoxPopupMenuListener());
		cmbDelimiterRemoval.setEditable(false);
	    d = cmbDelimiterRemoval.getPreferredSize();
	    cmbDelimiterRemoval.setPreferredSize(new Dimension(15, d.height));
//	    cmbDelimiterRemoval.addItemListener(this);
	    cmbDelimiterRemoval.setToolTipText("Allows for selection of a delimiter to remove from the list used when tokenizing");

	    //Word Removal Button
	    removeDelimiterButton = new JButton();
	    removeDelimiterButton.setText("Remove");
//	    removeDelimiterButton.setEnabled(false);
//	    removeDelimiterButton.addActionListener(this);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5,0,0,0);
		wordPanel.add(removeDelimiterLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 10, 0, 10);
		wordPanel.add(cmbDelimiterRemoval, gridBagConstraints);
		
		//Button stuff
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
		wordPanel.add(removeDelimiterButton, gridBagConstraints);

//		updateDelimiterCMBs();
		
		//Add components to main panel
		panel.add(wordPanel);
		
		//Add to collapsible panel
		collapsiblePanel.getContentPane().add(panel, BorderLayout.NORTH);
		
		return collapsiblePanel;
	}
	
	/**
	 * Creates a CollapsiblePanel that holds the word stemming information.
	 * @return CollapsiblePanel - word stemming panel interface.
	 */
	private CollapsiblePanel createStemmingPanel()
	{
		CollapsiblePanel collapsiblePanel = new CollapsiblePanel("Word Stemming");
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		
		StringBuffer buf = new StringBuffer();
		
		
		//Word panel
		JPanel wordPanel = new JPanel();
		//wordPanel.setLayout(new GridBagLayout());
		wordPanel.setLayout(new BorderLayout());
		
		//Create Checkbox
		stemmer = new JCheckBox("Enable Stemming");
		
		buf = new StringBuffer();
		buf.append("<html>" + "Causes all words to be stemmed using the Porter Stemmer algorithm." + "<br>");
		buf.append("<b>Notice:</b> This will allow words with a similar stem to map to the same word." + "<br>");
		buf.append("However, words stems may not be what you expect." + "</html>");
		stemmer.setToolTipText(buf.toString());
//		stemmer.addActionListener(this);
		stemmer.setSelected(false);
//		stemmer.setEnabled(false);
		
		//GridBagConstraints gridBagConstraints = new GridBagConstraints();
		//gridBagConstraints.gridx = 0;
		//gridBagConstraints.gridy = 0;
		//gridBagConstraints.gridwidth = 1;
		//gridBagConstraints.anchor = GridBagConstraints.WEST;
		//gridBagConstraints.insets = new Insets(5,0,0,0);
		wordPanel.add(stemmer, BorderLayout.WEST);
		
		//Add components to main panel
		panel.add(wordPanel);
		
		//Add to collapsible panel
		collapsiblePanel.getContentPane().add(panel, BorderLayout.NORTH);
		
		return collapsiblePanel;
	}
	
	
	/**
	 * Creates a CollapsiblePanel that holds the Cloud Layout information.
	 * @return CollapsiblePanel - cloud Layout panel interface.
	 */
	private CollapsiblePanel createCloudLayoutPanel()
	{
		CollapsiblePanel collapsiblePanel = new CollapsiblePanel("Layout");
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		
		
		JPanel cloudLayoutPanel = new JPanel();
		cloudLayoutPanel.setLayout(new GridBagLayout());

		JLabel cloudStyleLabel = new JLabel("Cloud Style: ");
		
		WidestStringComboBoxModel wscbm = new WidestStringComboBoxModel();
		cmbStyle = new JComboBox(wscbm);
		cmbStyle.addPopupMenuListener(new WidestStringComboBoxPopupMenuListener());
		cmbStyle.setEditable(false);
	    Dimension d = cmbStyle.getPreferredSize();
	    cmbStyle.setPreferredSize(new Dimension(15, d.height));

	    StringBuffer toolTip = new StringBuffer();
	    toolTip.append("<html>" + "--Visual style for the cloud layout--" +"<br>");
	    toolTip.append("<b>Clustered:</b> If a style with clustering is selected, then the cloud will be comprised of groups of words." + "<br>");
	    toolTip.append("Each cluster is build by analyzing which words appear next to each other and what order they appear." + "<br>");
	    toolTip.append("<b> No Clustering:</b> When a non-clustering option is selected, words appear in decreasing order of of size.");
	    cmbStyle.setToolTipText(toolTip.toString());

		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5,0,0,0);
		cloudLayoutPanel.add(cloudStyleLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 10, 0, 0);
		cloudLayoutPanel.add(cmbStyle, gridBagConstraints);
	    
//	    buildStyleCMB();
		
		panel.add(cloudLayoutPanel);
		
		//Create network button stuff
		JLabel createNetworkLabel = new JLabel("Network View:");
		
		createNetworkButton = new JButton("Export Cloud to Network");
//		createNetworkButton.setEnabled(false);
		createNetworkButton.setToolTipText("Creates a new network based on the current cloud");
//		createNetworkButton.addActionListener(new CreateCloudNetworkAction());
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5, 0, 0, 0);
		cloudLayoutPanel.add(createNetworkLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 10, 0, 0);
		cloudLayoutPanel.add(createNetworkButton, gridBagConstraints);
		
		//Save file to .jpg stuff
		/*
		JLabel saveCloudLabel = new JLabel("Save Cloud Image:");
		
		saveCloudButton = new JButton("Export Cloud to File");
		saveCloudButton.setEnabled(false);
		saveCloudButton.setToolTipText("Saves the current cloud as an image file");
		saveCloudButton.addActionListener(new SaveCloudAction());
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5, 0, 0, 0);
		cloudLayoutPanel.add(saveCloudLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 10, 0, 0);
		cloudLayoutPanel.add(saveCloudButton, gridBagConstraints);
		*/
		
		collapsiblePanel.getContentPane().add(panel,BorderLayout.NORTH);
		return collapsiblePanel;
	}
	
	
	/**
	 * Utility to create a panel for the buttons at the bottom of the Semantic 
	 * Summary Input Panel.
	 */
	private JPanel createBottomPanel()
	{
		
		JPanel panel = new JPanel();
		panel.setLayout(new ModifiedFlowLayout());
		
		//Create buttons
		JButton deleteButton = new JButton("Delete");
		JButton updateButton = new JButton("Update");
		JButton createButton = new JButton("Create");
	
		saveCloudButton = new JButton("Save Image");
//		saveCloudButton.setEnabled(false);
		saveCloudButton.setToolTipText("Saves the current cloud as an image file");
		
		//Add actions to buttons
/*
		createButton.addActionListener(new CreateCloudAction());
		deleteButton.addActionListener(new DeleteCloudAction());
		updateButton.addActionListener(new UpdateCloudAction());
		saveCloudButton.addActionListener(new SaveCloudAction());
*/		
		//Add buttons to panel
		panel.add(deleteButton);
		panel.add(updateButton);
		panel.add(createButton);
		panel.add(saveCloudButton);
		
		return panel;
	}
	
	/**
	 * Private Class to ensure that text fields are being set properly
	 */
	private class FormattedTextFieldAction implements PropertyChangeListener
	{
		public void propertyChange(PropertyChangeEvent e)
		{
			JFormattedTextField source = (JFormattedTextField) e.getSource();
			
			String message = "The value you have entered is invalid. \n";
			boolean invalid = false;

			
			//Max Words
			if (source == maxWordsTextField)
			{
				Number value = (Number) maxWordsTextField.getValue();
				if ((value != null) && (value.intValue() >= 0))
				{
					//All is well - do nothing
				}
				else
				{
					Integer defaultMaxWords = wordCloudSettings.getMaxWordCount();
					maxWordsTextField.setValue(defaultMaxWords);
					message += "The maximum number of words to display must be greater than or equal to 0.";
					invalid = true;
				}
			}// end max Words
			
			else if (source == clusterCutoffTextField)
			{
				Number value = (Number) clusterCutoffTextField.getValue();
				if ((value != null) && (value.doubleValue() >= 0.0))
				{
					//All is well - leave it be
				}
				else
				{
					Double defaultClusterCutoff = 1.0;
					clusterCutoffTextField.setValue(defaultClusterCutoff);
					message += "The cluster cutoff must be greater than or equal to 0";
					invalid = true;
				}
			}
			
			else if (source == addWordTextField)
			{
				String value = (String)addWordTextField.getText();
				if (value.equals("") || value.matches("[\\w]*"))
				{ 
					//All is well, leave it be
				}
				else
				{
					//addWordTextField.setValue("");
					//message += "You can only add a word that contains letters or numbers and no spaces";
					//invalid = true;
				}
			}
			
			if (invalid)
				JOptionPane.showMessageDialog(cySwingApplication.getJFrame(), message, "Parameter out of bounds", JOptionPane.WARNING_MESSAGE);
		}
	}
}
