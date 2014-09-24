package com.jivesoftware.android.mobile.sdk.util;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mark.schisler on 8/15/14.
 */
public class StringUtil {
    // ++ is a possessive one-or-many qualifier.
    // possessive quantifiers always eat the entire input string, trying once
    // (and only once) for a match. Unlike the greedy quantifiers, possessive
    // quantifiers never back off, even if doing so would allow the overall match to succeed.
    // http://docs.oracle.com/javase/tutorial/essential/regex/quant.html
    private static final Pattern WHITESPACE_ONLY_PATTERN = Pattern.compile("^\\s++$");

    public static boolean isNullOrEmptyOrWhitespace(@Nullable CharSequence c) {
        if (c == null) {
            return true;
        } else if (c.length() == 0) {
            return true;
        } else {
            Matcher whitespaceOnlyMatcher = WHITESPACE_ONLY_PATTERN.matcher(c);
            boolean whitespaceOnly = whitespaceOnlyMatcher.matches();
            return whitespaceOnly;
        }
    }
}
