package com.jivesoftware.android.mobile.sdk.core;

import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class JiveCoreURIFactoryTest {
    private static final String EXPECTED_COMPLETE_ROOT_URL = "http://jivesoftware.com/api/coreV3/someEndpoint/";
    private static final String EXPECTED_COMPLETE_NONROOT_URL = "http://jivesoftware.com/NonRootContext/api/coreV3/someEndpoint/";

    private JiveCoreURIFactory testObject;

    @Test
    public void testCreateURIWithOptions() throws Exception {
        JiveCoreRequestOptions options = new JiveCoreRequestOptions();
        options.setCount(2);
        options.setFields(Arrays.asList("foo", "bar"));

        testObject = new JiveCoreURIFactory(new URL("http://jivesoftware.com"));
        URI actual = testObject.createURI("/path", options);
        assertEquals(new URI("http://jivesoftware.com/path?count=2&fields=foo%2Cbar"), actual);
    }

    @Test
    public void testCreateQueriedURIWithOptions() throws Exception {
        JiveCoreRequestOptions options = new JiveCoreRequestOptions();
        options.setCount(2);
        options.setFields(Arrays.asList("foo", "bar"));

        testObject = new JiveCoreURIFactory(new URL("http://jivesoftware.com"));
        URI actual = testObject.createURI("/path?fizz=buzz", options);
        assertEquals(new URI("http://jivesoftware.com/path?fizz=buzz&count=2&fields=foo%2Cbar"), actual);
    }

    @Test
    public void testWhenUrlHasNoQueryParamsButIsFullyFormedThenQuestionMarkIsNotAppended() throws Exception {
        JiveCoreRequestOptions options = new JiveCoreRequestOptions();
        String baseUrl = "http://jivesoftware.com/?q=whatever";

        testObject = new JiveCoreURIFactory(new URL(baseUrl));
        URI actual = testObject.createURI("", options);
        assertEquals(new URI(baseUrl), actual);
    }

    @Test
    public void testWhenBaseUrlHasNoTrailingSlash() throws Exception {
        String urlStr = "http://jivesoftware.com";
        String subPath = "api/coreV3/someEndpoint/";
        URL baseURL = new URL(urlStr);

        testObject = new JiveCoreURIFactory(baseURL);
        URI actual = testObject.createURI(subPath);
        assertEquals(EXPECTED_COMPLETE_ROOT_URL, actual.toString());
    }

    @Test
    public void testWhenBaseUrlHasTrailingSlash() throws Exception {
        String urlStr = "http://jivesoftware.com/";
        String subPath = "api/coreV3/someEndpoint/";
        URL baseURL = new URL(urlStr);

        testObject = new JiveCoreURIFactory(baseURL);
        URI actual = testObject.createURI(subPath);
        assertEquals(EXPECTED_COMPLETE_ROOT_URL, actual.toString());
    }

    @Test
    public void testWhenBaseUrlHasPath() throws Exception {
        String urlStr = "http://jivesoftware.com/NonRootContext/";
        String subPath = "api/coreV3/someEndpoint/";
        URL baseURL = new URL(urlStr);

        testObject = new JiveCoreURIFactory(baseURL);
        URI actual = testObject.createURI(subPath);
        assertEquals(EXPECTED_COMPLETE_NONROOT_URL, actual.toString());
    }


    @Test
    public void testWhenBaseUrlHasPathWithNoTrailingSlashAndSubpathIsAbsolute() throws Exception {
        String urlStr = "http://jivesoftware.com/NonRootContext";
        String subPath = "/api/coreV3/someEndpoint/";
        URL baseURL = new URL(urlStr);

        testObject = new JiveCoreURIFactory(baseURL);
        URI actual = testObject.createURI(subPath);
        assertEquals(EXPECTED_COMPLETE_NONROOT_URL, actual.toString());
    }

    @Test
    public void testWhenBaseUrlHasPathWithNoTrailingSlashAndSubpathIsRelative() throws Exception {
        String urlStr = "http://jivesoftware.com/NonRootContext";
        String subPath = "api/coreV3/someEndpoint/";
        URL baseURL = new URL(urlStr);

        testObject = new JiveCoreURIFactory(baseURL);
        URI actual = testObject.createURI(subPath);
        assertEquals(EXPECTED_COMPLETE_NONROOT_URL, actual.toString());
    }

    @Test
    public void testWhenBaseUrlHasPathAndSubpathIsAbsolute() throws Exception {
        String urlStr = "http://jivesoftware.com/NonRootContext/";
        String subPath = "/api/coreV3/someEndpoint/";
        URL baseURL = new URL(urlStr);

        testObject = new JiveCoreURIFactory(baseURL);
        URI actual = testObject.createURI(subPath);
        assertEquals(EXPECTED_COMPLETE_NONROOT_URL, actual.toString());
    }

}
