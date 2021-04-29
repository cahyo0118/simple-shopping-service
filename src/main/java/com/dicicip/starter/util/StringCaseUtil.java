package com.dicicip.starter.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringCaseUtil {

    private static final String WORD_SEPARATOR = " ";

    public static String convertToTitleCaseSplitting(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        return Arrays
                .stream(text.split(WORD_SEPARATOR))
                .map(word -> word.isEmpty() ? word : Character.toTitleCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(WORD_SEPARATOR));
    }

    public static String snakeToTitleCase(String text) {
        text = text.replace("_", " ");
        return text;
    }

}
