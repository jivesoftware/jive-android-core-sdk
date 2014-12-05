package com.jivesoftware.android.mobile.sdk.core;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.jivesoftware.android.mobile.httpclient.matcher.HttpMatchers.httpHeadersContains;
import static com.jivesoftware.android.mobile.httpclient.matcher.HttpMatchers.requestEntity;
import static com.jivesoftware.android.mobile.httpclient.matcher.HttpMatchers.requestIsRedirecting;
import static com.jivesoftware.android.mobile.httpclient.matcher.HttpMatchers.requestUrl;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class JiveCoreUnauthenticatedRequestFactoryTest {

    private JiveCoreUnauthenticatedRequestFactory testObject;

    @Before
    public void setUp() throws MalformedURLException {
        testObject = new JiveCoreUnauthenticatedRequestFactory(
                new URL("http://jiveland.com"),
                "oauthCredentials",
                "oauthAddOnUUID");
    }

    @Test
    public void testWhenVersionThenRequestIsCreatedProperly() {
        HttpGet fetchVersionHttpGet = testObject.fetchVersion();

        assertThat(fetchVersionHttpGet, allOf(
                requestUrl("http://jiveland.com/api/version"),
                requestIsRedirecting(false)));
    }

    @Test
    public void testWhenRefreshTokenOccursThenOauth2TokenIsCalledWithRefreshGrant() throws IOException {
        HttpPost refreshTokenHttpPost = testObject.refreshToken("refreshTokenValue");

        assertThat(refreshTokenHttpPost, allOf(
                requestUrl("http://jiveland.com/oauth2/token"),
                httpHeadersContains(JiveCoreHeaders.AUTHORIZATION, "Basic oauthCredentials"),
                requestEntity("grant_type", "refresh_token", "refresh_token", "refreshTokenValue"),
                requestIsRedirecting(true)));
    }

    @Test
    public void testWhenAuthorizeDeviceThenRequestIsCreatedProperly() throws IOException {
        HttpPost authorizeDeviceHttpPost = testObject.authorizeDevice("user", "password123");

        assertThat(authorizeDeviceHttpPost, allOf(
                requestUrl("http://jiveland.com/oauth2/token"),
                httpHeadersContains(JiveCoreHeaders.AUTHORIZATION, "Basic oauthCredentials"),
                requestEntity("grant_type", "password", "username", "user", "password", "password123"),
                requestIsRedirecting(true)));
    }

    @Test
    public void testWhenFetchSessionGrantCalledThenRequestIsCreatedProperly() {
        HttpGet fetchSessionGrantHttpGet = testObject.fetchSessionGrant();

        assertThat(fetchSessionGrantHttpGet, allOf(
                requestUrl("http://jiveland.com/api/addons/oauthAddOnUUID/session-grant-allowed"),
                requestIsRedirecting(false)));
    }

    @Test
    public void testGetMetadataPropertiesPublic() {
        HttpGet fetchPublicMetadataPropertiesHttpGet = testObject.fetchPublicMetadataProperties();

        assertThat(fetchPublicMetadataPropertiesHttpGet, allOf(
                requestUrl("http://jiveland.com/api/core/v3/metadata/properties/public"),
                requestIsRedirecting(false)));
    }

    @Test
    public void testGetBaseURLFromFetchVersionLocationReturnsBaseURL() throws Exception {
        URL baseURL = JiveCoreUnauthenticatedRequestFactory.getBaseURLFromFetchVersionLocation("http://jiveland.com/api/version");
        assertEquals(new URL("http://jiveland.com/"), baseURL);
    }

    @Test (expected = JiveCoreInvalidLocationException.class)
    public void testGetBaseURLFromFetchVersionLocationThrowsJiveCoreInvalidLocationExceptionWhenApiVersionMissing() throws Exception {
        JiveCoreUnauthenticatedRequestFactory.getBaseURLFromFetchVersionLocation("http://jiveland.com");
    }

    @Test (expected = JiveCoreInvalidLocationException.class)
    public void testGetBaseURLFromFetchVersionLocationThrowsJiveCoreInvalidLocationExceptionWhenLocationIsntURL() throws Exception {
        JiveCoreUnauthenticatedRequestFactory.getBaseURLFromFetchVersionLocation("foo/bar/api/version");
    }
}
