package com.ec.svg.generator.app.util;


public class StringUtils {

    public static final Integer UNICODE_FOR_EXC_KEY = Integer.valueOf(33);
    public static final String KEY_FOR_033_UNICODE = "exc";
    public static final String REGEX_PATH_UNICODE_ID = "(\\w+?)_";
    public static final String REGEX_PATH_SEQUENCE = "\\w+_\\w+_(\\w+)";

    public static Boolean hasText(String checkString) {
        return org.springframework.util.StringUtils.hasText(checkString);
    }

    public static String wrapQuotes(String value) {
        String result = "\"" + value + "\"";
        return result;
    }

    public static String unicodeKeyToChar(Integer unicodeKey, Boolean strict) {
        String result = String.valueOf((char)unicodeKey.intValue());

        if (!strict && unicodeKey.equals(UNICODE_FOR_EXC_KEY)) {
            result = KEY_FOR_033_UNICODE;
        }

        return result;
    }

    public static String unicodeKeyToChar(Integer unicodeKey) {
        return unicodeKeyToChar(unicodeKey,Boolean.FALSE);
    }
}
