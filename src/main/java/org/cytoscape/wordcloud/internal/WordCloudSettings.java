package org.cytoscape.wordcloud.internal;

public class WordCloudSettings {
	
	private int minWordCloudFontSize = 10;

	private int maxWordCloudFontSize = 20;
	
	public int getMinWordCloudFontSize() {
		return minWordCloudFontSize;
	}
	
	public int getMaxWordCloudFontSize() {
		return maxWordCloudFontSize;
	}
	
	public void setMinWordCloudFontSize(int minWordCloudFontSize) {
		this.minWordCloudFontSize = minWordCloudFontSize;
	}
	
	public void setMaxWordCloudFontSize(int maxWordCloudFontSize) {
		this.maxWordCloudFontSize = maxWordCloudFontSize;
	}
	
}
