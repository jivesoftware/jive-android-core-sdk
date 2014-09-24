package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.parser.JiveCoreExceptionFactory;
import com.jivesoftware.android.mobile.sdk.parser.PlainInputStreamHttpResponseParser;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.InputStream;

@ParametersAreNonnullByDefault
public class JiveCoreInputStreamCallableFactory {
    @Nonnull
    private final HttpClient httpClient;
    @Nonnull
    private final JiveCoreExceptionFactory jiveCoreExceptionFactory;

    public JiveCoreInputStreamCallableFactory(HttpClient httpClient, JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        this.httpClient = httpClient;
        this.jiveCoreExceptionFactory = jiveCoreExceptionFactory;
    }

    @Nonnull
    public JiveCoreCallable<InputStream> createInputStreamCallable(HttpRequestBase httpRequestBase) {
        PlainInputStreamHttpResponseParser parser = new PlainInputStreamHttpResponseParser(jiveCoreExceptionFactory);
        JiveCoreCallable<InputStream> callable = new JiveCoreCallable<InputStream>(httpRequestBase, httpClient, parser);
        return callable;
    }
}
