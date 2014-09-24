package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.MetadataPropertyEntity;
import com.jivesoftware.android.mobile.sdk.entity.SessionGrantEntity;
import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;
import com.jivesoftware.android.mobile.sdk.entity.VersionEntity;
import com.jivesoftware.android.mobile.sdk.gson.JiveGson;
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
    private final JiveCoreUnauthenticatedRequestFactory jiveCoreUnauthenticatedRequestFactory;
    @Nonnull
    private final JiveCoreGsonCallableFactory jiveCoreGsonCallableFactory;
    @Nonnull
    private final JiveCoreEmptyCallableFactory jiveCoreEmptyCallableFactory;
    @Nonnull
    private final JiveCoreGenericCallableFactory jiveCoreGenericCallableFactory;

    public JiveCoreUnauthenticated(
            URL baseURL,
            HttpClient httpClient) {
        this(new JiveCoreUnauthenticatedRequestFactory(baseURL), httpClient, new JiveGson());
    }

    public JiveCoreUnauthenticated(
            JiveCoreUnauthenticatedRequestFactory jiveCoreUnauthenticatedRequestFactory,
            HttpClient httpClient,
            JiveGson jiveGson) {
        this(
                jiveCoreUnauthenticatedRequestFactory,
                httpClient,
                jiveGson,
                new JiveCoreExceptionFactory(jiveGson));
    }

    public JiveCoreUnauthenticated(
            JiveCoreUnauthenticatedRequestFactory jiveCoreUnauthenticatedRequestFactory,
            HttpClient httpClient,
            JiveGson jiveGson,
            JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        this(
                jiveCoreUnauthenticatedRequestFactory,
                new JiveCoreGsonCallableFactory(httpClient, jiveGson, jiveCoreExceptionFactory),
                new JiveCoreEmptyCallableFactory(httpClient, jiveCoreExceptionFactory),
                new JiveCoreGenericCallableFactory(httpClient, jiveCoreExceptionFactory));
    }

    public JiveCoreUnauthenticated(
            JiveCoreUnauthenticatedRequestFactory jiveCoreUnauthenticatedRequestFactory,
            JiveCoreGsonCallableFactory jiveCoreGsonCallableFactory,
            JiveCoreEmptyCallableFactory jiveCoreEmptyCallableFactory,
            JiveCoreGenericCallableFactory jiveCoreGenericCallableFactory) {
        this.jiveCoreUnauthenticatedRequestFactory = jiveCoreUnauthenticatedRequestFactory;
        this.jiveCoreGsonCallableFactory = jiveCoreGsonCallableFactory;
        this.jiveCoreEmptyCallableFactory = jiveCoreEmptyCallableFactory;
        this.jiveCoreGenericCallableFactory = jiveCoreGenericCallableFactory;
    }

    @Nonnull
    public JiveCoreCallable<TokenEntity> authorizeDevice(String username, String password) {
        HttpPost authorizeDeviceHttpPost = jiveCoreUnauthenticatedRequestFactory.authorizeDevice(username, password);
        return jiveCoreGsonCallableFactory.createGsonCallable(authorizeDeviceHttpPost, TokenEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<TokenEntity> authorizeDeviceFromSession() {
        HttpPost authorizeDeviceFromSessionHttpPost = jiveCoreUnauthenticatedRequestFactory.authorizeDeviceFromSession();
        return jiveCoreGsonCallableFactory.createGsonCallable(authorizeDeviceFromSessionHttpPost, TokenEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<Void> deauthorizeDevice(TokenEntity tokenEntity) {
        HttpPost deauthorizeDeviceHttpPost = jiveCoreUnauthenticatedRequestFactory.deauthorizeDevice(tokenEntity);
        return jiveCoreEmptyCallableFactory.createEmptyCallable(deauthorizeDeviceHttpPost);
    }

    @Nonnull
    public JiveCoreCallable<VersionEntity> fetchVersion() {
        HttpGet fetchVersionHttpGet = jiveCoreUnauthenticatedRequestFactory.fetchVersion();
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchVersionHttpGet, VersionEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<MetadataPropertyEntity[]> fetchPublicMetadataProperties() {
        HttpGet fetchPublicMetadataPropertiesHttpGet = jiveCoreUnauthenticatedRequestFactory.fetchPublicMetadataProperties();
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchPublicMetadataPropertiesHttpGet, MetadataPropertyEntity[].class);
    }

    @Nonnull
    public JiveCoreCallable<TokenEntity> refreshToken(String refreshToken) {
        HttpPost refreshTokenHttpPost = jiveCoreUnauthenticatedRequestFactory.refreshToken(refreshToken);
        return jiveCoreGsonCallableFactory.createGsonCallable(refreshTokenHttpPost, TokenEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<SessionGrantEntity> fetchSessionGrant() {
        HttpGet fetchSessionGrantHttpGet = jiveCoreUnauthenticatedRequestFactory.isSessionOAuthGrantAllowed();
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchSessionGrantHttpGet, SessionGrantEntity.class);
    }

    @Nonnull
    public <T> JiveCoreCallable<T> createCallable(HttpRequestBase httpRequestBase, HttpResponseParserFactory<T> httpResponseParserFactory) {
        return jiveCoreGenericCallableFactory.createGenericCallable(httpRequestBase, httpResponseParserFactory);
    }
}
