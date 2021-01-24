package dev.darshit.just.utils;

public final class StringUtils {

    public static boolean isEmpty(String value) {
        return (value == null || "".equals(value) || "null".equalsIgnoreCase(value));
    }

}