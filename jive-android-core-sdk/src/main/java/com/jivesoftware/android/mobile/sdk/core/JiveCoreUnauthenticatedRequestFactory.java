package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.httpclient.util.JiveEntityUtil;
import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;
import com.jivesoftware.android.mobile.sdk.httpclient.JiveCoreAuthScheme;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import javax.annotation.Nonnull;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import static com.jivesoftware.android.mobile.sdk.util.JiveURIUtil.createURI;

public class JiveCoreUnauthenticatedRequestFactory {
    @Nonnull
    private final URL baseURL;

    public JiveCoreUnauthenticatedRequestFactory(@Nonnull URL baseURL) {
        this.baseURL = baseURL;
    }

    public HttpGet fetchVersion() {
        URI fetchVersionURI = createURI(baseURL, "api/version");
        HttpGet versionHttpGet = new HttpGet(fetchVersionURI);
        return versionHttpGet;
    }

    public HttpGet fetchPublicMetadataProperties() {
        URI fetchPublicMetadataPropertiesURI = createURI(baseURL, "api/core/v3/metadata/properties/public");
        HttpGet publicMetadataPropertiesHttpGet = new HttpGet(fetchPublicMetadataPropertiesURI);
        return publicMetadataPropertiesHttpGet;
    }

    public HttpPost authorizeDevice(@Nonnull String username, @Nonnull String password) {
        URI uri = createURI(baseURL, JiveCoreEndpoints.OAUTH2_TOKEN_REQUEST_URL);
        HttpPost authorizeDeviceHttpPost = new HttpPost(uri);
        authorizeDeviceHttpPost.setHeader(JiveCoreHeaders.AUTHORIZATION, "Basic " + JiveCoreConstants.OAUTH2_CREDENTIALS);

        ArrayList<BasicNameValuePair> bodyNameValuePairs = new ArrayList<BasicNameValuePair>();
        bodyNameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
        bodyNameValuePairs.add(new BasicNameValuePair("username", username));
        bodyNameValuePairs.add(new BasicNameValuePair("password", password));

        authorizeDeviceHttpPost.setEntity(JiveEntityUtil.createForm(bodyNameValuePairs));

        return authorizeDeviceHttpPost;
    }

    public HttpPost deauthorizeDevice(@Nonnull TokenEntity tokenEntity) {
        URI uri = createURI(baseURL, JiveCoreEndpoints.OAUTH2_TOKEN_REVOKE_URL);
        HttpPost deauthorizeDevicePost = new HttpPost(uri);
        Header authenticationHeader = JiveCoreAuthScheme.authenticate(tokenEntity);
        deauthorizeDevicePost.addHeader(authenticationHeader);

        return deauthorizeDevicePost;
    }

    public HttpPost authorizeDeviceFromSession() {
        final URI uri = createURI(baseURL, JiveCoreEndpoints.OAUTH2_TOKEN_REQUEST_URL);
        HttpPost authorizeDeviceHttpPost = new HttpPost(uri);
        authorizeDeviceHttpPost.setHeader(JiveCoreHeaders.AUTHORIZATION, "Basic " + JiveCoreConstants.OAUTH2_CREDENTIALS);

        authorizeDeviceHttpPost.setEntity(JiveEntityUtil.createForm("grant_type", "session"));

        return authorizeDeviceHttpPost;
    }

    public HttpPost refreshToken(String refreshToken) {
        URI uri = createURI(baseURL, JiveCoreEndpoints.OAUTH2_TOKEN_REFRESH_URL);
        HttpPost authorizeDeviceHttpPost = new HttpPost(uri);
        authorizeDeviceHttpPost.setHeader(JiveCoreHeaders.AUTHORIZATION, "Basic " + JiveCoreConstants.OAUTH2_CREDENTIALS);

        ArrayList<BasicNameValuePair> bodyNameValuePairs = new ArrayList<BasicNameValuePair>();
        bodyNameValuePairs.add(new BasicNameValuePair("grant_type", "refresh_token"));
        bodyNameValuePairs.add(new BasicNameValuePair("refresh_token", refreshToken));

        authorizeDeviceHttpPost.setEntity(JiveEntityUtil.createForm(bodyNameValuePairs));

        return authorizeDeviceHttpPost;
    }

    @Nonnull
    public HttpGet isSessionOAuthGrantAllowed() {
        URI uri = createURI(baseURL, "/api/addons/" + JiveCoreConstants.OAUTH2_ADDON_UUID + "/session-grant-allowed");
        return new HttpGet(uri);
    }
}
