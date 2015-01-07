package com.jivesoftware.android.mobile.sdk.core;

import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.google.common.io.LineProcessor;
import com.jivesoftware.android.mobile.sdk.entity.ContentEntity;
import com.jivesoftware.android.mobile.sdk.entity.VersionEntity;
import com.jivesoftware.android.mobile.sdk.json.JiveJson;
import com.jivesoftware.android.mobile.sdk.parser.EmptyHttpResponseParser;
import com.jivesoftware.android.mobile.sdk.parser.HttpResponseParser;
import com.jivesoftware.android.mobile.sdk.parser.InputStreamHttpResponseParser;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreAPIException;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreExceptionFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.zip.GZIPOutputStream;

import static com.jivesoftware.android.mobile.matcher.IterableMatchers.containsAll;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ErrorEntityMatchers.errorAPIErrorCode;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ErrorEntityMatchers.errorDescription;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ErrorEntityMatchers.errorErrorCode;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class GzipITest extends TestEndpoint {
    private int port;
    private volatile ServerSocket serverSocket;
    private ExecutorService serverSocketExecutorService;
    private URL baseURL;
    private DefaultHttpClient httpClient;
    private JiveJson jiveJson;
    private String oauthCredentials;
    private String oauthAddOnUUID;
    private JiveCore jiveCore;
    private JiveCoreUnauthenticated jiveCoreUnauthenticated;

    @Before
    public void setup() throws Exception {
        oauthCredentials = "oauthCredentials";
        oauthAddOnUUID = "oauthAddOnUUID";
        jiveJson = new JiveJson();
        IOException lastIOException = null;
        for (int i = 0; i < 10; i++) {
            // opening and closing the same server socket quickly leads to weird IOExceptions
            // better to jump around.
            port = ((int) (Math.random() * (65535 - 1024))) + 1024;
            try {
                serverSocket = new ServerSocket(port);
                break;
            } catch (IOException e) {
                lastIOException = e;
            }
        }
        if (serverSocket == null) {
            if (lastIOException == null) {
                throw new AssertionError("Couldn't create serverSocket, no IOException to throw");
            } else {
                throw lastIOException;
            }
        }
        baseURL = new URL("http://localhost:" + port);
        httpClient = new DefaultHttpClient();

        serverSocketExecutorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(@Nonnull Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    @After
    public void tearDown() throws IOException {
        if (serverSocketExecutorService != null) {
            serverSocketExecutorService.shutdown();
        }
        if (jiveCore != null) {
            jiveCore.close();
            jiveCore = null;
        }
        if (jiveCoreUnauthenticated != null) {
            jiveCoreUnauthenticated.close();
            jiveCoreUnauthenticated = null;
        }
    }

    @Test
    public void jiveCoreUnauthenticatedIncludesAcceptGzipEncodingHeaderAndAcceptsGzipEncoding() throws Exception {
        Future<List<String>> requestLinesFuture = serverSocketExecutorService.submit(new AlwaysGzipHttpServer(serverSocket, "version.json"));

        jiveCoreUnauthenticated = new JiveCoreUnauthenticated(baseURL, oauthCredentials, oauthAddOnUUID, httpClient, jiveJson);
        JiveCoreCallable<VersionEntity> fetchVersionCallable = jiveCoreUnauthenticated.fetchVersion();
        VersionEntity versionEntity = fetchVersionCallable.call();
        assertNotNull(versionEntity);

        List<String> requestLines = requestLinesFuture.get(TIMEOUT_AMOUNT, TIMEOUT_TIME_UNIT);
        assertThat(requestLines, hasItem("Accept-Encoding: gzip"));

    }

    @Test
    public void jiveCoreIncludesAcceptGzipEncodingHeaderAndAcceptsGzipEncoding() throws Exception {
        Future<List<String>> requestLinesFuture = serverSocketExecutorService.submit(new AlwaysGzipHttpServer(serverSocket, "direct-message.json"));

        jiveCore = new JiveCore(new JiveCoreRequestFactory(oauthCredentials, baseURL, jiveJson), httpClient, jiveJson);
        JiveCoreCallable<ContentEntity> fetchContentCallable = jiveCore.fetchContent("/foo", new JiveCoreRequestOptions());
        ContentEntity contentEntity = fetchContentCallable.call();
        assertNotNull(contentEntity);

        List<String> requestLines = requestLinesFuture.get(TIMEOUT_AMOUNT, TIMEOUT_TIME_UNIT);
        assertThat(requestLines, hasItem("Accept-Encoding: gzip"));
    }

    @Test
    public void inputStreamHttpResponseParserAcceptsUnexpectedGzip() throws Exception {
        // sending a gzip when we don't ask for one is illegal HTTP, but
        // we should handle this case because some clients' servers are misconfigured
        // http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.3

        Future<List<String>> requestLinesFuture = serverSocketExecutorService.submit(new AlwaysGzipHttpServer(serverSocket, "version.json"));

        jiveCoreUnauthenticated = new JiveCoreUnauthenticated(baseURL, oauthCredentials, oauthAddOnUUID, httpClient, jiveJson);
        JiveCoreCallable<InputStream> inputStreamCallable = jiveCoreUnauthenticated.createCallable(new HttpGet(baseURL.toURI()), new HttpResponseParserFactory<InputStream>() {
            @Nonnull
            @Override
            public HttpResponseParser<InputStream> createHttpResponseParser(@Nonnull JiveCoreExceptionFactory jiveCoreExceptionFactory) {
                return new InputStreamHttpResponseParser<InputStream>(jiveCoreExceptionFactory) {
                    @Nullable
                    @Override
                    protected InputStream parseContentInputStreamedResponse(
                            @Nonnull HttpResponse httpResponse,
                            int statusCode,
                            @Nonnull HttpEntity httpEntity,
                            @Nonnull InputStream contentInputStream) throws IOException {
                        return contentInputStream;
                    }
                };
            }
        });
        InputStream inputStream = inputStreamCallable.call();
        String actualVersionJson = CharStreams.toString(new InputStreamReader(inputStream));
        inputStream.close();

        InputStream expectedVersionJsonInputStream = new FileInputStream("test-data/version.json");
        String expectedVersionJson = CharStreams.toString(new InputStreamReader(expectedVersionJsonInputStream));
        expectedVersionJsonInputStream.close();

        assertEquals(expectedVersionJson, actualVersionJson);

        List<String> requestLines = requestLinesFuture.get(TIMEOUT_AMOUNT, TIMEOUT_TIME_UNIT);
        assertThat(requestLines, containsAll(not(containsString("gzip"))));
    }

    @Test
    public void emptyHttpResponseParserAcceptsUnexpectedGzip() throws Exception {
        // sending a gzip when we don't ask for one is illegal HTTP, but
        // we should handle this case because some clients' servers are misconfigured
        // http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.3

        Future<List<String>> requestLinesFuture = serverSocketExecutorService.submit(new AlwaysGzipHttpServer(serverSocket, "version.json"));

        jiveCoreUnauthenticated = new JiveCoreUnauthenticated(baseURL, oauthCredentials, oauthAddOnUUID, httpClient, jiveJson);
        JiveCoreCallable<Void> emptyCallable = jiveCoreUnauthenticated.createCallable(new HttpGet(baseURL.toURI()), new HttpResponseParserFactory<Void>() {
            @Nonnull
            @Override
            public HttpResponseParser<Void> createHttpResponseParser(@Nonnull JiveCoreExceptionFactory jiveCoreExceptionFactory) {
                return new EmptyHttpResponseParser(jiveCoreExceptionFactory);
            }
        });
        Void result = emptyCallable.call();
        assertNull(result);

        List<String> requestLines = requestLinesFuture.get(TIMEOUT_AMOUNT, TIMEOUT_TIME_UNIT);
        assertThat(requestLines, containsAll(not(containsString("gzip"))));
    }

    @Test
    public void emptyHttpResponseParserAcceptsUnexpectedGzipInErrorResponse() throws Exception {
        Future<List<String>> requestLinesFuture = serverSocketExecutorService.submit(new AlwaysGzipHttpServer(serverSocket, "bad-request.json", "400 Bad Request"));

        jiveCoreUnauthenticated = new JiveCoreUnauthenticated(baseURL, oauthCredentials, oauthAddOnUUID, httpClient, jiveJson);
        JiveCoreCallable<Void> emptyCallable = jiveCoreUnauthenticated.createCallable(new HttpGet(baseURL.toURI()), new HttpResponseParserFactory<Void>() {
            @Nonnull
            @Override
            public HttpResponseParser<Void> createHttpResponseParser(@Nonnull JiveCoreExceptionFactory jiveCoreExceptionFactory) {
                return new EmptyHttpResponseParser(jiveCoreExceptionFactory);
            }
        });
        try {
            emptyCallable.call();
        } catch (JiveCoreAPIException e) {
            assertThat(e.errorEntity, allOf(
                    errorDescription("Invalid filter expression place(/api/core/v3/places/2523,/api/core/v3/places/2524)"),
                    errorAPIErrorCode("filterInvalid"),
                    errorErrorCode(400)));
        }

        List<String> requestLines = requestLinesFuture.get(TIMEOUT_AMOUNT, TIMEOUT_TIME_UNIT);
        assertThat(requestLines, containsAll(not(containsString("gzip"))));
    }

    @Test
    public void jiveCoreIncludesAcceptGzipEncodingHeaderAndAcceptsGzipEncodingInErrorResponse() throws Exception {
        Future<List<String>> requestLinesFuture = serverSocketExecutorService.submit(new AlwaysGzipHttpServer(serverSocket, "bad-request.json", "400 Bad Request"));

        jiveCore = new JiveCore(new JiveCoreRequestFactory(oauthCredentials, baseURL, jiveJson), httpClient, jiveJson);
        JiveCoreCallable<ContentEntity> fetchContentCallable = jiveCore.fetchContent("/foo", new JiveCoreRequestOptions());

        try {
            fetchContentCallable.call();
        } catch (JiveCoreAPIException e) {
            assertThat(e.errorEntity, allOf(
                    errorDescription("Invalid filter expression place(/api/core/v3/places/2523,/api/core/v3/places/2524)"),
                    errorAPIErrorCode("filterInvalid"),
                    errorErrorCode(400)));
        }

        List<String> requestLines = requestLinesFuture.get(TIMEOUT_AMOUNT, TIMEOUT_TIME_UNIT);
        assertThat(requestLines, hasItem("Accept-Encoding: gzip"));
    }

    private static class AlwaysGzipHttpServer implements Callable<List<String>> {
        private final ServerSocket serverSocket;
        private final String filename;
        private final String statusLine;

        public AlwaysGzipHttpServer(ServerSocket serverSocket, String filename) {
            this(serverSocket, filename, "200 OK");
        }

        public AlwaysGzipHttpServer(ServerSocket serverSocket, String filename, String statusLine) {
            this.serverSocket = serverSocket;
            this.filename = filename;
            this.statusLine = statusLine;
        }

        @Override
        public List<String> call() throws Exception {
            Socket socket = serverSocket.accept();
            List<String> requestLines = CharStreams.readLines(new InputStreamReader(socket.getInputStream()), new LineProcessor<List<String>>() {
                private final ArrayList<String> lines = new ArrayList<String>();

                @Override
                public boolean processLine(@Nonnull String line) throws IOException {
                    if (line.length() > 0) {
                        lines.add(line);
                        return true;
                    } else {
                        return false;
                    }
                }

                @Override
                public List<String> getResult() {
                    return lines;
                }
            });
            File file = new File("test-data/" + filename);

            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter((outputStream)));
            printWriter.println("HTTP/1.1 " + statusLine);
            printWriter.println("Content-Type: application/json");
            printWriter.println("Content-Length: " + file.length());
            printWriter.println("Content-Encoding: gzip");
            printWriter.println();
            printWriter.flush();

            FileInputStream fileInputStream = new FileInputStream(file);
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
            ByteStreams.copy(fileInputStream, gzipOutputStream);
            fileInputStream.close();
            gzipOutputStream.close();
            socket.close();
            serverSocket.close();
            return requestLines;
        }
    }
}
