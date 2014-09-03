package com.aconex.vanity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * Primary class to do the matching based on the given phone number
 * and return the list of matching words.
 * 
 * @author James David
 *
 */
public class VanityFinder {

	String dictionaryFileName;
	Map<Integer, VanityData> vanity;

	public VanityFinder() throws Exception {
		init();
	}

	public VanityFinder(String dictionaryFileName) throws Exception {
		this.dictionaryFileName = dictionaryFileName;
		init();
	}

	private void init() throws Exception {
		vanity = VanityUtil.prepareDictionary(dictionaryFileName);
	}

	public String cleanupEachPhoneNum(String phoneNum) {
		phoneNum = phoneNum.replaceAll("['\",;: ]", "");
		return phoneNum;
	}
	/**
	 * This method takes the phone number and returns the matching words.
	 * 
	 * @param phoneNum
	 * @return
	 */
	public List<String> findMatch(String phoneNum) {
		phoneNum = cleanupEachPhoneNum(phoneNum);

		List<String> result = new ArrayList<String>();
		// basic validations of the input phone number
		if (!valid(phoneNum)) {
			return result;
		}
		if (phoneNum.indexOf('.') != -1) {
			// do the spl formatting and permutations
			doFindMatchWithSplChars(phoneNum, result);

		} else {
			// First try regular match
			if (!(phoneNum.startsWith("1") || phoneNum.startsWith("0"))) {
				result = _findMatch(phoneNum);
			}
			// Try leaving the first digit
			result = handleSingleDigitLeftoutPrefix(phoneNum, result);

			// Try leaving the last digit
			result = handleSingleDigitLeftoutSuffix(phoneNum, result);
		}
		return result;
	}
	/*
	 * Checks if the results are empty before proceeding with further 
	 * matching.  Tries to find match by omitting the first digit.
	 */
	private List<String> handleSingleDigitLeftoutSuffix(String phoneNum,
			List<String> result) {
		if (result == null || result.size() == 0) {
			result = _findMatch(phoneNum.substring(0, (phoneNum.length() - 1)));

			if (result != null && result.size() > 0) {
				result = suffix(phoneNum.charAt(phoneNum.length() - 1) + "",
						result);
			}
		}
		return result;
	}
	/*
	 * Checks if the results are empty before proceeding with further 
	 * matching.  Tries to find match by omitting the last digit.
	 * 
	 */
	private List<String> handleSingleDigitLeftoutPrefix(String phoneNum,
			List<String> result) {

		if (result == null || result.size() == 0) {
			result = _findMatch(phoneNum.substring(1));

			if (result != null && result.size() > 0) {
				result = prefix(phoneNum.charAt(0) + "", result);
			}
		}
		return result;
	}
	
	/*
	 * For cases were more than one word needs to be framed for a 
	 * given phone number, this method does the additional processing
	 * and formating.  Also, this method returns the entire permutation
	 * combination such that the output can be printed line by line.
	 *  
	 */
	private void doFindMatchWithSplChars(String phoneNum, List result) {

		String split[] = phoneNum.split("[.]");

		boolean singleDigitRuleApplied = false;
		List<List> sublists = new ArrayList<List>();
		for (String each : split) {
			if (each.length() > 1) {//dont try to match a word for a single digit
				List<String> match = _findMatch(each);
				if (match != null && match.size() > 0) {
					sublists.add(match);
				} else {
					sublists = null;
					break;
				}
			} else if (!singleDigitRuleApplied) {//just add the single digit directly to the output
				List tmp = new ArrayList();
				tmp.add(each);
				sublists.add(tmp);
				singleDigitRuleApplied = true;
			} else if (singleDigitRuleApplied) {//do not allow more than one single digit, just skip and dont match
				sublists = null;
				break;
			}
		}
		if (sublists != null && sublists.size() > 0) {
			doPermutation(0, sublists, "", result);//prepare for output by combining the various output of sublists.
		}

	}

	/*
	 * Basic validation method to check if the phone number given in the input
	 * does not contain 0 or 1 in middle of the phone number.
	 * 
	 */
	private boolean valid(String phoneNum) {
		if (phoneNum.substring(1, phoneNum.length() - 1).indexOf('0') != -1) {
			return false;
		} else if (phoneNum.substring(1, phoneNum.length() - 1).indexOf('1') != -1) {
			return false;
		}
		return true;
	}

	/*
	 * 
	 * Recursive method to capture all permutation combination of the matches found.
	 * 
	 * @param index the index on the sub list
	 * @param sublist the list of words for each . delimited numbers
	 * @param prefix concatenated string from the parent
	 * @param result result object where the final string is added for output
	 */
	private void doPermutation(int index, List sublist, String prefix,
			List<String> result) {

		if (index < (sublist.size() - 1)) {
			//More sub lists are found, not yet ready for adding to result, recurse.
			List sub = (List) sublist.get(index);
			Iterator iter = sub.iterator();
			while (iter.hasNext()) {
				String each = (String) iter.next();
				String newprefix = "";
				if (prefix.length() > 0) {
					newprefix = prefix + " - " + each;
				} else {
					newprefix = each;
				}
				doPermutation((index + 1), sublist, newprefix, result);
			}
		} else {
			//No more sub list, add the concatenated string to result.
			List sub = (List) sublist.get(index);
			Iterator iter = sub.iterator();
			while (iter.hasNext()) {
				String each = (String) iter.next();
				String newprefix = "";
				if (prefix.length() > 0) {
					newprefix = prefix + " - " + each;
				} else {
					newprefix = each;
				}
				result.add(newprefix);
			}
		}
	}
	/*
	 * for all the output words, suffix the number directly which is not
	 * converted to words.
	 * 
	 */
	private List<String> suffix(String suff, List<String> result) {
		List<String> al = new ArrayList<String>();

		Iterator<String> iter = result.iterator();
		while (iter.hasNext()) {
			al.add(iter.next() + " " + suff);
		}
		return al;
	}
	/*
	 * for all the output words, prefix the number directly which is not
	 * converted to words.
	 */
	private List<String> prefix(String pref, List<String> result) {

		List<String> al = new ArrayList<String>();

		Iterator<String> iter = result.iterator();
		while (iter.hasNext()) {
			al.add(pref + " " + iter.next());
		}
		return al;
	}
	/*
	 * Helper method to get the matching words from the dictionary
	 * data structure.
	 */
	private List<String> _findMatch(String phoneNum) {
		int ilen = phoneNum.length();
		Map<Integer, VanityData> vanityPerString = vanity;
		VanityData data = null;
		for (int i = 0; i < ilen; i++) {
			if (vanityPerString == null) {
				continue;
			}
			data = vanityPerString
					.get(Integer.parseInt("" + phoneNum.charAt(i)));
			if (data != null) {
				vanityPerString = data.getVanityData();
			}
		}
		if (data != null) {
			return data.getWords();
		} else {
			return null;
		}

	}
}
