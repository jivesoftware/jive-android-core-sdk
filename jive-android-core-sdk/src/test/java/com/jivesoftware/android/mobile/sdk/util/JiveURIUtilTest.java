package com.jivesoftware.android.mobile.sdk.util;

import com.jivesoftware.android.mobile.sdk.core.JiveCoreRequestOptions;
import org.junit.Test;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class JiveURIUtilTest {
    private static final String EXPECTED_COMPLETE_ROOT_URL = "http://jivesoftware.com/api/coreV3/someEndpoint/";
    private static final String EXPECTED_COMPLETE_NONROOT_URL = "http://jivesoftware.com/NonRootContext/api/coreV3/someEndpoint/";

    @Test
    public void testCreateURIWithOptions() throws Exception {
        JiveCoreRequestOptions options = new JiveCoreRequestOptions();
        options.setCount(2);
        options.setFields(Arrays.asList("foo", "bar"));

        URI actual = JiveURIUtil.createURI(new URL("http://jivesoftware.com"), "/path", options);
        assertEquals(new URI("http://jivesoftware.com/path?count=2&fields=foo%2Cbar"), actual);
    }

    @Test
    public void testCreateQueriedURIWithOptions() throws Exception {
        JiveCoreRequestOptions options = new JiveCoreRequestOptions();
        options.setCount(2);
        options.setFields(Arrays.asList("foo", "bar"));

        URI actual = JiveURIUtil.createURI(new URL("http://jivesoftware.com"), "/path?fizz=buzz", options);
        assertEquals(new URI("http://jivesoftware.com/path?fizz=buzz&count=2&fields=foo%2Cbar"), actual);
    }

    @Test
    public void testWhenUrlHasNoQueryParamsButIsFullyFormedThenQuestionMarkIsNotAppended() throws Exception {
        JiveCoreRequestOptions options = new JiveCoreRequestOptions();
        String url = "http://jivesoftware.com/?q=whatever";
        URI actual = JiveURIUtil.createURI(new URL(url), "", options);
        assertEquals(new URI(url), actual);
    }

    @Test
    public void testWhenBaseUrlHasNoTrailingSlash() throws Exception {
        String urlStr = "http://jivesoftware.com";
        String subPath = "api/coreV3/someEndpoint/";
        URL baseURL = new URL(urlStr);
        URI actual = JiveURIUtil.createURI(baseURL, subPath);
        assertEquals(EXPECTED_COMPLETE_ROOT_URL, actual.toString());
    }

    @Test
    public void testWhenBaseUrlHasTrailingSlash() throws Exception {
        String urlStr = "http://jivesoftware.com/";
        String subPath = "api/coreV3/someEndpoint/";
        URL baseURL = new URL(urlStr);
        URI actual = JiveURIUtil.createURI(baseURL, subPath);
        assertEquals(EXPECTED_COMPLETE_ROOT_URL, actual.toString());
    }

    @Test
    public void testWhenBaseUrlHasPath() throws Exception {
        String urlStr = "http://jivesoftware.com/NonRootContext/";
        String subPath = "api/coreV3/someEndpoint/";
        URL baseURL = new URL(urlStr);
        URI actual = JiveURIUtil.createURI(baseURL, subPath);
        assertEquals(EXPECTED_COMPLETE_NONROOT_URL, actual.toString());
    }


    @Test
    public void testWhenBaseUrlHasPathWithNoTrailingSlashAndSubpathIsAbsolute() throws Exception {
        String urlStr = "http://jivesoftware.com/NonRootContext";
        String subPath = "/api/coreV3/someEndpoint/";
        URL baseURL = new URL(urlStr);
        URI actual = JiveURIUtil.createURI(baseURL, subPath);
        assertEquals(EXPECTED_COMPLETE_NONROOT_URL, actual.toString());
    }

    @Test
    public void testWhenBaseUrlHasPathWithNoTrailingSlashAndSubpathIsRelative() throws Exception {
        String urlStr = "http://jivesoftware.com/NonRootContext";
        String subPath = "api/coreV3/someEndpoint/";
        URL baseURL = new URL(urlStr);
        URI actual = JiveURIUtil.createURI(baseURL, subPath);
        assertEquals(EXPECTED_COMPLETE_NONROOT_URL, actual.toString());
    }

    @Test
    public void testWhenBaseUrlHasPathAndSubpathIsAbsolute() throws Exception {
        String urlStr = "http://jivesoftware.com/NonRootContext/";
        String subPath = "/api/coreV3/someEndpoint/";
        URL baseURL = new URL(urlStr);
        URI actual = JiveURIUtil.createURI(baseURL, subPath);
        assertEquals(EXPECTED_COMPLETE_NONROOT_URL, actual.toString());
    }
}
