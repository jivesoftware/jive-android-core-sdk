package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import com.jivesoftware.android.mobile.sdk.entity.SessionGrantEntity;
import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.jivesoftware.android.mobile.sdk.entity.matcher.SessionGrantEntityMatchers.sessionGrantStatus;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.TokenEntityMatchers.tokenAccessToken;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.TokenEntityMatchers.tokenExpiresIn;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.TokenEntityMatchers.tokenRefreshToken;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.TokenEntityMatchers.tokenTokenType;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class JiveCoreAuthenticationITest extends AbstractITest {
    private JiveCoreTokenEntityStore spyTestTokenEntityStore;

    @Before
    public void setup() throws Exception {
        super.setup();

        jiveCoreDefaultHttpClientAdmin.getConnectionManager().shutdown();

        spyTestTokenEntityStore = spy(testTokenEntityStoreAdmin);

        jiveCoreAdmin = new JiveCore(TEST_URL, new DefaultHttpClient(), spyTestTokenEntityStore, testTokenEntityRefresherAdmin);
    }

    @Test
    public void testWhenMultipleAuthenticatedCallsAreMadeCredentialsAreReused() throws Exception {
        JiveCoreCallable<PersonEntity> fetchMePersonJiveCoreCallable1 = jiveCoreAdmin.fetchMePerson();
        fetchMePersonJiveCoreCallable1.call();
        JiveCoreCallable<PersonEntity> fetchMePersonJiveCoreCallable2 = jiveCoreAdmin.fetchMePerson();
        fetchMePersonJiveCoreCallable2.call();

        verify(spyTestTokenEntityStore).getTokenEntity();
    }

    @Test
    public void testWhenAuthenticationTokenIsFetchedThenItIsCorrect() throws IOException {
        JiveCoreCallable<TokenEntity> callable = jiveCoreUnauthenticatedAdmin.authorizeDevice(ADMIN.username, ADMIN.password);
        TokenEntity tokenEntity = callable.call();

        assertThat(tokenEntity, allOf(
                tokenAccessToken(notNullValue()),
                tokenExpiresIn(not(equalTo(0))),
                tokenRefreshToken(notNullValue()),
                tokenTokenType("bearer")));
    }

    @Test
    public void testWhenDeauthenticatedTokenIsRemoved() throws IOException {
        JiveCoreCallable<PersonEntity> mePersonCallable1 = jiveCoreAdmin.fetchMePerson();
        // establishes that a legal TokenEntity exists
        mePersonCallable1.call();
        TokenEntity tokenEntity = testTokenEntityStoreAdmin.getLastTokenEntity();
        assertNotNull(tokenEntity);

        JiveCoreCallable<Void> deauthorizeDeviceCallable = jiveCoreUnauthenticatedAdmin.deauthorizeDevice(tokenEntity);
        deauthorizeDeviceCallable.call();
        JiveCoreCallable<Void> failingDeauthorizeDeviceCallable = jiveCoreUnauthenticatedAdmin.deauthorizeDevice(tokenEntity);
        try {
            failingDeauthorizeDeviceCallable.call();
            fail("This should fail");
        } catch (JiveCoreException e) {
            // success!
        }
    }

    @Test
    public void testRefreshToken() throws Exception {
        TokenEntity tokenEntity = jiveCoreUnauthenticatedAdmin.authorizeDevice(ADMIN.username, ADMIN.password).call();

        TokenEntity refreshedTokenEntity = jiveCoreUnauthenticatedAdmin.refreshToken(tokenEntity.refreshToken).call();

        assertThat(refreshedTokenEntity, allOf(
                tokenAccessToken(not(equalTo(tokenEntity.accessToken))),
                tokenExpiresIn(not(equalTo(0))),
                tokenRefreshToken(equalTo(tokenEntity.refreshToken)),
                tokenTokenType("bearer")));
    }

    @Test
    public void testSessionOAuthGrantEntityStatusIsNot200() throws Exception {
        SessionGrantEntity sessionGrantEntity = jiveCoreUnauthenticatedAdmin.fetchSessionGrant().call();
        assertThat(sessionGrantEntity, sessionGrantStatus(not(equalTo(200))));
    }
}
