package com.jivesoftware.android.mobile.sdk.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jivesoftware.android.mobile.sdk.json.JiveJson;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreExceptionFactory;
import com.jivesoftware.android.mobile.sdk.parser.JiveJsonHttpResponseParser;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreJiveJsonCallableFactory {
    @Nonnull
    private final HttpClient httpClient;
    @Nonnull
    private final JiveJson jiveJson;
    @Nonnull
    private final JiveCoreExceptionFactory jiveCoreExceptionFactory;

    public JiveCoreJiveJsonCallableFactory(
            HttpClient httpClient,
            JiveJson jiveJson,
            JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        this.httpClient = httpClient;
        this.jiveJson = jiveJson;
        this.jiveCoreExceptionFactory = jiveCoreExceptionFactory;
    }

    @Nonnull
    public <E> JiveCoreCallable<E> createGsonCallable(HttpRequestBase httpRequestBase, Class<E> gsonObjectClass) {
        httpRequestBase.setHeader("Accept-Encoding", "gzip");
        JiveJsonHttpResponseParser<E> parser = new JiveJsonHttpResponseParser<E>(jiveCoreExceptionFactory, jiveJson, gsonObjectClass, null);
        JiveCoreCallable<E> callable = new JiveCoreCallable<E>(httpRequestBase, httpClient, parser);
        return callable;
    }

    @Nonnull
    public <E> JiveCoreCallable<E> createGsonCallable(HttpRequestBase httpRequestBase, TypeReference<E> gsonObjectClass) {
        httpRequestBase.setHeader("Accept-Encoding", "gzip");
        JiveJsonHttpResponseParser<E> parser = new JiveJsonHttpResponseParser<E>(jiveCoreExceptionFactory, jiveJson, null, gsonObjectClass);
        JiveCoreCallable<E> callable = new JiveCoreCallable<E>(httpRequestBase, httpClient, parser);
        return callable;
    }
}
