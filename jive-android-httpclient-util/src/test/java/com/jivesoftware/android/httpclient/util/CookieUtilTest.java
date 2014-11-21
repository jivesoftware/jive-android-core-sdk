package com.jivesoftware.android.httpclient.util;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class CookieUtilTest {

    @Mock
    private CookieSpec mockCookieSpec;

    @Test
    public void getRequestHeaderValueWithNullCookies() {
        Header actualRequestHeader = CookieUtil.getRequestHeader(null, mockCookieSpec);
        assertNull(actualRequestHeader);
    }

    @Test
    public void getRequestHeaderValueWithNoCookies() {
        List<Cookie> cookies = Collections.emptyList();
        Header actualRequestHeader = CookieUtil.getRequestHeader(cookies, mockCookieSpec);
        assertNull(actualRequestHeader);
    }

    @Test
    public void getRequestHeaderValueWithOneCookies() {
        Cookie mockCookie = mock(Cookie.class);
        Header mockCookieHeader = mock(Header.class);
        doReturn(Arrays.asList(mockCookieHeader)).when(mockCookieSpec).formatCookies(Collections.singletonList(mockCookie));
        doReturn("cookieHeaderValue").when(mockCookieHeader).getValue();

        List<Cookie> cookies = Arrays.asList(mockCookie);

        Header actualRequestHeader = CookieUtil.getRequestHeader(cookies, mockCookieSpec);
        assertNotNull(actualRequestHeader);
        assertEquals("cookieHeaderValue", actualRequestHeader.getValue());
    }

    @Test
    public void getRequestHeaderValueWithManyCookies() {
        Cookie mockCookie1 = mock(Cookie.class);
        Header mockCookieHeader1 = mock(Header.class);
        doReturn(Arrays.asList(mockCookieHeader1)).when(mockCookieSpec).formatCookies(Collections.singletonList(mockCookie1));
        doReturn("cookieHeaderValue1").when(mockCookieHeader1).getValue();

        Cookie mockCookie2 = mock(Cookie.class);
        Header mockCookieHeader2 = mock(Header.class);
        doReturn(Arrays.asList(mockCookieHeader2)).when(mockCookieSpec).formatCookies(Collections.singletonList(mockCookie2));
        doReturn("cookieHeaderValue2").when(mockCookieHeader2).getValue();

        Cookie mockCookie3 = mock(Cookie.class);
        Header mockCookieHeader3 = mock(Header.class);
        doReturn(Arrays.asList(mockCookieHeader3)).when(mockCookieSpec).formatCookies(Collections.singletonList(mockCookie3));
        doReturn("cookieHeaderValue3").when(mockCookieHeader3).getValue();

        List<Cookie> cookies = Arrays.asList(mockCookie1, mockCookie2, mockCookie3);

        Header actualRequestHeader = CookieUtil.getRequestHeader(cookies, mockCookieSpec);
        assertNotNull(actualRequestHeader);
        assertEquals("cookieHeaderValue1;cookieHeaderValue2;cookieHeaderValue3", actualRequestHeader.getValue());
    }

    @Test
    public void getCookieOriginUsesUriPort() {
        URI uri = URI.create("http://example.com:8080/");
        CookieOrigin actualCookieOrigin = CookieUtil.getCookieOrigin(uri);
        assertEquals(8080, actualCookieOrigin.getPort());
    }

    @Test
    public void getCookieOriginUses443IfUriPortUndefinedAndSchemeHttps() {
        URI uri = URI.create("https://example.com/");
        CookieOrigin actualCookieOrigin = CookieUtil.getCookieOrigin(uri);
        assertEquals(443, actualCookieOrigin.getPort());
    }

    @Test
    public void getCookieOriginUses80IfUriPortUndefinedAndSchemeHttp() {
        URI uri = URI.create("http://example.com/");
        CookieOrigin actualCookieOrigin = CookieUtil.getCookieOrigin(uri);
        assertEquals(80, actualCookieOrigin.getPort());
    }

    @Test
    public void getCookieOriginIsSecureWhenSchemeHttps() {
        URI uri = URI.create("https://example.com/");
        CookieOrigin actualCookieOrigin = CookieUtil.getCookieOrigin(uri);
        assertTrue(actualCookieOrigin.isSecure());
    }

    @Test
    public void getCapitalCookieOriginIsSecureWhenSchemeHttps() {
        URI uri = URI.create("HTTPS://example.com/");
        CookieOrigin actualCookieOrigin = CookieUtil.getCookieOrigin(uri);
        assertTrue(actualCookieOrigin.isSecure());
    }


    @Test
    public void getCapitalCookieOriginInsecureWhenSchemeHttp() {
        URI uri = URI.create("HTTP://example.com/");
        CookieOrigin actualCookieOrigin = CookieUtil.getCookieOrigin(uri);
        assertFalse(actualCookieOrigin.isSecure());
    }

    @Test
    public void getCookieOriginThrowsIllegalArgumentExceptionWhenSchemeIsUnknown() {
        URI uri = URI.create("foo://example.com/");
        try {
            CookieUtil.getCookieOrigin(uri);
            fail();
        } catch (IllegalArgumentException e) {
            // success!
        }
    }

    @Test
    public void getCookieOriginUsesUriHost() {
        URI uri = URI.create("http://example.com/foo/bar");
        CookieOrigin actualCookieOrigin = CookieUtil.getCookieOrigin(uri);
        assertEquals("example.com", actualCookieOrigin.getHost());
    }

    @Test
    public void getCookieOriginUsesUriPath() {
        URI uri = URI.create("http://example.com/foo/bar");
        CookieOrigin actualCookieOrigin = CookieUtil.getCookieOrigin(uri);
        assertEquals("/foo/bar", actualCookieOrigin.getPath());
    }

    @Test
    public void getCookiesForURIReturnsEmptyListWhenCookiesAreNull() {
        assertEquals(Arrays.<Cookie>asList(), CookieUtil.getCookiesForURI(null, mock(CookieSpec.class), URI.create("http://example.com/")));
    }

    @Test
    public void getCookiesForURIReturnsCookiesThatTheCookieSpecSaysMatch() {
        Cookie mockCookie1 = mock(Cookie.class);
        Cookie mockCookie2 = mock(Cookie.class);
        Cookie mockCookie3 = mock(Cookie.class);
        Cookie mockCookie4 = mock(Cookie.class);

        CookieSpec mockCookieSpec = mock(CookieSpec.class);
        doReturn(true).when(mockCookieSpec).match(eq(mockCookie1), any(CookieOrigin.class));
        doReturn(false).when(mockCookieSpec).match(eq(mockCookie2), any(CookieOrigin.class));
        doReturn(true).when(mockCookieSpec).match(eq(mockCookie3), any(CookieOrigin.class));
        doReturn(true).when(mockCookieSpec).match(eq(mockCookie4), any(CookieOrigin.class));

        List<Cookie> actualMatchedCookies = CookieUtil.getCookiesForURI(
                Arrays.asList(
                        mockCookie1,
                        mockCookie2,
                        mockCookie3,
                        mockCookie4),
                mockCookieSpec,
                URI.create("http://example.com/")
        );
        assertEquals(
                Arrays.asList(
                        mockCookie1,
                        mockCookie3,
                        mockCookie4),
                actualMatchedCookies
        );
    }
}
