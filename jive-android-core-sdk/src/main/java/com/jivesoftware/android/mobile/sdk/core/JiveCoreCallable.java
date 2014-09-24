package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.parser.HttpResponseParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;

public class JiveCoreCallable<T> implements Callable<T> {
    @Nonnull
    private final HttpRequestBase httpRequestBase;
    @Nonnull
    private final HttpClient httpClient;
    @Nonnull
    private final HttpResponseParser<T> httpResponseParser;
    @Nonnull
    private final AtomicBoolean calledAtomicBoolean = new AtomicBoolean();

    public JiveCoreCallable(
            @Nonnull HttpRequestBase httpRequestBase,
            @Nonnull HttpClient httpClient,
            @Nonnull HttpResponseParser<T> httpResponseParser) {
        this.httpRequestBase = httpRequestBase;
        this.httpClient = httpClient;
        this.httpResponseParser = httpResponseParser;
    }

    @Override
    public T call() throws IOException, CancellationException {
        if (calledAtomicBoolean.compareAndSet(false, true)) {
            HttpResponse httpResponse;
            try {
                httpResponse = httpClient.execute(httpRequestBase);
            } catch (IOException e) {
                // HttpClient throws an IOException if HttpRequestBase is aborted during execution
                if (httpRequestBase.isAborted()) {
                    throw new CancellationException();
                } else {
                    throw e;
                }
            } catch (IllegalStateException e) {
                // HttpClient throws an IllegalStateException if HttpRequestBase is aborted before execution.
                if (httpRequestBase.isAborted()) {
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
        return httpRequestBase.getURI();
    }

    public void abort() {
        httpRequestBase.abort();
    }

    @Override
    public String toString() {
        String string =
                httpRequestBase.getMethod() + " "
                        + httpRequestBase.getURI() + " "
                        + (httpRequestBase.isAborted() ? "aborted" : "not aborted") + " "
                        + (calledAtomicBoolean.get() ? "called" : "not called");
        return string;
    }

    public static class AlreadyCalledException extends RuntimeException {
        public AlreadyCalledException() {
            super("Already called");
        }
    }
}
