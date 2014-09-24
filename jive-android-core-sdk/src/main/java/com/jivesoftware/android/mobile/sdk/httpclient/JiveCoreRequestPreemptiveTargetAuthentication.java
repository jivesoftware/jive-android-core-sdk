package com.jivesoftware.android.mobile.sdk.httpclient;

import com.jivesoftware.android.mobile.sdk.core.JiveCoreTokenEntityRefresher;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import javax.annotation.Nonnull;
import java.io.IOException;

public class JiveCoreRequestPreemptiveTargetAuthentication implements HttpRequestInterceptor {
    @Nonnull
    private final JiveCoreTokenEntityRefresher jiveCoreTokenEntityRefresher;

    public JiveCoreRequestPreemptiveTargetAuthentication(@Nonnull JiveCoreTokenEntityRefresher jiveCoreTokenEntityRefresher) {
        this.jiveCoreTokenEntityRefresher = jiveCoreTokenEntityRefresher;
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);

        // If no auth scheme avaialble yet, try to initialize it
        // preemptively
        AuthScheme authScheme = authState.getAuthScheme();
        if (authScheme == null) {
            authScheme = new JiveCoreAuthScheme(jiveCoreTokenEntityRefresher);
            authState.setAuthScheme(authScheme);

            CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
            HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
            Credentials creds = authState.getCredentials();
            if (creds == null) {
                creds = credsProvider.getCredentials(authScope);
            } else {
                if (authScheme.isComplete()) {
                    creds = null;
                }
            }

            authState.setAuthScope(authScope);
            authState.setCredentials(creds);
        }
    }
}
