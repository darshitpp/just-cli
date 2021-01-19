package dev.darshit.just.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class StringUtils {

    public static boolean isEmpty(String value) {
        return (value == null || "".equals(value) || "null".equalsIgnoreCase(value));
    }

    public static List<String> split(String value, String delimiter) {
        if (isEmpty(value) || isEmpty(delimiter)) {
            return new ArrayList<>();
        }
        return Arrays.asList(value.split(delimiter));
    }

    public static String replace(final String value, final String target, final String replacement) {
        if (isEmpty(value) || isEmpty(target) || replacement == null) {
            return value;
        }

        int endIndex = value.indexOf(target);
        if (endIndex == -1) {
            return value;
        }

        StringBuilder builder = new StringBuilder(value.length());
        int beginIndex = 0;
        int targetLength = target.length();
        while (endIndex >= 0) {
            builder.append(substring(value, beginIndex, endIndex));
            builder.append(replacement);
            beginIndex = endIndex + targetLength;
            endIndex = value.indexOf(target, beginIndex);
        }

        // characters from lastIndex as loop will break because of -1 endIndex value
        builder.append(substring(value, beginIndex));
        return builder.toString();
    }

    public static String delete(final String value, final String target) {
        return replace(value, target, "");
    }

    private static String substring(final String value, final int beginIndex) {
        return value.substring(beginIndex);
    }

    private static String substring(final String value, final int beginIndex, final int endIndex) {
        return value.substring(beginIndex, endIndex);
    }

}