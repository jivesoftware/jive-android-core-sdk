package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreLoginRequiredException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.fail;

public class JiveCoreOAuthAuthenticationFailureITest extends TestEndpoint {
    private TestTokenEntityStore testTokenEntityStore;
    private TestTokenEntityRefresher testTokenEntityRefresher;

    private DefaultHttpClient jiveCoreUnauthenticatedDefaultHttpClient;
    private JiveCoreUnauthenticated jiveCoreUnauthenticated;

    private DefaultHttpClient jiveCoreDefaultHttpClient;
    private JiveCore jiveCore;

    @Before
    public void setup() throws Exception {
        testTokenEntityStore = new TestTokenEntityStore();
        testTokenEntityRefresher = new TestTokenEntityRefresher();

        jiveCoreUnauthenticatedDefaultHttpClient = new DefaultHttpClient();
        jiveCoreUnauthenticated = new JiveCoreUnauthenticated(TEST_URL, jiveCoreUnauthenticatedDefaultHttpClient);

        jiveCoreDefaultHttpClient = new DefaultHttpClient();
        jiveCore = new JiveCore(TEST_URL, jiveCoreDefaultHttpClient, testTokenEntityStore, testTokenEntityRefresher);
    }

    @After
    public void tearDown() throws Exception {
        if (jiveCoreDefaultHttpClient != null) {
            jiveCoreDefaultHttpClient.getConnectionManager().shutdown();
            jiveCoreDefaultHttpClient = null;
        }
    }

    @Test
    public void testJiveCoreCallableThrowsLoginRequiredExceptionWhenNoTokenEntityExists() throws Exception {
        try {
            jiveCore.fetchMePerson().call();
            fail();
        } catch (JiveCoreLoginRequiredException e) {
            // success
        }
    }

    @Test
    public void testJiveCoreCallableThrowsLoginRequiredExceptionWhenTokenEntityStoreThrowsIOException() throws Exception {
        testTokenEntityStore.tokenEntityOrIOExceptionQueue.add(new TokenEntityOrIOException(new IOException()));
        try {
            jiveCore.fetchMePerson().call();
            fail();
        } catch (JiveCoreLoginRequiredException e) {
            // success
        }
    }

    @Test
    public void testJiveCoreCallableThrowsLoginRequiredExceptionWhenAuthorizeDeviceUsesWrongCredentials() throws Exception {
        try {
            jiveCoreUnauthenticated.authorizeDevice(ADMIN.username, "bad_password").call();
        } catch (JiveCoreLoginRequiredException e) {
            // success
        }
    }

    @Test
    public void testJiveCoreCallableThrowsLoginRequiredExceptionWhenTokenEntityStoreReturnsInvalidTokenEntity() throws Exception {
        TokenEntity tokenEntity = jiveCoreUnauthenticated.authorizeDevice(ADMIN.username, ADMIN.password).call();
        jiveCoreUnauthenticated.deauthorizeDevice(tokenEntity).call();
        testTokenEntityStore.tokenEntityOrIOExceptionQueue.add(new TokenEntityOrIOException(tokenEntity));

        try {
            jiveCore.fetchMePerson().call();
            fail();
        } catch (JiveCoreLoginRequiredException e) {
            // success
        }
    }

    @Test
    public void testJiveCoreCallableRequestsASecondTokenEntityIfFirstFails() throws Exception {
        TokenEntity tokenEntity = jiveCoreUnauthenticated.authorizeDevice(ADMIN.username, ADMIN.password).call();
        TokenEntity brokenTokenEntity = new TokenEntity();
        brokenTokenEntity.accessToken = tokenEntity.accessToken + "_broken";
        brokenTokenEntity.expiresIn = tokenEntity.expiresIn;
        brokenTokenEntity.refreshToken = tokenEntity.refreshToken;
        brokenTokenEntity.tokenType = tokenEntity.tokenType;

        testTokenEntityStore.tokenEntityOrIOExceptionQueue.add(new TokenEntityOrIOException(brokenTokenEntity));
        testTokenEntityRefresher.tokenEntityOrIOExceptionQueue.add(new TokenEntityOrIOException(tokenEntity));

        jiveCore.fetchMePerson().call();
    }

    @Test
    public void testJiveCoreCallableRequestsThrowsLoginRequiredExceptionWhenTokenEntityRefresherThrowsIOException() throws Exception {
        TokenEntity tokenEntity = jiveCoreUnauthenticated.authorizeDevice(ADMIN.username, ADMIN.password).call();
        TokenEntity brokenTokenEntity = new TokenEntity();
        brokenTokenEntity.accessToken = tokenEntity.accessToken + "_broken";
        brokenTokenEntity.expiresIn = tokenEntity.expiresIn;
        brokenTokenEntity.refreshToken = tokenEntity.refreshToken;
        brokenTokenEntity.tokenType = tokenEntity.tokenType;

        testTokenEntityStore.tokenEntityOrIOExceptionQueue.add(new TokenEntityOrIOException(brokenTokenEntity));
        testTokenEntityRefresher.tokenEntityOrIOExceptionQueue.add(new TokenEntityOrIOException(new IOException()));

        try {
            jiveCore.fetchMePerson().call();
            fail();
        } catch (JiveCoreLoginRequiredException e) {
            // success!
        }
    }

    private static class TokenEntityOrIOException {
        @Nullable
        public final TokenEntity tokenEntity;
        @Nullable
        public final IOException ioException;

        public TokenEntityOrIOException(@Nonnull TokenEntity tokenEntity) {
            this.tokenEntity = tokenEntity;
            this.ioException = null;
        }

        private TokenEntityOrIOException(@Nonnull IOException ioException) {
            this.tokenEntity = null;
            this.ioException = ioException;
        }
    }

    private static class TestTokenEntityStore implements JiveCoreTokenEntityStore {
        public final Queue<TokenEntityOrIOException> tokenEntityOrIOExceptionQueue = new LinkedBlockingQueue<TokenEntityOrIOException>();

        @Nullable
        @Override
        public TokenEntity getTokenEntity() throws IOException {
            try {
                TokenEntityOrIOException tokenEntityOrIOException = tokenEntityOrIOExceptionQueue.remove();
                if (tokenEntityOrIOException.ioException == null) {
                    return tokenEntityOrIOException.tokenEntity;
                } else {
                    throw tokenEntityOrIOException.ioException;
                }
            } catch (NoSuchElementException e) {
                return null;
            }
        }
    }

    private static class TestTokenEntityRefresher implements JiveCoreTokenEntityRefresher {
        public final Queue<TokenEntityOrIOException> tokenEntityOrIOExceptionQueue = new LinkedBlockingQueue<TokenEntityOrIOException>();

        @Nullable
        @Override
        public TokenEntity refreshTokenEntity(@Nonnull String refreshToken) throws IOException {
            try {
                TokenEntityOrIOException tokenEntityOrIOException = tokenEntityOrIOExceptionQueue.remove();
                if (tokenEntityOrIOException.ioException == null) {
                    return tokenEntityOrIOException.tokenEntity;
                } else {
                    throw tokenEntityOrIOException.ioException;
                }
            } catch (NoSuchElementException e) {
                return null;
            }
        }
    }
}
