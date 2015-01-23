package com.jivesoftware.android.mobile.sdk.httpclient;

import com.jivesoftware.android.mobile.sdk.core.JiveCoreTokenEntityStore;
import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JiveCoreSingleCredentialsProvider implements CredentialsProvider {
    @Nonnull
    private final JiveCoreTokenEntityStore tokenEntityStore;
    @Nonnull
    private final Object credentialsMonitor = new Object();

    private TokenEntityCredentials credentials;

    public JiveCoreSingleCredentialsProvider(@Nonnull JiveCoreTokenEntityStore tokenEntityStore) {
        this.tokenEntityStore = tokenEntityStore;
    }

    @Override
    public void setCredentials(AuthScope authscope, Credentials credentials) {
        synchronized (credentialsMonitor) {
            // ClassCastException is ok
            this.credentials = (TokenEntityCredentials) credentials;
        }
    }

    @Override
    @Nullable
    public TokenEntityCredentials getCredentials(AuthScope authscope) {
        synchronized (credentialsMonitor) {
            if (credentials == null) {
                // intentionally invoke callback within the lock so that only one thread may call
                // getTokenEntity at once.
                TokenEntity tokenEntity;
                try {
                    tokenEntity = tokenEntityStore.getTokenEntity();
                } catch (Exception e) {
                    // can't rethrow this auth exception. It will crash HttpClient.
                    tokenEntity = null;
                }
                if (tokenEntity != null) {
                    credentials = new TokenEntityCredentials(tokenEntity);
                }
            }
        }

        return credentials;
    }

    @Override
    public void clear() {
        synchronized (credentialsMonitor) {
            credentials = null;
        }
    }
}
