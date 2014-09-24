package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.httpclient.util.JiveEntityUtil;
import com.jivesoftware.android.mobile.httpclient.matcher.HttpMatchers;
import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by mark.schisler on 8/19/14.
 */
public class JiveCoreUnauthenticatedRequestFactoryTest {

    @Mock private HttpClient httpClient;

    private JiveCoreUnauthenticatedRequestFactory testObject;
    private String baseUrl;

    @Before
    public void setUp() throws MalformedURLException {
        baseUrl = "http://jiveland.com";
        testObject = new JiveCoreUnauthenticatedRequestFactory(new URL(baseUrl));
    }

    @Test
    public void testWhenVersionThenRequestIsCreatedProperly() {
        HttpGet get = testObject.fetchVersion();

        assertNotNull(get);
        assertEquals(get.getMethod(), "GET");
        assertEquals(get.getURI(), URI.create(baseUrl + JiveCoreEndpoints.VERSION));
    }

    @Test
    public void testWhenRefreshTokenOccursThenOauth2TokenIsCalledWithRefreshGrant() throws IOException {
        String refreshTokenValue = "refreshTokenValue";
        HttpPost post = testObject.refreshToken(refreshTokenValue);

        assertNotNull(post);
        assertEquals(post.getMethod(), "POST");
        assertEquals(post.getURI(), URI.create(baseUrl + "/oauth2/token"));
        assertEquals(post.getFirstHeader(JiveCoreHeaders.AUTHORIZATION).getValue(), "Basic " + JiveCoreConstants.OAUTH2_CREDENTIALS);
        assertEquals(JiveEntityUtil.toString(post.getEntity()), "grant_type=refresh_token&refresh_token=" + refreshTokenValue);
    }

    @Test
    public void testWhenAuthorizeDeviceThenRequestIsCreatedProperly() throws IOException {
        String user = "user";
        String password = "password123";
        HttpPost post = testObject.authorizeDevice(user, password);

        assertNotNull(post);
        assertEquals(post.getMethod(), "POST");
        assertEquals(post.getURI(), URI.create(baseUrl + "/oauth2/token"));
        assertEquals(post.getFirstHeader(JiveCoreHeaders.AUTHORIZATION).getValue(), "Basic " + JiveCoreConstants.OAUTH2_CREDENTIALS);
        assertEquals(JiveEntityUtil.toString(post.getEntity()), "grant_type=password&username=" + user + "&password=" + password);
    }

    @Test
    public void testWhenAuthorizeDeviceFromSessionThenRequestIsCreatedProperly() throws IOException {
        HttpPost post = testObject.authorizeDeviceFromSession();

        assertNotNull(post);
        assertEquals(post.getMethod(), "POST");
        assertEquals(post.getURI(), URI.create(baseUrl + "/oauth2/token"));
        assertEquals(post.getFirstHeader(JiveCoreHeaders.AUTHORIZATION).getValue(), "Basic " + JiveCoreConstants.OAUTH2_CREDENTIALS);
        assertEquals(JiveEntityUtil.toString(post.getEntity()), "grant_type=session");
    }

    @Test
    public void testWhenDeauthorizeDeviceThenRequestIsCreatedProperly() throws IOException {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.tokenType = "tokenType";
        tokenEntity.accessToken = "accessToken";

        HttpPost post = testObject.deauthorizeDevice(tokenEntity);

        assertNotNull(post);
        assertEquals(post.getMethod(), "POST");
        assertEquals(post.getURI(), URI.create(baseUrl + "/oauth2/revoke"));
        assertEquals(post.getFirstHeader(JiveCoreHeaders.AUTHORIZATION).getValue(), tokenEntity.tokenType + " " + tokenEntity.accessToken);
    }

    @Test
    public void testWhenIsSessionOAuthGrantAllowedCalledThenRequestIsCreatedProperly() {
        HttpGet get = testObject.isSessionOAuthGrantAllowed();

        assertNotNull(get);
        assertEquals(get.getMethod(), "GET");
        assertEquals(get.getURI(), URI.create(baseUrl + "/api/addons/" + JiveCoreConstants.OAUTH2_ADDON_UUID + "/session-grant-allowed"));
    }

    @Test
    public void testGetMetadataPropertiesPublic()  {
        HttpGet get = testObject.fetchPublicMetadataProperties();

        assertThat(get, allOf(
                HttpMatchers.requestUrl(baseUrl + JiveCoreConstants.CORE_API_V3_PREFIX + "/metadata/properties/public"),
                HttpMatchers.requestMethod("GET")));
    }


}
