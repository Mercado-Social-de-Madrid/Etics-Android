package com.triskelapps.loginview;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by julio on 27/08/15.
 */
public class DigestUtils {

    public static String hashString(String string, String algorithm) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.reset();
        digest.update(string.getBytes());
        byte[] bytes = digest.digest();
        return convertToHex(bytes);

    }

    public static String convertToHex(byte[] input) {

        int len = input.length;
        StringBuilder sb = new StringBuilder(len << 1);
        for (int i = 0; i < len; i++) {
            sb.append(Character.forDigit((input[i] & 0xf0) >> 4, 16));
            sb.append(Character.forDigit(input[i] & 0x0f, 16));
        }

        return sb.toString();
    }

}
