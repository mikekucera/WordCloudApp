package org.cytoscape.wordcloud.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.wordcloud.internal.util.WordShortener;
import org.cytoscape.wordcloud.internal.util.WordTokenizer;

public class WordCloudSettingsHolder {
	
	// Max words
	private int maxWordCount = 50;
	
	// Word Cloud font size
	private int minWordCloudFontSize = 12;
	private int maxWordCloudFontSize = 25;
	
	// Word Cloud string splitter
	private WordTokenizer wordTokenizer = new WordTokenizer();
	
	// Word Cloud word shortener
	private WordShortener wordShortener = new WordShortener();
	
	// Map from network to collection of column names
	private Map<CyNetwork, Collection<String>> excludedColumnsMap = new HashMap<CyNetwork, Collection<String>>();
	
	public int getMaxWordCount() {
		return this.maxWordCount;
	}
	
	public int getMinWordCloudFontSize() {
		return this.minWordCloudFontSize;
	}
	
	public int getMaxWordCloudFontSize() {
		return this.maxWordCloudFontSize;
	}
	
	public WordTokenizer getWordTokenizer() {
		return this.wordTokenizer;
	}
	
	public WordShortener getWordShortener() {
		return this.wordShortener;
	}
	
	public Map<CyNetwork, Collection<String>> getExcludedColumnsMap() {
		return this.excludedColumnsMap;
	}
	
	public void setMaxWordCount(int maxWordCount) {
		this.maxWordCount = maxWordCount;
	}
	
	public void setMinWordCloudFontSize(int minWordCloudFontSize) {
		this.minWordCloudFontSize = minWordCloudFontSize;
	}
	
	public void setMaxWordCloudFontSize(int maxWordCloudFontSize) {
		this.maxWordCloudFontSize = maxWordCloudFontSize;
	}
	
	public void setWordTokenizer(WordTokenizer wordTokenizer) {
		this.wordTokenizer = wordTokenizer;
	}
	
	public void setWordShortener(WordShortener wordShortener) {
		this.wordShortener = wordShortener;
	}
}
