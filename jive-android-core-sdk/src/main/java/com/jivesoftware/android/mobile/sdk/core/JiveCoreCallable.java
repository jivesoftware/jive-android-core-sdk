package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.httpclient.util.SerializableHttpHostConnectException;
import com.jivesoftware.android.mobile.sdk.parser.HttpResponseParser;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreException;
import com.jivesoftware.android.mobile.sdk.util.Cancelable;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpHostConnectException;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;

@ParametersAreNonnullByDefault
public class JiveCoreCallable<T> implements Callable<T>, Cancelable {
    @Nonnull
    private final HttpUriRequest httpUriRequest;
    @Nonnull
    private final HttpClient httpClient;
    @Nonnull
    private final HttpResponseParser<T> httpResponseParser;
    @Nonnull
    private final AtomicBoolean calledAtomicBoolean = new AtomicBoolean();

    public JiveCoreCallable(
            HttpUriRequest httpUriRequest,
            HttpClient httpClient,
            HttpResponseParser<T> httpResponseParser) {
        this.httpUriRequest = httpUriRequest;
        this.httpClient = httpClient;
        this.httpResponseParser = httpResponseParser;
    }

    @Override
    public T call() throws IOException, CancellationException, JiveCoreException {
        if (calledAtomicBoolean.compareAndSet(false, true)) {
            HttpResponse httpResponse;
            try {
                try {
                    httpResponse = httpClient.execute(httpUriRequest);
                } catch (HttpHostConnectException httpHostConnectException) {
                    // https://code.google.com/p/android/issues/detail?id=55371
                    throw new SerializableHttpHostConnectException(httpHostConnectException);
                }
            } catch (IOException e) {
                // HttpClient throws an IOException if HttpRequestBase is aborted during execution
                if (httpUriRequest.isAborted()) {
                    throw new CancellationException();
                } else {
                    throw e;
                }
            } catch (IllegalStateException e) {
                // HttpClient throws an IllegalStateException if HttpRequestBase is aborted before execution.
                if (httpUriRequest.isAborted()) {
                    throw new CancellationException();
                } else {
                    throw e;
                }
            }

            T result = httpResponseParser.parseResponse(httpResponse);
            return result;
        } else {
            throw new AlreadyCalledException();
        }
    }

    public URI getRequestURI() {
        return httpUriRequest.getURI();
    }

    @Override
    public boolean cancel() {
        try {
            httpUriRequest.abort();
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        String string =
                httpUriRequest.getMethod() + " "
                        + httpUriRequest.getURI() + " "
                        + (httpUriRequest.isAborted() ? "aborted" : "not aborted") + ", "
                        + (calledAtomicBoolean.get() ? "called" : "not called");
        return string;
    }

    public static class AlreadyCalledException extends RuntimeException {
        public AlreadyCalledException() {
            super("Already called");
        }
    }
}
