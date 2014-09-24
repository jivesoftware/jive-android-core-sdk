package com.jivesoftware.android.mobile.sdk.httpclient;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.SM;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;

import java.util.ArrayList;
import java.util.List;

public class ValueQuotingBrowserCompatSpec extends BrowserCompatSpec {
    public ValueQuotingBrowserCompatSpec(String[] datepatterns) {
        super(datepatterns);
    }

    public ValueQuotingBrowserCompatSpec() {
    }

    // Copied almost exactly from BrowserCompatSpec#formatCookies

    @Override
    public List<Header> formatCookies(List<Cookie> cookies) {
        if (cookies == null) {
            throw new IllegalArgumentException("List of cookies may not be null");
        }
        if (cookies.isEmpty()) {
            throw new IllegalArgumentException("List of cookies may not be empty");
        }
        CharArrayBuffer buffer = new CharArrayBuffer(20 * cookies.size());
        buffer.append(SM.COOKIE);
        buffer.append(": ");
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
            if (i > 0) {
                buffer.append("; ");
            }
            buffer.append(cookie.getName());
            buffer.append("=");
            String s = cookie.getValue();
            if (s != null) {
                // Jive start

                // RFC2109Spec#formatCookies uses the following code to format a cookie:
                //
                // formatParamAsVer(buffer, cookie.getName(), cookie.getValue(), version);
                //
                // protected void formatParamAsVer(final CharArrayBuffer buffer,
                //             final String name, final String value, int version) {
                //     buffer.append(name);
                //     buffer.append("=");
                //     if (value != null) {
                //         if (version > 0) {
                //             buffer.append('\"');
                //             buffer.append(value);
                //             buffer.append('\"');
                //         } else {
                //             buffer.append(value);
                //         }
                //     }
                // }
                //
                // Notice that RFC2109Spec#formatParamAsVer doesn't check the cookie value for quotes
                // so we don't either.

                buffer.append('\"');
                buffer.append(s);
                buffer.append('\"');

                // Jive end
            }
        }
        List<Header> headers = new ArrayList<Header>(1);
        headers.add(new BufferedHeader(buffer));
        return headers;
    }
}
