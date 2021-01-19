package dev.darshit.just.utils;

import org.apache.commons.validator.routines.UrlValidator;

import java.util.regex.Pattern;

public class Validator {

    private static final UrlValidator URL_VALIDATOR = new UrlValidator(new String[]{"http", "https"});

    private static final Pattern INVALID_STRING_VALIDATOR = Pattern.compile("[\\w\\d\\-_]+");

    public static boolean validateUrl(String url) {
        return URL_VALIDATOR.isValid(url);
    }

    public static boolean validateCustomPath(String customPath) {
        return INVALID_STRING_VALIDATOR.matcher(customPath).matches();
    }
}
