package com.jivesoftware.android.httpclient.util;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

/**
 * Created by mark.schisler on 8/15/14.
 */
public interface ICookieStore extends CookieStore {
    void deleteCookie(Cookie cookie);
}
