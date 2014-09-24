package com.jivesoftware.android.httpclient.util;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mark.schisler on 4/2/14.
 */
public class CookieUtil {
    // Response header is the header as specified in http://tools.ietf.org/html/rfc2109#section-4.3.4
    // In other words, whatever follows "Cookie":
    @Nullable
    public String getRequestHeaderValue(@Nullable List<Cookie> cookies, @Nonnull CookieSpec cookieSpec) {
        if (cookies == null) return null;
        StringBuilder stringBuilder = new StringBuilder();
        for (Cookie cookie : cookies) {
            List<Header> cookieHeaders = cookieSpec.formatCookies(Collections.singletonList(cookie));
            Header cookieHeader = cookieHeaders.get(0);
            String cookieHeaderValue = cookieHeader.getValue();
            stringBuilder.append(cookieHeaderValue);
            stringBuilder.append(";");
        }
        if (stringBuilder.length() > 0) {
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        } else {
            return null;
        }
    }

    @Nonnull
    public List<Cookie> getCookiesForURI(@Nullable List<Cookie> cookies, @Nonnull CookieSpec cookieSpec, @Nonnull URI uri) {
        CookieOrigin cookieOrigin = getCookieOrigin(uri);
        if (cookies == null) {
            return new ArrayList<Cookie>();
        } else {
            // Get all cookies available in the HTTP state
            List<Cookie> iteratingCookies = new ArrayList<Cookie>(cookies);
            // Find cookies matching the given origin
            List<Cookie> matchedCookies = new ArrayList<Cookie>();
            for (Cookie cookie : iteratingCookies) {
                if (cookieSpec.match(cookie, cookieOrigin)) {
                    matchedCookies.add(cookie);
                }
            }

            return matchedCookies;
        }
    }

    @Nonnull
    public CookieOrigin getCookieOrigin(@Nonnull URI uri) {
        String scheme = uri.getScheme().toLowerCase();
        String host = uri.getHost();
        int port = uri.getPort();
        String path = uri.getPath();
        boolean secure;
        if ("https".equals(scheme)) {
            secure = true;
            if (port < 0) {
                port = 443;
            }
        } else if ("http".equals(scheme)) {
            secure = false;
            if (port < 0) {
                port = 80;
            }
        } else {
            throw new IllegalArgumentException("Unexpected scheme: " + scheme);
        }

        CookieOrigin cookieOrigin = new CookieOrigin(
                host,
                port,
                path,
                secure);

        return cookieOrigin;
    }

    @Nullable
    public Cookie purgeDuplicateCookies(@Nonnull ICookieStore persistentCookieStore, @Nonnull CookieSpec cookieSpec, @Nonnull String url, @Nonnull String cookieName) {
        URI uri = URI.create(url);
        return purgeDuplicateCookies(persistentCookieStore, cookieSpec, uri, cookieName);
    }

    @Nullable
    public Cookie purgeDuplicateCookies(@Nonnull ICookieStore persistentCookieStore, @Nonnull CookieSpec cookieSpec, @Nonnull URI uri, @Nonnull String cookieName) {
        List<Cookie> allCookies = persistentCookieStore.getCookies();
        List<Cookie> matchingCookies = getCookiesForURI(allCookies, cookieSpec, uri);
        Cookie matchingCookie = null;
        for (Cookie cookie : matchingCookies) {
            if (cookieName.equalsIgnoreCase(cookie.getName())) {
                if (matchingCookie == null) {
                    matchingCookie = cookie;
                } else {
                    persistentCookieStore.deleteCookie(cookie);
                }
            }
        }
        return matchingCookie;
    }
}
