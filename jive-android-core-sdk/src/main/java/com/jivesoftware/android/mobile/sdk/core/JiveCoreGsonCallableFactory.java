package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.gson.JiveGson;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreExceptionFactory;
import com.jivesoftware.android.mobile.sdk.parser.JiveGsonHttpResponseParser;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreGsonCallableFactory {
    @Nonnull
    private final HttpClient httpClient;
    @Nonnull
    private final JiveGson jiveGson;
    @Nonnull
    private final JiveCoreExceptionFactory jiveCoreExceptionFactory;

    public JiveCoreGsonCallableFactory(
            HttpClient httpClient,
            JiveGson jiveGson,
            JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        this.httpClient = httpClient;
        this.jiveGson = jiveGson;
        this.jiveCoreExceptionFactory = jiveCoreExceptionFactory;
    }

    @Nonnull
    public <E> JiveCoreCallable<E> createGsonCallable(HttpRequestBase httpRequestBase, Class<E> gsonObjectClass) {
        httpRequestBase.setHeader("Accept-Encoding", "gzip");
        JiveGsonHttpResponseParser<E> parser = new JiveGsonHttpResponseParser<E>(jiveCoreExceptionFactory, jiveGson, gsonObjectClass);
        JiveCoreCallable<E> callable = new JiveCoreCallable<E>(httpRequestBase, httpClient, parser);
        return callable;
    }
}
