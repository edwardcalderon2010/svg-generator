package com.ec.svg.generator.app.util;


public class StringUtils {

    public static final String REGEX_PATH_UNICODE_ID = "(\\w+?)_";
    public static final String REGEX_PATH_SEQUENCE = "\\w+_\\w+_(\\w+)";

    public static Boolean hasText(String checkString) {
        return org.springframework.util.StringUtils.hasText(checkString);
    }

    public static String wrapQuotes(String value) {
        String result = "\"" + value + "\"";
        return result;
    }

}
