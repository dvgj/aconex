package com.aconex.vanity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Primary class to read data from input files viz dictionary and phone numbers,
 * and load the vanity finder to find probable matching words.
 * 
 * if the phone numbers file is not provided, accept input from the console and
 * display output.
 * 
 * @author James David
 * 
 */

public class App {
	public static void main(String[] args) throws Exception {

		print("Usage : App [phonenumbers file name] [-Ddictionary=dictionary file name]");

		String dictionaryFileName = System.getProperty("dictionary");

		VanityFinder vf = null;
		try {
			vf = new VanityFinder(dictionaryFileName);
		} catch (FileNotFoundException fe) {
			System.err
					.println("Unable to instantiate VanityFinder, incorrect dictionary file");
		} catch (IOException e) {
			System.err
					.println("Unable to instantiate VanityFinder, unable to load dictionary file");
		} catch (Exception e) {
			System.err
					.println("Unable to instantiate VanityFinder, internal error");
		}

		if (args != null && args.length > 0) {
			String phoneNumbersFileName = args[0];
			try {
				readPhoneNumbersAndPrint(phoneNumbersFileName, vf);
			} catch (FileNotFoundException fe) {
				System.err.println("Unable to load phone numbers file");
			}
		} else {
			inputPhoneNumbersAndPrint(vf);
		}

	}

	/*
	 * read the phone numbers from console and output the matching vanity words.
	 */
	private static void inputPhoneNumbersAndPrint(VanityFinder vf) {
		String line = null;
		Scanner sc = new Scanner(System.in);
		do {
			print("Please enter the phone number to display possible vanity names. (Type 'exit' to quit)");
			line = sc.nextLine();
			if (!"exit".equals(line) && line.trim().length() > 0) {
				try {
					List<String> al = vf.findMatch(line);
					if (al != null && al.size() > 0) {
						print(al);
					}
				} catch (NumberFormatException ne) {
					System.err
							.println("Invalid input, please provide a numeric with no special characters viz. '\",; :");
				}
			}
		} while (!"exit".equals(line));
		sc.close();

	}

	/*
	 * read the phone numbers from a file and display matching output.
	 */
	private static void readPhoneNumbersAndPrint(String phoneNumbersFileName,
			VanityFinder vf) throws Exception {
		String line;
		FileInputStream fis = new FileInputStream(phoneNumbersFileName);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		line = br.readLine();
		while (line != null) {
			if (line.trim().length() > 0) {
				try {
					List<String> al = vf.findMatch(line);
					System.out.println(line);
					if (al != null && al.size() > 0) {
						print(al);
					}
				} catch (NumberFormatException ne) {
					// Ignore invalid data during file processing.
				}
			}
			line = br.readLine();
		}
		fis.close();

	}

	public static void print(String msg) {
		System.out.println(msg);
	}

	public static void print(List al) {
		Iterator iter = al.iterator();

		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
	}
}
