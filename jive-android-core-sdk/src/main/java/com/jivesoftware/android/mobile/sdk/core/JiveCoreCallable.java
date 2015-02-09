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
import java.net.SocketException;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("TryWithIdenticalCatches")
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
            Throwable caught;
            int attempt = 0;
            do {
                try {
                    return callInternal();
                } catch (IOException e) {
                    if (isAndroidProblemRetryable(e)) {
                        caught = e;
                        continue;
                    }
                    throw(e);
                } catch (RuntimeException e) {
                    if (isAndroidProblemRetryable(e)) {
                        caught = e;
                        continue;
                    }
                    throw(e);
                }
            } while (++attempt < 3);
            throw new IllegalStateException("Unable to invoke callable after " + attempt + " attempts", caught);
        } else {
            throw new AlreadyCalledException();
        }
    }

    private T callInternal() throws IOException, CancellationException, JiveCoreException {
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

    private boolean isAndroidProblemRetryable(Throwable e) {
        String msg = e.getMessage();
        if (e instanceof SerializableHttpHostConnectException) {
            return false;
        } else if (e instanceof SocketException) {
            return true;
        } else if (msg == null) {
            return false;
        } else if (msg.contains("Connection already shutdown")) {
            return true;
        } else if (msg.contains("Connection must not be open")) {
            return true;
        }
        return false;
    }
}
