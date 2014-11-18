package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.MetadataPropertyEntity;
import com.jivesoftware.android.mobile.sdk.entity.SessionGrantEntity;
import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;
import com.jivesoftware.android.mobile.sdk.entity.VersionEntity;
import com.jivesoftware.android.mobile.sdk.json.JiveJson;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreExceptionFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.net.URL;

@ParametersAreNonnullByDefault
public class JiveCoreUnauthenticated {
    static {
        // Uncomment to turn on HttpClient Wire Debugging
        //noinspection ConstantConditions,ConstantIfStatement
        if (false) {
            java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);

            System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
            System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
            System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
            System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "debug");
            System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "debug");
        }

        // also run the following in adb
        // adb shell setprop log.tag.org.apache.http VERBOSE
        // adb shell setprop log.tag.org.apache.http.wire VERBOSE
        // adb shell setprop log.tag.org.apache.http.headers VERBOSE
    }

    @Nonnull
    public final JiveCoreUnauthenticatedRequestFactory jiveCoreUnauthenticatedRequestFactory;
    @Nonnull
    private final JiveCoreJiveJsonCallableFactory jiveCoreJiveJsonCallableFactory;
    @Nonnull
    private final JiveCoreGenericCallableFactory jiveCoreGenericCallableFactory;

    public JiveCoreUnauthenticated(
            URL baseURL,
            String oauthCredentials,
            String oauthAddOnUUID,
            HttpClient httpClient,
            JiveJson jiveJson) {
        this(new JiveCoreUnauthenticatedRequestFactory(baseURL, oauthCredentials, oauthAddOnUUID), httpClient, jiveJson);
    }

    public JiveCoreUnauthenticated(
            JiveCoreUnauthenticatedRequestFactory jiveCoreUnauthenticatedRequestFactory,
            HttpClient httpClient,
            JiveJson jiveJson) {
        this(
                jiveCoreUnauthenticatedRequestFactory,
                httpClient,
                jiveJson,
                new JiveCoreExceptionFactory(jiveJson));
    }

    public JiveCoreUnauthenticated(
            JiveCoreUnauthenticatedRequestFactory jiveCoreUnauthenticatedRequestFactory,
            HttpClient httpClient,
            JiveJson jiveJson,
            JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        this(
                jiveCoreUnauthenticatedRequestFactory,
                new JiveCoreJiveJsonCallableFactory(httpClient, jiveJson, jiveCoreExceptionFactory),
                new JiveCoreGenericCallableFactory(httpClient, jiveCoreExceptionFactory));
    }

    public JiveCoreUnauthenticated(
            JiveCoreUnauthenticatedRequestFactory jiveCoreUnauthenticatedRequestFactory,
            JiveCoreJiveJsonCallableFactory jiveCoreJiveJsonCallableFactory,
            JiveCoreGenericCallableFactory jiveCoreGenericCallableFactory) {
        this.jiveCoreUnauthenticatedRequestFactory = jiveCoreUnauthenticatedRequestFactory;
        this.jiveCoreJiveJsonCallableFactory = jiveCoreJiveJsonCallableFactory;
        this.jiveCoreGenericCallableFactory = jiveCoreGenericCallableFactory;
    }

    @Nonnull
    public JiveCoreCallable<TokenEntity> authorizeDevice(String username, String password) {
        HttpPost authorizeDeviceHttpPost = jiveCoreUnauthenticatedRequestFactory.authorizeDevice(username, password);
        return jiveCoreJiveJsonCallableFactory.createGsonCallable(authorizeDeviceHttpPost, TokenEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<VersionEntity> fetchVersion() {
        HttpGet fetchVersionHttpGet = jiveCoreUnauthenticatedRequestFactory.fetchVersion();
        return jiveCoreJiveJsonCallableFactory.createGsonCallable(fetchVersionHttpGet, VersionEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<MetadataPropertyEntity[]> fetchPublicMetadataProperties() {
        HttpGet fetchPublicMetadataPropertiesHttpGet = jiveCoreUnauthenticatedRequestFactory.fetchPublicMetadataProperties();
        return jiveCoreJiveJsonCallableFactory.createGsonCallable(fetchPublicMetadataPropertiesHttpGet, MetadataPropertyEntity[].class);
    }

    @Nonnull
    public JiveCoreCallable<TokenEntity> refreshToken(String refreshToken) {
        HttpPost refreshTokenHttpPost = jiveCoreUnauthenticatedRequestFactory.refreshToken(refreshToken);
        return jiveCoreJiveJsonCallableFactory.createGsonCallable(refreshTokenHttpPost, TokenEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<SessionGrantEntity> fetchSessionGrant() {
        HttpGet fetchSessionGrantHttpGet = jiveCoreUnauthenticatedRequestFactory.isSessionOAuthGrantAllowed();
        return jiveCoreJiveJsonCallableFactory.createGsonCallable(fetchSessionGrantHttpGet, SessionGrantEntity.class);
    }

    @Nonnull
    public <T> JiveCoreCallable<T> createCallable(HttpRequestBase httpRequestBase, HttpResponseParserFactory<T> httpResponseParserFactory) {
        return jiveCoreGenericCallableFactory.createGenericCallable(httpRequestBase, httpResponseParserFactory);
    }
}
