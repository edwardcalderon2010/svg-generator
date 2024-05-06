package com.ec.svg.generator.app.util;


public class StringUtils {

    public static Boolean hasText(String checkString) {
        return org.springframework.util.StringUtils.hasText(checkString);
    }

    public static String wrapQuotes(String value) {
        String result = "\"" + value + "\"";
        return result;
    }
}
