package org.cytoscape.wordcloud.internal;

/**
 * Toggle used to tell the RowSetListener to ignore events for a temporary duration.
 * 
 * This is used for cases where the word cloud application needs to make a node selection on the
 * graph and not have the node selection listener be affected.
 */
public class RowSetListenerToggle {
	
	public static RowSetListenerToggle instance = null;
	
	public static RowSetListenerToggle getInstance() {
		return instance;
	}
	
	/**
	 * Creates a new instance
	 * @return <code>null</code> if there was already an instance, or the newly created instance otherwise
	 */
	public static RowSetListenerToggle createInstance() {
		if (RowSetListenerToggle.instance == null) {
			RowSetListenerToggle.instance = new RowSetListenerToggle();
			
			return RowSetListenerToggle.instance;
		} else {
			return null;
		}
	}
	
	/**
	 *  The maximum duration for a single episode of not listening, in milliseconds
	 */
	private static long MAX_SINGLE_STOP_LISTENING_TIME_MILLIS = 3 * 1000;

	private long stopListeningTime;
	private long stopListeningDurationMillis;
	
	private RowSetListenerToggle() {
		RowSetListenerToggle.instance = this;
	}
	
	public boolean isListening() {
		long nowTime = System.nanoTime();
		
		long elapsedTime = nowTime - this.stopListeningTime;
		
		if (elapsedTime > stopListeningDurationMillis * 1000 * 1000) {
			return true;
		} else {
			if (elapsedTime > MAX_SINGLE_STOP_LISTENING_TIME_MILLIS  * 1000 * 1000) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * 
	 * @param durationMillis
	 */
	public void stopListening(long durationMillis) {
		this.stopListeningTime = System.nanoTime();
		this.stopListeningDurationMillis = durationMillis;
	}
	
}
