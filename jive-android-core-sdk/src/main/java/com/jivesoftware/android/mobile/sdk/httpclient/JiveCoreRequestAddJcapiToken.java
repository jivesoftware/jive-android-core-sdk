package com.jivesoftware.android.mobile.sdk.httpclient;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.ProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecRegistry;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JiveCoreRequestAddJcapiToken implements HttpRequestInterceptor {
    private static final String X_JCAPI_TOKEN = "X-JCAPI-Token";

    @Override
    public void process(final HttpRequest request, final HttpContext context)
            throws HttpException, IOException {
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        }

        String method = request.getRequestLine().getMethod();
        if (!method.equalsIgnoreCase("POST")) {
            return;
        }

        // Obtain cookie store
        CookieStore cookieStore = (CookieStore) context.getAttribute(
                ClientContext.COOKIE_STORE);
        if (cookieStore == null) {
            return;
        }

        // Obtain the registry of cookie specs
        CookieSpecRegistry registry = (CookieSpecRegistry) context.getAttribute(
                ClientContext.COOKIESPEC_REGISTRY);
        if (registry == null) {
            return;
        }

        // Obtain the target host (required)
        HttpHost targetHost = (HttpHost) context.getAttribute(
                ExecutionContext.HTTP_TARGET_HOST);
        if (targetHost == null) {
            throw new IllegalStateException("Target host not specified in HTTP context");
        }

        // Obtain the client connection (required)
        ManagedClientConnection conn = (ManagedClientConnection) context.getAttribute(
                ExecutionContext.HTTP_CONNECTION);
        if (conn == null) {
            throw new IllegalStateException("Client connection not specified in HTTP context");
        }

        String policy = HttpClientParams.getCookiePolicy(request.getParams());

        URI requestURI;
        if (request instanceof HttpUriRequest) {
            requestURI = ((HttpUriRequest) request).getURI();
        } else {
            try {
                requestURI = new URI(request.getRequestLine().getUri());
            } catch (URISyntaxException ex) {
                throw new ProtocolException("Invalid request URI: " +
                        request.getRequestLine().getUri(), ex);
            }
        }

        String hostName = targetHost.getHostName();
        int port = targetHost.getPort();
        if (port < 0) {

            // Obtain the scheme registry
            SchemeRegistry sr = (SchemeRegistry) context.getAttribute(
                    ClientContext.SCHEME_REGISTRY);
            if (sr != null) {
                Scheme scheme = sr.get(targetHost.getSchemeName());
                port = scheme.resolvePort(port);
            } else {
                port = conn.getRemotePort();
            }
        }

        CookieOrigin cookieOrigin = new CookieOrigin(
                hostName,
                port,
                requestURI.getPath(),
                conn.isSecure());

        // Get an instance of the selected cookie policy
        CookieSpec cookieSpec = registry.getCookieSpec(policy, request.getParams());
        // Get all cookies available in the HTTP state
        List<Cookie> cookies = new ArrayList<Cookie>(cookieStore.getCookies());
        String jcapiToken = null;

        Date now = new Date();
        for (Cookie cookie : cookies) {
            if (!cookie.isExpired(now)) {
                if (cookieSpec.match(cookie, cookieOrigin)) {
                    if (X_JCAPI_TOKEN.equals(cookie.getName())) {
                        jcapiToken = cookie.getValue();
                    }
                }
            }
        }

        if (jcapiToken != null) {
            request.addHeader(X_JCAPI_TOKEN, jcapiToken);
        }
    }
}
