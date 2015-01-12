package com.jivesoftware.android.mobile.sdk.core;

import com.jayway.awaitility.Awaitility;
import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;
import com.jivesoftware.android.mobile.sdk.json.JiveJson;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class AbstractITest extends TestEndpoint {
    protected static final long POLL_DELAY_AMOUNT = 100L;
    protected static final TimeUnit POLL_DELAY_TIME_UNIT = TimeUnit.MILLISECONDS;
    protected static final long POLL_INTERVAL_AMOUNT = 1L;
    protected static final TimeUnit POLL_INTERVAL_TIME_UNIT = TimeUnit.SECONDS;

    static {
        //trueify to enable logging
        //noinspection ConstantIfStatement,ConstantConditions
        if (false) {
            java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);
            java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.FINEST);

            System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
            System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
            System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
            System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "debug");
            System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "debug");
        }

        Awaitility.setDefaultPollDelay(POLL_DELAY_AMOUNT, POLL_DELAY_TIME_UNIT);
        Awaitility.setDefaultPollInterval(POLL_INTERVAL_AMOUNT, POLL_INTERVAL_TIME_UNIT);
        Awaitility.setDefaultTimeout(TIMEOUT_AMOUNT, TIMEOUT_TIME_UNIT);
    }

    protected JiveJson jiveJson = new JiveJson();

    protected JiveCoreUnauthenticated jiveCoreUnauthenticatedAdminRedirecting;

    protected JiveCoreUnauthenticated jiveCoreUnauthenticatedAdmin;

    protected volatile TestTokenEntityStore testTokenEntityStoreAdmin;
    protected volatile TestTokenEntityRefresher testTokenEntityRefresherAdmin;

    protected JiveCore jiveCoreAdmin;

    protected JiveCoreUnauthenticated jiveCoreUnauthenticatedUser2;

    protected volatile TestTokenEntityStore testTokenEntityStoreUser2;
    protected volatile TestTokenEntityRefresher testTokenEntityRefresherUser2;

    protected JiveCore jiveCoreUser2;

    protected JiveCoreUnauthenticated jiveCoreUnauthenticatedUser3;

    protected volatile TestTokenEntityStore testTokenEntityStoreUser3;
    protected volatile TestTokenEntityRefresher testTokenEntityRefresherUser3;

    protected JiveCore jiveCoreUser3;

    @Before
    public void setup() throws Exception {
        jiveCoreUnauthenticatedAdminRedirecting = new JiveCoreUnauthenticated(
                TEST_REDIRECTING_URL,
                OAUTH_CREDENTIALS,
                OAUTH_ADDON_UUID,
                new DefaultHttpClient(),
                jiveJson);

        jiveCoreUnauthenticatedAdmin = new JiveCoreUnauthenticated(
                TEST_URL,
                OAUTH_CREDENTIALS,
                OAUTH_ADDON_UUID,
                new DefaultHttpClient(),
                jiveJson);

        testTokenEntityStoreAdmin = new TestTokenEntityStore(
                jiveCoreUnauthenticatedAdmin,
                ADMIN.username,
                ADMIN.password);
        testTokenEntityRefresherAdmin = new TestTokenEntityRefresher();

        jiveCoreAdmin = new JiveCore(
                TEST_URL,
                OAUTH_CREDENTIALS,
                new DefaultHttpClient(),
                testTokenEntityStoreAdmin,
                testTokenEntityRefresherAdmin,
                null,
                jiveJson);

        jiveCoreUnauthenticatedUser2 = new JiveCoreUnauthenticated(
                TEST_URL,
                OAUTH_CREDENTIALS,
                OAUTH_ADDON_UUID,
                new DefaultHttpClient(),
                jiveJson);


        testTokenEntityStoreUser2 = new TestTokenEntityStore(
                jiveCoreUnauthenticatedUser2,
                USER2.username,
                USER2.password);
        testTokenEntityRefresherUser2 = new TestTokenEntityRefresher();

        jiveCoreUser2 = new JiveCore(
                TEST_URL,
                OAUTH_CREDENTIALS,
                new DefaultHttpClient(),
                testTokenEntityStoreUser2,
                testTokenEntityRefresherUser2,
                null,
                jiveJson);

        jiveCoreUnauthenticatedUser3 = new JiveCoreUnauthenticated(
                TEST_URL,
                OAUTH_CREDENTIALS,
                OAUTH_ADDON_UUID,
                new DefaultHttpClient(),
                jiveJson);

        testTokenEntityStoreUser3 = new TestTokenEntityStore(
                jiveCoreUnauthenticatedUser3,
                USER3.username,
                USER3.password);
        testTokenEntityRefresherUser3 = new TestTokenEntityRefresher();

        jiveCoreUser3 = new JiveCore(
                TEST_URL,
                OAUTH_CREDENTIALS,
                new DefaultHttpClient(),
                testTokenEntityStoreUser3,
                testTokenEntityRefresherUser3,
                null,
                jiveJson);
    }

    @After
    public void tearDown() throws Exception {
        if (jiveCoreUnauthenticatedAdmin != null) {
            jiveCoreUnauthenticatedAdmin.close();
            jiveCoreUnauthenticatedAdmin = null;
        }
        if (jiveCoreAdmin != null) {
            jiveCoreAdmin.close();
            jiveCoreAdmin = null;
        }

        if (jiveCoreUnauthenticatedUser2 != null) {
            jiveCoreUnauthenticatedUser2.close();
            jiveCoreUnauthenticatedUser2 = null;
        }

        if (jiveCoreUser2 != null) {
            jiveCoreUser2.close();
            jiveCoreUser2 = null;
        }

        if (jiveCoreUnauthenticatedUser3 != null) {
            jiveCoreUnauthenticatedUser3.close();
            jiveCoreUnauthenticatedUser3 = null;
        }

        if (jiveCoreUser3 != null) {
            jiveCoreUser3.close();
            jiveCoreUser3 = null;
        }
    }

    @ParametersAreNonnullByDefault
    protected static class TestTokenEntityStore implements JiveCoreTokenEntityStore {
        @Nonnull
        private final JiveCoreUnauthenticated jiveCoreUnauthenticated;
        @Nonnull
        private final String username;

        @Nonnull
        private final String password;

        public TestTokenEntityStore(
                JiveCoreUnauthenticated jiveCoreUnauthenticated,
                String username,
                String password) {
            this.jiveCoreUnauthenticated = jiveCoreUnauthenticated;
            this.username = username;
            this.password = password;
        }

        @Nullable
        @Override
        public TokenEntity getTokenEntity() throws IOException, JiveCoreException {
            JiveCoreCallable<TokenEntity> authorizeDeviceCallable = jiveCoreUnauthenticated.authorizeDevice(
                    username,
                    password);
            TokenEntity tokenEntity = authorizeDeviceCallable.call();
            return tokenEntity;
        }
    }

    @ParametersAreNonnullByDefault
    protected class TestTokenEntityRefresher implements JiveCoreTokenEntityRefresher {
        public final CopyOnWriteArrayList<TokenEntity> refreshedTokenEntities = new CopyOnWriteArrayList<TokenEntity>();

        @Nullable
        @Override
        public TokenEntity refreshTokenEntity(String refreshToken) throws IOException, JiveCoreException {
            JiveCoreCallable<TokenEntity> refreshTokenCallable = jiveCoreUnauthenticatedAdmin.refreshToken(refreshToken);
            TokenEntity refreshedTokenEntity = refreshTokenCallable.call();
            refreshedTokenEntities.add(refreshedTokenEntity);
            return refreshedTokenEntity;
        }
    }
}
