package com.aconex.vanity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Utility class for few static data, dictionary and data structure initialization and few 
 * utility methods.
 * 
 * @author James David
 *
 */

public class VanityUtil {
	static int numpad[] = new int[92];//Just the last 26 elements are used.
	//mapping the alphabets with numeric keypad
	static {
		numpad['A'] = 2;
		numpad['B'] = 2;
		numpad['C'] = 2;
		numpad['D'] = 3;
		numpad['E'] = 3;
		numpad['F'] = 3;
		numpad['G'] = 4;
		numpad['H'] = 4;
		numpad['I'] = 4;
		numpad['J'] = 5;
		numpad['K'] = 5;
		numpad['L'] = 5;
		numpad['M'] = 6;
		numpad['N'] = 6;
		numpad['O'] = 6;
		numpad['P'] = 7;
		numpad['Q'] = 7;
		numpad['R'] = 7;
		numpad['S'] = 7;
		numpad['T'] = 8;
		numpad['U'] = 8;
		numpad['V'] = 8;
		numpad['W'] = 9;
		numpad['X'] = 9;
		numpad['Y'] = 9;
		numpad['Z'] = 9;
	}
	/**
	 * load the dictionary data into a convenient map data structure for
	 * quick read based on the given phone number.
	 * 
	 * @param dictionaryFileName the name of the dictionary file along with path.
	 * @return a map data structure with single characer as key, and matching strings as value.
	 *         Additionally, the value component contains the sub map, a recursive data structure
	 *         to hold any length of dictionary word to numeric keypad mapping. 
	 * @throws Exception
	 */
	public static Map prepareDictionary(String dictionaryFileName) throws Exception {
		Map vanity = null;
		InputStream is = getDictionaryAsStream(dictionaryFileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = br.readLine();

		vanity = new HashMap<Integer, VanityData>();
		while (line != null) {
			String each = cleanupEachWord(line);
			int ilen = each.length();
			if (ilen < 2) {
				continue; // ignore single characters
			}
			VanityData data = null;
			Map<Integer, VanityData> vanityPerString = vanity;
			
			for (int i = 0; i < ilen; i++) {
				data = vanityPerString.get(numpad[each.charAt(i)]);
				if (data == null) {
					//initialize the sub map
					data = new VanityData();
					data.setWords(new ArrayList<String>());
					data.setVanityData(new HashMap<Integer, VanityData>());

					vanityPerString.put(numpad[each.charAt(i)], data);
				}

				vanityPerString = data.getVanityData();//reset to the inner map as we traverse the digits of phone number

			}
			data.getWords().add(each);//add the current word to the list
			line = br.readLine();
		}
		is.close();
		return vanity;
	}

	private static InputStream getDictionaryAsStream(String dictionaryFileName) throws Exception {
		InputStream is = null;
		if (dictionaryFileName == null) {
			is = VanityFinder.class.getResourceAsStream("/dictionary.txt");
			System.out.println("Default system dictionary loaded");
		} else {
			is = new FileInputStream(dictionaryFileName);
			System.out.println("Dictionary loaded from path : "
					+ dictionaryFileName);
		}

		return is;
	}

	public static String cleanupEachWord(String line) {
		line = line.trim().toUpperCase();
		line = line.replaceAll("['\",;: .]", "");
		return line;
	}
}
