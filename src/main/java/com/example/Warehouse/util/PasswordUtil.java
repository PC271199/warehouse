package com.example.Warehouse.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

public class PasswordUtil {
	private static final char[] ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private static final char[] ALPHA_LOWER = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	private static final char[] NUM = "0123456789".toCharArray();
	private static final char[] SPL_CHARS = "!@#$%^&*_=+-".toCharArray();

	private static final SecureRandom secureRandom;
	static {
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("Invalid Random Method");
		}
	}

	public static String generatePswd(int minLen, int maxLen, int noOfCAPSAlpha, int noOfDigits, int noOfSplChars) {
		if (minLen > maxLen)
			throw new IllegalArgumentException("Min. Length > Max. Length!");
		if ((noOfCAPSAlpha + noOfDigits + noOfSplChars) > minLen)
			throw new IllegalArgumentException(
					"Min. Length should be atleast sum of (CAPS, DIGITS, SPL CHARS) Length!");

		secureRandom.setSeed((new Date()).getTime());
		int len = secureRandom.nextInt(maxLen - minLen + 1) + minLen;

		StringBuilder newPassword = new StringBuilder();
		newPassword.append( getRandomString(noOfCAPSAlpha, ALPHA_CAPS) );
		newPassword.append( getRandomString(noOfDigits, NUM) );
		newPassword.append( getRandomString(noOfSplChars, SPL_CHARS) );
		newPassword.append( getRandomString((len - noOfCAPSAlpha - noOfDigits - noOfSplChars), ALPHA_LOWER) );

		return newPassword.toString();
	}
	public static StringBuilder getRandomString(int length, char[] characterSet) {
		StringBuilder randomString = new StringBuilder(length);
		for (int loop = 0; loop < length; loop++) {
			int index = secureRandom.nextInt(characterSet.length);
			randomString.append(characterSet[index]);
		}
		
		return randomString;
	}
	public static String execute_genpass() {
		return generatePswd(9,10,2,2,2);
	}
}
