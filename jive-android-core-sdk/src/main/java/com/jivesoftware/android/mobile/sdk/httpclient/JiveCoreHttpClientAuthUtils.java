package com.jivesoftware.android.mobile.sdk.httpclient;

import com.jivesoftware.android.mobile.sdk.core.JiveCoreConstants;
import com.jivesoftware.android.mobile.sdk.core.JiveCoreJiveClientProvider;
import com.jivesoftware.android.mobile.sdk.core.JiveCoreTokenEntityRefresher;
import com.jivesoftware.android.mobile.sdk.core.JiveCoreTokenEntityStore;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.params.HttpParams;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JiveCoreHttpClientAuthUtils {

    /**
     * @return the given HttpClient so this can be called inline in a constructor.
     */
    @Nonnull
    public static <H extends AbstractHttpClient> H initHttpClientAuth(
            @Nonnull H httpClient,
            @Nonnull JiveCoreTokenEntityStore tokenEntityStore,
            @Nonnull final JiveCoreTokenEntityRefresher tokenEntityRefresher,
            @Nullable JiveCoreJiveClientProvider jiveClientProvider) {
        // must use zero index so that JiveCoreRequestPreemptiveTargetAuthentication runs before HttpClient's RequestTargetAuthentication
        httpClient.addRequestInterceptor(new JiveCoreRequestPreemptiveTargetAuthentication(tokenEntityRefresher), 0);
        httpClient.addRequestInterceptor(new JiveCoreRequestAddJcapiToken());
        if (jiveClientProvider != null) {
            httpClient.addRequestInterceptor(new JiveCoreRequestAddJiveClient(jiveClientProvider));
        }
        httpClient.getAuthSchemes().register(JiveCoreConstants.JIVE_CORE_AUTH_SCHEME_NAME, new AuthSchemeFactory() {
            @Override
            public AuthScheme newInstance(HttpParams params) {
                return new JiveCoreAuthScheme(tokenEntityRefresher);
            }
        });
        httpClient.setCredentialsProvider(new JiveCoreSingleCredentialsProvider(tokenEntityStore));
        httpClient.setTargetAuthenticationHandler(new JiveCoreAuthenticationHandler());

        return httpClient;
    }
}
