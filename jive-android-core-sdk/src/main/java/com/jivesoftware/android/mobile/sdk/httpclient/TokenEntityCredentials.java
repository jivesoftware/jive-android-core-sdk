package com.jivesoftware.android.mobile.sdk.httpclient;

import com.jivesoftware.android.mobile.sdk.core.TokenEntityComparator;
import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;
import com.jivesoftware.android.mobile.sdk.util.EqualsAtomicReference;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.BasicUserPrincipal;
import org.apache.http.auth.Credentials;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.security.Principal;

public class TokenEntityCredentials implements Credentials {
    private static final String AUTH_HEADER_START = AUTH.WWW_AUTH_RESP + ": ";
    // access token types are usually "bearer" (6 chars). access tokens are usually 44 characters long. add 64 just to be safe.
    private static final int CHAR_ARRAY_BUFFER_INITIAL_SIZE = AUTH_HEADER_START.length() + 64;

    @Nonnull
    public final EqualsAtomicReference<TokenEntity> tokenEntityEqualsAtomicReference;

    public TokenEntityCredentials(@Nonnull TokenEntity tokenEntity) {
        tokenEntityEqualsAtomicReference = new EqualsAtomicReference<TokenEntity>(tokenEntity, TokenEntityComparator.SINGLETON);
    }

    @Override
    @Nullable
    public Principal getUserPrincipal() {
        TokenEntity tokenEntity = tokenEntityEqualsAtomicReference.get();
        if ((tokenEntity != null) && (tokenEntity.accessToken != null)) {
            return new BasicUserPrincipal(tokenEntity.accessToken);
        } else {
            return null;
        }
    }

    @Override
    @Nullable
    public String getPassword() {
        return null;
    }
}
