package com.jivesoftware.android.mobile.sdk;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@ParametersAreNonnullByDefault
public class FakeHttpServer {
    private static final int TIMEOUT_MILLIS = 5000;

    public final CopyOnWriteArraySet<Throwable> throwables = new CopyOnWriteArraySet<Throwable>();

    private final CountDownLatch shouldStopCountDownLatch = new CountDownLatch(1);
    private final CountDownLatch stoppedCountDownLatch = new CountDownLatch(1);

    private volatile boolean started;

    private volatile int serverPort = 65535;
    private volatile ServerSocket serverSocket;
    private volatile HttpGet httpGet;

    private void checkStarted() {
        if (started) {
            throw new IllegalStateException("Can only start once");
        } else {
            started = true;
        }
    }

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
            throw new AssertionError("Couldn't create ServerSocket");
        }
    }

    private void createHttpGet() {
        httpGet = new HttpGet("http://localhost:" + serverPort);
        HttpParams httpGetHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpGetHttpParams, TIMEOUT_MILLIS);
        httpGet.setParams(httpGetHttpParams);
    }

    public HttpGet startServerThreadAndCompleteNormallyWhenClientConnects(final String httpStatusLine, final String[] httpHeaders, final String content) {
        checkStarted();
        createServerSocket();
        createHttpGet();

        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = serverSocket.accept();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    for (String line = bufferedReader.readLine(); (line != null) && line.length() > 0; ) {
                        line = bufferedReader.readLine();
                    }
                    PrintStream printStream = new PrintStream(socket.getOutputStream());
                    printStream.println(httpStatusLine);
                    boolean foundContentLength = false;
                    for (String httpHeader : httpHeaders) {
                        printStream.println(httpHeader);
                        if (httpHeader.startsWith("Content-Length:")) {
                            foundContentLength = true;
                        }
                    }

                    if (!foundContentLength) {
                        printStream.println("Content-Length: " + content.length());
                    }
                    printStream.print("\r\n");
                    printStream.println(content);
                    printStream.print("\r\n");

                    printStream.flush();

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

        return httpGet;
    }

    public HttpGet startServerThreadAndAbortHttpGetWhenClientConnects() {
        checkStarted();
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

        return httpGet;
    }

    public void stopServerThread() throws InterruptedException {
        shouldStopCountDownLatch.countDown();
        if (!stoppedCountDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
            throw new AssertionError("Timed out waiting for the server to stop");
        }
    }
}
