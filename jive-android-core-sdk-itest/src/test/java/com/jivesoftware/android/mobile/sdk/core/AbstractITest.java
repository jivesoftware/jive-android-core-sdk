package com.jivesoftware.android.mobile.sdk.core;

import com.jayway.awaitility.Awaitility;
import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;
import com.jivesoftware.android.mobile.sdk.json.JiveJson;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class AbstractITest extends TestEndpoint {
    protected static final long POLL_DELAY_AMOUNT = 100L;
    protected static final TimeUnit POLL_DELAY_TIME_UNIT = TimeUnit.MILLISECONDS;
    protected static final long POLL_INTERVAL_AMOUNT = 1L;
    protected static final TimeUnit POLL_INTERVAL_TIME_UNIT = TimeUnit.SECONDS;

    static {
        Awaitility.setDefaultPollDelay(POLL_DELAY_AMOUNT, POLL_DELAY_TIME_UNIT);
        Awaitility.setDefaultPollInterval(POLL_INTERVAL_AMOUNT, POLL_INTERVAL_TIME_UNIT);
        Awaitility.setDefaultTimeout(TIMEOUT_AMOUNT, TIMEOUT_TIME_UNIT);
    }

    protected JiveJson jiveJson = new JiveJson();

    protected DefaultHttpClient jiveCoreUnauthenticatedDefaultHttpClientAdmin;
    protected JiveCoreUnauthenticated jiveCoreUnauthenticatedAdmin;

    protected volatile TestTokenEntityStore testTokenEntityStoreAdmin;
    protected volatile TestTokenEntityRefresher testTokenEntityRefresherAdmin;

    protected DefaultHttpClient jiveCoreDefaultHttpClientAdmin;
    protected JiveCore jiveCoreAdmin;

    protected DefaultHttpClient jiveCoreUnauthenticatedDefaultHttpClientUser2;
    protected JiveCoreUnauthenticated jiveCoreUnauthenticatedUser2;

    protected volatile TestTokenEntityStore testTokenEntityStoreUser2;
    protected volatile TestTokenEntityRefresher testTokenEntityRefresherUser2;

    protected DefaultHttpClient jiveCoreDefaultHttpClientUser2;
    protected JiveCore jiveCoreUser2;

    protected DefaultHttpClient jiveCoreUnauthenticatedDefaultHttpClientUser3;
    protected JiveCoreUnauthenticated jiveCoreUnauthenticatedUser3;

    protected volatile TestTokenEntityStore testTokenEntityStoreUser3;
    protected volatile TestTokenEntityRefresher testTokenEntityRefresherUser3;

    protected DefaultHttpClient jiveCoreDefaultHttpClientUser3;
    protected JiveCore jiveCoreUser3;

    @Before
    public void setup() throws Exception {
        jiveCoreUnauthenticatedDefaultHttpClientAdmin = new DefaultHttpClient();
        jiveCoreUnauthenticatedAdmin = new JiveCoreUnauthenticated(TEST_URL, OAUTH_CREDENTIALS, OAUTH_ADDON_UUID, jiveCoreUnauthenticatedDefaultHttpClientAdmin, jiveJson);

        testTokenEntityStoreAdmin = new TestTokenEntityStore(jiveCoreUnauthenticatedAdmin, ADMIN.username, ADMIN.password);
        testTokenEntityRefresherAdmin = new TestTokenEntityRefresher();

        jiveCoreDefaultHttpClientAdmin = new DefaultHttpClient();
        jiveCoreAdmin = new JiveCore(TEST_URL, jiveCoreDefaultHttpClientAdmin, testTokenEntityStoreAdmin, testTokenEntityRefresherAdmin, jiveJson);

        jiveCoreUnauthenticatedDefaultHttpClientUser2 = new DefaultHttpClient();
        jiveCoreUnauthenticatedUser2 = new JiveCoreUnauthenticated(TEST_URL, OAUTH_CREDENTIALS, OAUTH_ADDON_UUID, jiveCoreUnauthenticatedDefaultHttpClientUser2, jiveJson);

        testTokenEntityStoreUser2 = new TestTokenEntityStore(jiveCoreUnauthenticatedUser2, USER2.username, USER2.password);
        testTokenEntityRefresherUser2 = new TestTokenEntityRefresher();

        jiveCoreDefaultHttpClientUser2 = new DefaultHttpClient();
        jiveCoreUser2 = new JiveCore(TEST_URL, jiveCoreDefaultHttpClientUser2, testTokenEntityStoreUser2, testTokenEntityRefresherUser2, jiveJson);

        jiveCoreUnauthenticatedDefaultHttpClientUser3 = new DefaultHttpClient();
        jiveCoreUnauthenticatedUser3 = new JiveCoreUnauthenticated(TEST_URL, OAUTH_CREDENTIALS, OAUTH_ADDON_UUID, jiveCoreUnauthenticatedDefaultHttpClientUser3, jiveJson);

        testTokenEntityStoreUser3 = new TestTokenEntityStore(jiveCoreUnauthenticatedUser3, USER3.username, USER3.password);
        testTokenEntityRefresherUser3 = new TestTokenEntityRefresher();

        jiveCoreDefaultHttpClientUser3 = new DefaultHttpClient();
        jiveCoreUser3 = new JiveCore(TEST_URL, jiveCoreDefaultHttpClientUser3, testTokenEntityStoreUser3, testTokenEntityRefresherUser3, jiveJson);
    }

    @After
    public void tearDown() throws Exception {
        if (jiveCoreUnauthenticatedDefaultHttpClientAdmin != null) {
            jiveCoreUnauthenticatedDefaultHttpClientAdmin.getConnectionManager().shutdown();
            jiveCoreUnauthenticatedDefaultHttpClientAdmin = null;
        }
        if (jiveCoreDefaultHttpClientAdmin != null) {
            jiveCoreDefaultHttpClientAdmin.getConnectionManager().shutdown();
            jiveCoreDefaultHttpClientAdmin = null;
        }

        if (jiveCoreUnauthenticatedDefaultHttpClientUser2 != null) {
            jiveCoreUnauthenticatedDefaultHttpClientUser2.getConnectionManager().shutdown();
            jiveCoreUnauthenticatedDefaultHttpClientUser2 = null;
        }

        if (jiveCoreDefaultHttpClientUser2 != null) {
            jiveCoreDefaultHttpClientUser2.getConnectionManager().shutdown();
            jiveCoreDefaultHttpClientUser2 = null;
        }

        if (jiveCoreUnauthenticatedDefaultHttpClientUser3 != null) {
            jiveCoreUnauthenticatedDefaultHttpClientUser3.getConnectionManager().shutdown();
            jiveCoreUnauthenticatedDefaultHttpClientUser3 = null;
        }

        if (jiveCoreDefaultHttpClientUser3 != null) {
            jiveCoreDefaultHttpClientUser3.getConnectionManager().shutdown();
            jiveCoreDefaultHttpClientUser3 = null;
        }
    }

    protected static class TestTokenEntityStore implements JiveCoreTokenEntityStore {
        public final CopyOnWriteArrayList<TokenEntity> tokenEntities = new CopyOnWriteArrayList<TokenEntity>();

        @Nonnull
        private final JiveCoreUnauthenticated jiveCoreUnauthenticated;
        @Nonnull
        private final String username;

        @Nonnull
        private final String password;

        public TestTokenEntityStore(@Nonnull JiveCoreUnauthenticated jiveCoreUnauthenticated, @Nonnull String username, @Nonnull String password) {
            this.jiveCoreUnauthenticated = jiveCoreUnauthenticated;
            this.username = username;
            this.password = password;
        }

        @Nullable
        public TokenEntity getLastTokenEntity() {
            if (tokenEntities.isEmpty()) {
                return null;
            } else {
                return tokenEntities.get(tokenEntities.size() - 1);
            }
        }

        @Nullable
        @Override
        public TokenEntity getTokenEntity() throws IOException {
            JiveCoreCallable<TokenEntity> authorizeDeviceCallable = jiveCoreUnauthenticated.authorizeDevice(username, password);
            TokenEntity tokenEntity = authorizeDeviceCallable.call();
            tokenEntities.add(tokenEntity);

            return tokenEntity;
        }
    }

    protected class TestTokenEntityRefresher implements JiveCoreTokenEntityRefresher {
        public final CopyOnWriteArrayList<TokenEntity> refreshedTokenEntities = new CopyOnWriteArrayList<TokenEntity>();

        @Nullable
        @Override
        public TokenEntity refreshTokenEntity(@Nonnull String refreshToken) throws IOException {
            JiveCoreCallable<TokenEntity> refreshTokenCallable = jiveCoreUnauthenticatedAdmin.refreshToken(refreshToken);
            TokenEntity refreshedTokenEntity = refreshTokenCallable.call();
            refreshedTokenEntities.add(refreshedTokenEntity);
            return refreshedTokenEntity;
        }
    }
}
