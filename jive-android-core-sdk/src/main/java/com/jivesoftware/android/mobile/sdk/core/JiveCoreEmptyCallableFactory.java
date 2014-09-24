package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.parser.EmptyHttpResponseParser;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreExceptionFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreEmptyCallableFactory {
    @Nonnull
    private final HttpClient httpClient;
    @Nonnull
    private final JiveCoreExceptionFactory jiveCoreExceptionFactory;

    public JiveCoreEmptyCallableFactory(HttpClient httpClient, JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        this.httpClient = httpClient;
        this.jiveCoreExceptionFactory = jiveCoreExceptionFactory;
    }

    @Nonnull
    public JiveCoreCallable<Void> createEmptyCallable(HttpRequestBase httpRequestBase) {
        httpRequestBase.setHeader("Accept-Encoding", "gzip");
        EmptyHttpResponseParser parser = new EmptyHttpResponseParser(jiveCoreExceptionFactory);
        JiveCoreCallable<Void> callable = new JiveCoreCallable<Void>(httpRequestBase, httpClient, parser);
        return callable;
    }
}
