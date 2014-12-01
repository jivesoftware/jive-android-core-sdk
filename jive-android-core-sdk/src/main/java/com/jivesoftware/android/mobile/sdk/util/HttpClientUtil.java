package com.jivesoftware.android.mobile.sdk.util;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InterruptedIOException;

@ParametersAreNonnullByDefault
public class HttpClientUtil {
    public static void shutdownSafely(HttpClient httpClient) throws IOException {
        final ClientConnectionManager clientConnectionManager = httpClient.getConnectionManager();

        if (clientConnectionManager != null) {
            Thread shutdownThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // run shutdown on a background thread to guarantee that NetworkOnMainThreadException
                    // can't be thrown
                    clientConnectionManager.shutdown();
                }
            });
            shutdownThread.start();
            try {
                shutdownThread.join();
            } catch (InterruptedException e) {
                throw new InterruptedIOException();
            }
        }
    }
}
