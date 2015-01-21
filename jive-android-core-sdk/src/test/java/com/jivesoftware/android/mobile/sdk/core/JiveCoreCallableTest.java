package com.jivesoftware.android.mobile.sdk.core;

import com.google.common.collect.ImmutableList;
import com.jivesoftware.android.httpclient.util.SerializableHttpHostConnectException;
import com.jivesoftware.android.mobile.sdk.json.JiveJson;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreExceptionFactory;
import com.jivesoftware.android.mobile.sdk.parser.PlainInputStreamHttpResponseParser;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.RequestWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.Response;
import spark.SparkJive;
import spark.route.HttpMethod;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.CancellationException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class JiveCoreCallableTest {

    private volatile DefaultHttpClient defaultHttpClient;

    @Before
    public void setup() throws Exception {
        defaultHttpClient = new DefaultHttpClient();
    }

    @After
    public void tearDown() throws Exception {
        if (defaultHttpClient != null) {
            ClientConnectionManager connectionManager = defaultHttpClient.getConnectionManager();
            if (connectionManager != null) {
                connectionManager.shutdown();
            }
        }
    }

    @Test
    public void callThrowsSerializableHttpHostConnectExceptionWhenHttpClientThrowsHttpHostConnectException() throws Exception {
        // TCP port 4 is officially unassigned, so it shouldn't be possible to connect to it
        // localhost should resolve to an actual host
        // inability to connect to a port on a known host throws ConnectException,
        // which HttpClient changes into a HttpHostConnectException
        HttpGet httpGet = new HttpGet("http://localhost:4");
        JiveCoreCallable<InputStream> testObject = new JiveCoreCallable<InputStream>(
                httpGet,
                defaultHttpClient,
                new PlainInputStreamHttpResponseParser(new JiveCoreExceptionFactory(new JiveJson())));

        try {
            testObject.call();
            fail("Expected SerializableHttpHostConnectException");
        } catch (SerializableHttpHostConnectException e) {
            // success!
        }
    }

    @Test
    public void callThrowsCancellationExceptionIfCancelledBeforeCall() throws Exception {
        HttpGet httpGet = new HttpGet("http://example.com");
        JiveCoreCallable<InputStream> testObject = new JiveCoreCallable<InputStream>(
                httpGet,
                defaultHttpClient,
                new PlainInputStreamHttpResponseParser(new JiveCoreExceptionFactory(new JiveJson())));

        boolean success = testObject.cancel();
        assertTrue(success);

        try {
            testObject.call();
            fail("Expected CancellationException");
        } catch (CancellationException e) {
            // success!
        }
    }

    @Test
    public void callingTwiceThrowsAlreadyCalledException() throws Exception {
        HttpGet httpGet = new HttpGet("http://example.com");
        JiveCoreCallable<InputStream> testObject = new JiveCoreCallable<InputStream>(
                httpGet,
                defaultHttpClient,
                new PlainInputStreamHttpResponseParser(new JiveCoreExceptionFactory(new JiveJson())));

        // just cancel this call so we don't have to deal with setting up a fake server for this test.
        boolean success = testObject.cancel();
        assertTrue(success);

        try {
            testObject.call();
            fail("Expected CancellationException");
        } catch (CancellationException e) {
            // success!
        }

        try {
            testObject.call();
            fail("Expected AlreadyCalledException");
        } catch (JiveCoreCallable.AlreadyCalledException f) {
            // success!
        }
    }

    @Test
    public void callThrowsIOExceptionFromHttpClient() throws Exception {
        // HttpClient throws UnknownHostException when it can't determine the host
        // a UUID should cause an UnknownHostException
        String uuid = UUID.randomUUID().toString();
        HttpGet httpGet = new HttpGet("http://" + uuid + ".com");
        JiveCoreCallable<InputStream> testObject = new JiveCoreCallable<InputStream>(
                httpGet,
                defaultHttpClient,
                new PlainInputStreamHttpResponseParser(new JiveCoreExceptionFactory(new JiveJson())));

        try {
            testObject.call();
            fail("Expected UnknownHostException");
        } catch (UnknownHostException e) {
            // success!
        }
    }

    @Test
    public void callThrowsIllegalStateExceptionFromHttpClient() throws Exception {
        // HttpClient throws IllegalStateException when an HttpRequest's URI
        // doesn't specify a host and the HttpClient has no default host.
        HttpGet httpGet = new HttpGet("/foo/bar");
        JiveCoreCallable<InputStream> testObject = new JiveCoreCallable<InputStream>(
                httpGet,
                defaultHttpClient,
                new PlainInputStreamHttpResponseParser(new JiveCoreExceptionFactory(new JiveJson())));

        try {
            testObject.call();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // success!
        }
    }

    @Test
    public void cancelDuringHttpClientExecuteThrowsCancellationException() throws Exception {
        final SparkJive sparkJive = new SparkJive();
        final HttpGet httpGet = new HttpGet(sparkJive.getURI());
        sparkJive.setScript(ImmutableList.of(new SparkJive.ScriptEntry(HttpMethod.get, "/",
                new SparkJive.Handler() {
                    @Override
                    public Object handle(Request request, Response response) {
                        httpGet.abort();
                        sparkJive.close();
                        return null;
                    }
                }
        )));

        JiveCoreCallable<InputStream> testObject = new JiveCoreCallable<InputStream>(
                httpGet,
                defaultHttpClient,
                new PlainInputStreamHttpResponseParser(new JiveCoreExceptionFactory(new JiveJson())));
        try {
            testObject.call();
            fail("Should have thrown CancellationException");
        } catch (CancellationException e) {
            // success!
        }

        assertTrue(sparkJive.isScriptComplete());
    }

    @Test
    public void cancelReturnsFalseWhenHttpRequestAbortThrowsUnsupportedOperationException() throws Exception {
        HttpGet httpGet = new HttpGet("http://example.com");
        // RequestWrapper#abort throws UnsupportedOperationException
        RequestWrapper requestWrapper = new RequestWrapper(httpGet);
        JiveCoreCallable<InputStream> testObject = new JiveCoreCallable<InputStream>(
                requestWrapper,
                defaultHttpClient,
                new PlainInputStreamHttpResponseParser(new JiveCoreExceptionFactory(new JiveJson())));

        boolean success = testObject.cancel();
        assertFalse(success);
    }
}
