package com.jivesoftware.android.mobile.httpclient.matcher;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import org.apache.http.cookie.Cookie;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class HttpClientCookieMatchers {
    @Nonnull
    public static Matcher<Cookie> cookieName(@Nullable String name) {
        return new PropertyMatcher<String, Cookie>("name", name) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull Cookie item) {
                return item.getName();
            }
        };
    }

    @Nonnull
    public static Matcher<Cookie> cookieValue(@Nullable String value) {
        return new PropertyMatcher<String, Cookie>("value", value) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull Cookie item) {
                return item.getValue();
            }
        };
    }

    @Nonnull
    public static Matcher<Cookie> cookiePath(@Nullable String path) {
        return new PropertyMatcher<String, Cookie>("path", path) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull Cookie item) {
                return item.getPath();
            }
        };
    }

    @Nonnull
    public static Matcher<Cookie> cookieDomain(@Nullable String domain) {
        return new PropertyMatcher<String, Cookie>("domain", domain) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull Cookie item) {
                return item.getDomain();
            }
        };
    }

    @Nonnull
    public static Matcher<Cookie> cookieVersion(int version) {
        return new PropertyMatcher<Integer, Cookie>("domain", version) {
            @Nullable
            @Override
            protected Integer getPropertyValue(@Nonnull Cookie item) {
                return item.getVersion();
            }
        };
    }

    @Nonnull
    public static Matcher<Cookie> cookiePorts(@Nullable int... ports) {
        List<Integer> portsList;
        if (ports == null) {
            portsList = null;
        } else {
            portsList = new ArrayList<Integer>(ports.length);
            for (int i=0; i < ports.length; i++) {
                portsList.add(ports[i]);
            }
        }
        return new PropertyMatcher<List<Integer>, Cookie>("ports", portsList) {
            @Nullable
            @Override
            protected List<Integer> getPropertyValue(@Nonnull Cookie item) {
                int[] ports = item.getPorts();
                List<Integer> portsList;
                if (ports == null) {
                    portsList = null;
                } else {
                    portsList = new ArrayList<Integer>(ports.length);
                    for (int i=0; i < ports.length; i++) {
                        portsList.add(ports[i]);
                    }
                }
                return portsList;
            }
        };
    }

    @Nonnull
    public static Matcher<Cookie> cookieIsSecure(boolean secure) {
        return new PropertyMatcher<Boolean, Cookie>("isSecure", secure) {
            @Nonnull
            @Override
            protected Boolean getPropertyValue(@Nonnull Cookie item) {
                return item.isSecure();
            }
        };
    }
}
