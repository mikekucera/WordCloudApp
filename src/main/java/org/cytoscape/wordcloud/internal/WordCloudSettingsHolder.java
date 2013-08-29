package org.cytoscape.wordcloud.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

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
	
	private Map<CyNetwork, Collection<String>> excludedColumnsMap = new HashMap<CyNetwork, Collection<String>>();
	
	private Collection<SettingsChangeListener> settingsChangeListeners = new HashSet<SettingsChangeListener>();
	
	private Set<String> excludedWordsLowercase = new HashSet<String>();
	
	private double normalizationCoefficient = 0.5;
	
	private boolean isUsingNormalization = false;
	
	private boolean isUsingStemming = true;
	
	private boolean isRemovingAllNumbers = false;
	
	// Minimum word count for word to appear
	private int minWordCount = 0;
	
	private int minWordCountSliderMax = 20;
	
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
	
	public Map<CyNetwork, Collection<String>> getExcludedColumnsMap() {
		return this.excludedColumnsMap;
	}
	
	public Set<String> getExcludedWordsLowercase() {
		return this.excludedWordsLowercase;
	}
	
	public double getNormalizationCoefficient() {
		return this.normalizationCoefficient;
	}
	
	public boolean isUsingNormalization() {
		return this.isUsingNormalization;
	}
	
	public boolean isUsingStemming() {
		return this.isUsingStemming;
	}
	
	public boolean isRemovingAllNumbers() {
		return this.isRemovingAllNumbers;
	}
	
	public int getMinWordCount() {
		return this.minWordCount;
	}
	
	public int getMinWordCountSliderMax() {
		return this.minWordCountSliderMax;
	}
	
	public void setMaxWordCount(int maxWordCount) {
		this.maxWordCount = maxWordCount;
		
		fireChangedEvent();
	}
	
	public void setMinWordCloudFontSize(int minWordCloudFontSize) {
		this.minWordCloudFontSize = minWordCloudFontSize;
		
		fireChangedEvent();
	}
	
	public void setMaxWordCloudFontSize(int maxWordCloudFontSize) {
		this.maxWordCloudFontSize = maxWordCloudFontSize;
		
		fireChangedEvent();
	}
	
	public void setWordTokenizer(WordTokenizer wordTokenizer) {
		this.wordTokenizer = wordTokenizer;
		
		fireChangedEvent();
	}
	
	public void setWordShortener(WordShortener wordShortener) {
		this.wordShortener = wordShortener;
		
		fireChangedEvent();
	}
	
	public void setExcludedColumnsMap(Map<CyNetwork, Collection<String>> excludedColumnsMap) {
		this.excludedColumnsMap = excludedColumnsMap;
		
		fireChangedEvent();
	}
	
	public void setNormalizationCoefficient(double normalizationCoefficient) {
		this.normalizationCoefficient = normalizationCoefficient;
		
		fireChangedEvent();
	}
	
	public void setUsingNormalization(boolean isUsingNormalization) {
		this.isUsingNormalization = isUsingNormalization;
		
		fireChangedEvent();
	}
	
	public void setUsingStemming(boolean isUsingStemming) {
		this.isUsingStemming = isUsingStemming;
		
		fireChangedEvent();
	}
	
	public void setRemoveAllNumbers(boolean isRemovingAllNumbers) {
		this.isRemovingAllNumbers = isRemovingAllNumbers;
		
		fireChangedEvent();
	}
	
	public void setExcludedWordsLowercase(Set<String> excludedWordsLowercase) {
		this.excludedWordsLowercase = excludedWordsLowercase;
		
		fireChangedEvent();
	}
	
	public void setMinWordCount(int wordCount) {
		this.minWordCount = wordCount;
		
		fireChangedEvent();
	}
	
	public void setMinWordCountSliderMax(int minWordCountSliderMax) {
		this.minWordCountSliderMax = minWordCountSliderMax;
		
		fireChangedEvent();
	}
	
	public void fireChangedEvent() {
		for (SettingsChangeListener settingsChangeListener : this.settingsChangeListeners) {
			settingsChangeListener.settingsChanged(this);
		}
	}
	
	public void addSettingsChangedListener(SettingsChangeListener settingsChangeListener) {
		this.settingsChangeListeners.add(settingsChangeListener);
	}
	
	public void removeSettingsChangedListener(SettingsChangeListener settingsChangeListener) {
		this.settingsChangeListeners.remove(settingsChangeListener);
	}
	
	public void removeAllSettingsChangedListeners() {
		this.settingsChangeListeners.clear();
	}
}
