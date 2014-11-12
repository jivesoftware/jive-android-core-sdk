package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.httpclient.util.JiveEntityUtil;
import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;
import com.jivesoftware.android.mobile.sdk.httpclient.JiveCoreAuthScheme;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import static com.jivesoftware.android.mobile.sdk.util.JiveURIUtil.createURI;

@ParametersAreNonnullByDefault
public class JiveCoreUnauthenticatedRequestFactory {
    @Nonnull
    private final String oauthCredentials;
    @Nonnull
    private final URL baseURL;
    @Nonnull
    private final String addonUUID;

    public JiveCoreUnauthenticatedRequestFactory(URL baseURL, String oauthCredentials, String addonUUID) {
        this.oauthCredentials = oauthCredentials;
        this.baseURL = baseURL;
        this.addonUUID = addonUUID;
    }

    @Nonnull
    public HttpGet fetchVersion() {
        URI fetchVersionURI = createURI(baseURL, "api/version");
        HttpGet versionHttpGet = new HttpGet(fetchVersionURI);
        return versionHttpGet;
    }

    @Nonnull
    public HttpGet fetchPublicMetadataProperties() {
        URI fetchPublicMetadataPropertiesURI = createURI(baseURL, "api/core/v3/metadata/properties/public");
        HttpGet publicMetadataPropertiesHttpGet = new HttpGet(fetchPublicMetadataPropertiesURI);
        return publicMetadataPropertiesHttpGet;
    }

    @Nonnull
    public HttpPost authorizeDevice(String username, String password) {
        URI uri = createURI(baseURL, JiveCoreEndpoints.OAUTH2_TOKEN_REQUEST_URL);
        HttpPost authorizeDeviceHttpPost = new HttpPost(uri);
        authorizeDeviceHttpPost.setHeader(JiveCoreHeaders.AUTHORIZATION, "Basic " + oauthCredentials);

        ArrayList<BasicNameValuePair> bodyNameValuePairs = new ArrayList<BasicNameValuePair>();
        bodyNameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
        bodyNameValuePairs.add(new BasicNameValuePair("username", username));
        bodyNameValuePairs.add(new BasicNameValuePair("password", password));

        authorizeDeviceHttpPost.setEntity(JiveEntityUtil.createForm(bodyNameValuePairs));

        return authorizeDeviceHttpPost;
    }

    @Nonnull
    public HttpPost refreshToken(String refreshToken) {
        URI uri = createURI(baseURL, JiveCoreEndpoints.OAUTH2_TOKEN_REFRESH_URL);
        HttpPost authorizeDeviceHttpPost = new HttpPost(uri);
        authorizeDeviceHttpPost.setHeader(JiveCoreHeaders.AUTHORIZATION, "Basic " + oauthCredentials);

        ArrayList<BasicNameValuePair> bodyNameValuePairs = new ArrayList<BasicNameValuePair>();
        bodyNameValuePairs.add(new BasicNameValuePair("grant_type", "refresh_token"));
        bodyNameValuePairs.add(new BasicNameValuePair("refresh_token", refreshToken));

        authorizeDeviceHttpPost.setEntity(JiveEntityUtil.createForm(bodyNameValuePairs));

        return authorizeDeviceHttpPost;
    }

    @Nonnull
    public HttpGet isSessionOAuthGrantAllowed() {
        URI uri = createURI(baseURL, "/api/addons/" + addonUUID + "/session-grant-allowed");
        return new HttpGet(uri);
    }
}
