package org.cytoscape.wordcloud.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
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
	
	// List of excluded column names
	private Collection<String> excludedColumns = new LinkedList<String>();
	
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
	
	public Collection<String> getExcludedColumns() {
		return this.excludedColumns;
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
