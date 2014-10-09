package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.httpclient.util.SerializableHttpHostConnectException;
import com.jivesoftware.android.mobile.sdk.gson.JiveGson;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreExceptionFactory;
import com.jivesoftware.android.mobile.sdk.parser.PlainInputStreamHttpResponseParser;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
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
                new PlainInputStreamHttpResponseParser(new JiveCoreExceptionFactory(new JiveGson())));

        try {
            testObject.call();
            fail("Expected SerializableHttpHostConnectException");
        } catch (SerializableHttpHostConnectException e) {
            // success!
        }
    }

    @Test
    public void callThrowsCancellationExceptionIfAbortedBeforeCall() throws Exception {
        HttpGet httpGet = new HttpGet("http://example.com");
        JiveCoreCallable<InputStream> testObject = new JiveCoreCallable<InputStream>(
                httpGet,
                defaultHttpClient,
                new PlainInputStreamHttpResponseParser(new JiveCoreExceptionFactory(new JiveGson())));

        testObject.abort();

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
                new PlainInputStreamHttpResponseParser(new JiveCoreExceptionFactory(new JiveGson())));

        // just abort this call so we don't have to deal with setting up a fake server for this test.
        testObject.abort();

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
                new PlainInputStreamHttpResponseParser(new JiveCoreExceptionFactory(new JiveGson())));

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
                new PlainInputStreamHttpResponseParser(new JiveCoreExceptionFactory(new JiveGson())));

        try {
            testObject.call();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // success!
        }
    }

    @Test
    public void abortDuringHttpClientExecuteThrowsCancellationException() throws Exception {
        @ParametersAreNonnullByDefault
        class TestContext {
            final int TIMEOUT_MILLIS = 5000;

            final CopyOnWriteArraySet<Throwable> throwables = new CopyOnWriteArraySet<Throwable>();
            final CountDownLatch shouldStopCountDownLatch = new CountDownLatch(1);
            final CountDownLatch stoppedCountDownLatch = new CountDownLatch(1);

            volatile int serverPort = 65535;
            volatile ServerSocket serverSocket;
            volatile HttpGet httpGet;

            private void createServerSocket() {
                while (serverPort > 1024) {
                    try {
                        serverSocket = new ServerSocket(serverPort);
                        break;
                    } catch (IOException e) {
                        serverPort--;
                    }
                }

                if (serverSocket == null) {
                    fail("Couldn't create ServerSocket");
                }
            }

            private void createHttpGet() {
                httpGet = new HttpGet("http://localhost:" + serverPort);
                HttpParams httpGetHttpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpGetHttpParams, TIMEOUT_MILLIS);
                httpGet.setParams(httpGetHttpParams);
            }

            public void startServerThreadAndAbortHttpGetWhenClientConnects() {
                createServerSocket();
                createHttpGet();

                Thread serverThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = serverSocket.accept();
                            httpGet.abort();
                            shouldStopCountDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
                            try {
                                socket.close();
                            } catch (IOException e) {
                                // ignore
                            }
                            try {
                                serverSocket.close();
                            } catch (IOException e) {
                                // ignore
                            }
                        } catch (Exception e) {
                            throwables.add(e);
                        }
                        stoppedCountDownLatch.countDown();
                    }
                });
                serverThread.setDaemon(true);
                serverThread.start();
            }

            public void stopServerThread() throws InterruptedException {
                shouldStopCountDownLatch.countDown();
                assertTrue(stoppedCountDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS));
            }
        }

        final TestContext testContext = new TestContext();
        testContext.startServerThreadAndAbortHttpGetWhenClientConnects();

        JiveCoreCallable<InputStream> testObject = new JiveCoreCallable<InputStream>(
                testContext.httpGet,
                defaultHttpClient,
                new PlainInputStreamHttpResponseParser(new JiveCoreExceptionFactory(new JiveGson())));
        try {
            testObject.call();
        } catch (CancellationException e) {
            // success!
        }

        testContext.stopServerThread();

        assertEquals(Collections.<Throwable>emptySet(), testContext.throwables);
    }
}
