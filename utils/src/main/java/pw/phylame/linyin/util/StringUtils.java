/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtils {
    private static final MessageDigest MD5;

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Not found MD5 module", e);
        }
    }

    public static final String EMPTY = "";

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        if (isEmpty(cs)) {
            return true;
        }
        int end = cs.length();
        for (int i = 0; i < end; ++i) {
            if (Character.isWhitespace(cs.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static String trim(String str) {
        return isEmpty(str) ? str : str.trim();
    }

    public static <T extends CharSequence> T notEmptyOr(T origin, T placeholder) {
        return isNotEmpty(origin) ? origin : placeholder;
    }

    public static String md5OfBytes(byte[] bytes) {
        MD5.update(bytes);
        return new BigInteger(1, MD5.digest()).toString(16);
    }

    public static String md5OfString(String str) {
        return md5OfBytes(str.getBytes());
    }

    public static String md5OfBuffer(ByteBuffer buffer) {
        MD5.update(buffer);
        return new BigInteger(1, MD5.digest()).toString(16);
    }
}