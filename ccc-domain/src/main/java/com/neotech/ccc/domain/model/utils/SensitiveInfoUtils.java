package com.neotech.ccc.domain.model.utils;

public class SensitiveInfoUtils {

    public static final char HIDING_SYMBOL = '*';

    public static String hideLast4(String str) {
        return str == null || str.length() < 5
               ? str
               : replaceBetween(str, str.length() - 4, 0, HIDING_SYMBOL);
    }

    public static String replaceBetween(String str, int padLeft, int padRight, char replaceWith) {
        if (padLeft < 0 || padRight < 0) {
            return str;
        }
        if (str.length() <= padLeft + padRight) {
            return str;
        }
        var sb = new StringBuilder(str.substring(0, padLeft));
        for (int i = 0; i < str.length() - padLeft - padRight; i++) {
            sb.append(replaceWith);
        }
        return sb.append(str.substring(str.length() - padRight))
                 .toString();
    }

}
