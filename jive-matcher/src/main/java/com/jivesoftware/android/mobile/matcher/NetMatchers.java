package com.jivesoftware.android.mobile.matcher;

import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URI;
import java.net.URL;

public class NetMatchers {
    @Nonnull
    public static Matcher<URI> uri(@Nonnull String spec) {
        return new PropertyMatcher<String, URI>("spec", spec) {
            @Nonnull
            @Override
            protected String getPropertyValue(@Nonnull URI item) throws Exception {
                return item.toString();
            }
        };
    }

    @Nonnull
    public static Matcher<URL> url(@Nonnull String spec) {
        return new PropertyMatcher<String, URL>("spec", spec) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull URL item) throws Exception {
                return item.toExternalForm();
            }
        };
    }
}
