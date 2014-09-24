package com.jivesoftware.android.mobile.sdk.util;

import java.util.List;

/**
 * Created by mark.schisler on 8/14/14.
 */
public class Joiner {
    public static class Builder {
        private String token;

        Builder(String token) {
            this.token = token;
        }

        public String join(List<String> list) {
            StringBuilder builder = new StringBuilder();
            for ( String string : list) {
                builder.append(string);
                builder.append(token);
            }
            if ( builder.length() > 0 && token.length() > 0 ) {
                builder.delete(builder.length() - token.length(), builder.length());
            }
            return builder.toString();
        }
        public void appendTo(StringBuilder destination, List<String> list) {
            destination.append(join(list));
        }
    }

    public static Builder on(String s) {
        return new Builder(s);
    }

}
