package org.cytoscape.wordcloud.internal.util;

public class WordShortener {

	private int maximumWordLength = 15;
	
	public int getMaximumWordLength() {
		return this.maximumWordLength;
	}
	
	public void setMaximumWordLength(int maximumWordLength) {
		this.maximumWordLength = maximumWordLength;
	}
	
	public String shortenWord(String word) {
		if (word.length() <= this.maximumWordLength) {
			return word;
		} else {
			String trimmedWord = word.trim();
			
			// Shortening algorithm: Return the first (maximumWordLength - 6 characters,
			// followed by "...", followed by the last 3 characters.
			String shortenedWord = trimmedWord.substring(0, this.maximumWordLength - 6);
			shortenedWord = shortenedWord.concat("..." + trimmedWord.substring(trimmedWord.length() - 3));
			
			
			return shortenedWord;
		}
	}
}
