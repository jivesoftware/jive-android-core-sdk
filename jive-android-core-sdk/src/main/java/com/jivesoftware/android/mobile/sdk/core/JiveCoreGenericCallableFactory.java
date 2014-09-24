package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.parser.HttpResponseParser;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreExceptionFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreGenericCallableFactory {
    @Nonnull
    private final HttpClient httpClient;
    @Nonnull
    private final JiveCoreExceptionFactory jiveCoreExceptionFactory;

    public JiveCoreGenericCallableFactory(HttpClient httpClient, JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        this.httpClient = httpClient;
        this.jiveCoreExceptionFactory = jiveCoreExceptionFactory;
    }

    public <T> JiveCoreCallable<T> createGenericCallable(HttpRequestBase httpRequestBase, HttpResponseParserFactory<T> httpResponseParserFactory) {
        HttpResponseParser<T> httpResponseParser = httpResponseParserFactory.createHttpResponseParser(jiveCoreExceptionFactory);
        return new JiveCoreCallable<T>(httpRequestBase, httpClient, httpResponseParser);
    }
}
