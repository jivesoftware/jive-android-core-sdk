package com.jivesoftware.android.mobile.sdk.httpclient;

import com.jivesoftware.android.mobile.sdk.core.JiveCoreJiveClientProvider;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import javax.annotation.Nonnull;
import java.io.IOException;

public class JiveCoreRequestAddJiveClient implements HttpRequestInterceptor {
    private static final String X_JIVE_CLIENT = "X-Jive-Client";
    @Nonnull
    private final JiveCoreJiveClientProvider jiveClientProvider;

    public JiveCoreRequestAddJiveClient(@Nonnull JiveCoreJiveClientProvider jiveClientProvider) {
        this.jiveClientProvider = jiveClientProvider;
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        String base64edJiveClientJson = jiveClientProvider.getBase64edJiveClientJson();
        if (base64edJiveClientJson != null) {
            request.addHeader(X_JIVE_CLIENT, base64edJiveClientJson);
        }
    }
}
