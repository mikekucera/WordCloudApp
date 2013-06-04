package org.cytoscape.wordcloud.internal;

import java.awt.event.ActionEvent;

import org.cytoscape.application.swing.AbstractCyAction;
import org.w3c.dom.views.AbstractView;

public class ShowWordCloudAction extends AbstractCyAction {

	public ShowWordCloudAction() {
		super("Show Word Cloud");
		setPreferredMenu("Apps.WordCloud");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("Word Cloud clicked");
	}
}
