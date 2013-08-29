package org.cytoscape.wordcloud.internal.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import org.cytoscape.wordcloud.internal.WordCloudSettingsHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tokenizes a string into words, removing words like "and", "but"
 */
public class WordTokenizer {
	
	private static final Logger logger = LoggerFactory.getLogger(WordTokenizer.class);
	
	private static final String TOKENIZATION_REGEX = "[\\s\\.,\\t]";
	
	private Collection<String> ignoredWords;
	
	public WordTokenizer() {
		this.ignoredWords = new LinkedList<String>();
		
		// loadIgnoredWords("/tokenizer/ignoredWords.txt");
	}
	
	public Collection<String> tokenize(WordCloudSettingsHolder wordCloudSettingsHolder, String text) {
		
		Collection<String> words = new LinkedList<String>();
		
		String[] split = text.split(TOKENIZATION_REGEX);
		
		for (int i = 0; i < split.length; i++) {
			
			// Only take non-empty strings
			if (split[i].length() > 0) {
				
				words.add(split[i]);
				
			}
		}
		
		return words;
	}
	
	private String generateTokenizationRegex(Set<String> delimiters) {
		return null;
	}
	
	/*
	public void loadIgnoredWords(String resourcePath) {
		StringBuilder text = new StringBuilder();
		
		InputStream inputStream;
		inputStream = WordTokenizer.class.getResourceAsStream(resourcePath);
		
		try {
			while (inputStream.available() > 0) {
				char readChar = (char) inputStream.read();
				
				text.append(readChar);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			logger.warn("Failed to load list of ignored words for word tokenization");
		}
		
		String ignoredWordsList = text.toString();
		String[] ignoredWordsSplit = ignoredWordsList.split("\n");
		
		for (int i = 0; i < ignoredWordsSplit.length; i++) {
			if (ignoredWordsSplit[i].length() > 0) {
				this.ignoredWords.add(ignoredWordsSplit[i]);
			}
		}
	}
	*/
}
