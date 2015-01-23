package com.jivesoftware.android.mobile.sdk.httpclient;

import com.jivesoftware.android.mobile.sdk.core.JiveCoreConstants;
import com.jivesoftware.android.mobile.sdk.core.JiveCoreOAuthUnrefreshableAuthenticationException;
import com.jivesoftware.android.mobile.sdk.core.JiveCoreTokenEntityRefresher;
import com.jivesoftware.android.mobile.sdk.core.TokenEntityComparator;
import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;
import com.jivesoftware.android.mobile.sdk.util.EqualsAtomicReference;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.impl.auth.AuthSchemeBase;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JiveCoreAuthScheme extends AuthSchemeBase {
    private static final String AUTH_HEADER_START = AUTH.WWW_AUTH_RESP + ": ";
    // access token types are usually "bearer" (6 chars). access tokens are usually 44 characters long. add 64 just to be safe.
    private static final int CHAR_ARRAY_BUFFER_INITIAL_SIZE = AUTH_HEADER_START.length() + 64;
    @Nonnull
    private final JiveCoreTokenEntityRefresher tokenEntityRefresher;

    @Nonnull
    private final EqualsAtomicReference<TokenEntity> lastUsedTokenEntityEqualsAtomicReference = new EqualsAtomicReference<TokenEntity>(null, TokenEntityComparator.SINGLETON);

    private volatile boolean complete;

    public JiveCoreAuthScheme(@Nonnull JiveCoreTokenEntityRefresher tokenEntityRefresher) {
        this.tokenEntityRefresher = tokenEntityRefresher;
    }

    @Override
    protected void parseChallenge(CharArrayBuffer buffer, int pos, int len) throws MalformedChallengeException {
        // nothing to do, our challenge holds no data.
    }

    @Override
    public String getSchemeName() {
        return JiveCoreConstants.JIVE_CORE_AUTH_SCHEME_NAME;
    }

    @Override
    @Nullable
    public String getParameter(String name) {
        return null;
    }

    @Override
    @Nullable
    public String getRealm() {
        return null;
    }

    @Override
    public boolean isConnectionBased() {
        return false;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Nonnull
    @Override
    public Header authenticate(@Nullable Credentials credentials, HttpRequest request) throws AuthenticationException {
        if (credentials == null) {
            throw new AssertionError("HttpClient says Credentials shouldn't be null");
        }
        TokenEntityCredentials tokenEntityCredentials = (TokenEntityCredentials) credentials;
        @Nullable
        TokenEntity lastUsedTokenEntity = lastUsedTokenEntityEqualsAtomicReference.get();
        @Nullable
        TokenEntity tokenEntityCredentialsTokenEntity = tokenEntityCredentials.tokenEntityEqualsAtomicReference.get();
        if (tokenEntityCredentialsTokenEntity == null) {
            complete = true;
            throw new JiveCoreOAuthUnrefreshableAuthenticationException("OAuth tokens were invalidated");
        }

        @Nullable
        TokenEntity maybeNextTokenEntity;
        if (TokenEntityComparator.SINGLETON.compare(lastUsedTokenEntity, tokenEntityCredentialsTokenEntity) == 0) {
            // TokenEntityCredentials' TokenEntity is the one we last used, so it failed. We need to refresh it.
            // tokenEntityCredentialsOriginalAccessToken has been used and failed (we only hit this method after a failure)
            if (tokenEntityCredentialsTokenEntity.refreshToken == null) {
                complete = true;
                throw new JiveCoreOAuthUnrefreshableAuthenticationException("OAuth refreshToken is null");
            }
            // TokenEntityCredentials' TokenEntity is the one we last used, so it failed. We need to refresh it.
            // tokenEntityCredentialsOriginalAccessToken has been used and failed (we only hit this method after a failure)
            TokenEntity refreshedTokenEntity;
            try {
                refreshedTokenEntity = tokenEntityRefresher.refreshTokenEntity(tokenEntityCredentialsTokenEntity.refreshToken);
            } catch (Exception e) {
                tokenEntityCredentials.tokenEntityEqualsAtomicReference.set(null);
                complete = true;
                throw new JiveCoreOAuthUnrefreshableAuthenticationException("OAuth refreshToken failed", e);
            }

            if (tokenEntityCredentials.tokenEntityEqualsAtomicReference.compareAndSet(tokenEntityCredentialsTokenEntity, refreshedTokenEntity)) {
                // if refreshedTokenEntity was null, we just invalidated TokenEntityCredentials' TokenEntity,
                // but that's ok because refreshedTokenEntity wasn't valid anyway.
                maybeNextTokenEntity = refreshedTokenEntity;
            } else {
                // TokenEntityCredentials' TokenEntity might be null if it was invalidated while we were refreshing
                maybeNextTokenEntity = tokenEntityCredentials.tokenEntityEqualsAtomicReference.get();
            }
        } else {
            // lastUsedTokenEntity is different from TokenEntityCredentials' TokenEntity, so we'll try it.
            // TokenEntityCredentials' TokenEntity might be null if it was invalidated before this call.
            maybeNextTokenEntity = tokenEntityCredentialsTokenEntity;
        }

        if (maybeNextTokenEntity == null) {
            complete = true;
            throw new JiveCoreOAuthUnrefreshableAuthenticationException("OAuth tokens were invalidated");
        }

        TokenEntity nextTokenEntity;
        if (lastUsedTokenEntityEqualsAtomicReference.compareAndSet(lastUsedTokenEntity, maybeNextTokenEntity)) {
            nextTokenEntity = maybeNextTokenEntity;
        } else {
            // these might be a bad token, but if we can recover, we will. Otherwise, we'll fail on the next retry.
            nextTokenEntity = lastUsedTokenEntityEqualsAtomicReference.get();
        }

        // TODO - need a header to return when all we've got is a null
        @SuppressWarnings("ConstantConditions") Header authenticationHeader = authenticate(nextTokenEntity);
        return authenticationHeader;
    }

    @Nonnull
    public static Header authenticate(@Nonnull TokenEntity tokenEntity) {
        CharArrayBuffer charArrayBuffer = new CharArrayBuffer(CHAR_ARRAY_BUFFER_INITIAL_SIZE);
        charArrayBuffer.append(AUTH_HEADER_START);
        charArrayBuffer.append(tokenEntity.tokenType);
        charArrayBuffer.append(" ");
        charArrayBuffer.append(tokenEntity.accessToken);
        return new BufferedHeader(charArrayBuffer);
    }
}
