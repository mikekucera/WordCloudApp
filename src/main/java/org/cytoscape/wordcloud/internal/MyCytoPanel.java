package org.cytoscape.wordcloud.internal;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import javax.swing.JLabel;

public class MyCytoPanel extends JPanel implements CytoPanelComponent {
	
	
	private static final long serialVersionUID = 8292806967891823933L;

	public Component getComponent() {
		return this;
	}


	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.WEST;
	}


	public String getTitle() {
		return "MyPanel";
	}


	public Icon getIcon() {
		return null;
	}

}
