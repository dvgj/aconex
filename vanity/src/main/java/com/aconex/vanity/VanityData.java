package com.aconex.vanity;

import java.util.List;
import java.util.Map;

/**
 * Place holder class for storing the words and the sub map for recursive
 * traversing of phone numbers.
 * 
 * @author James David
 *
 */
public class VanityData {
	List<String> words;
	Map<Integer, VanityData> vanityData;

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

	public Map<Integer, VanityData> getVanityData() {
		return vanityData;
	}

	public void setVanityData(Map<Integer, VanityData> vanityData) {
		this.vanityData = vanityData;
	}
}
