package com.jivesoftware.android.mobile.sdk.entity.matcher;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TokenEntityMatchers {
    public static Matcher<TokenEntity> tokenAccessToken(@Nonnull Matcher<? super String> accessTokenMatcher) {
        return new PropertyMatcher<String, TokenEntity>(accessTokenMatcher, "accessToken") {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull TokenEntity item) throws Exception {
                return item.accessToken;
            }
        };
    }

    public static Matcher<TokenEntity> tokenExpiresIn(@Nonnull Matcher<? super Long> expiresInMatcher) {
        return new PropertyMatcher<Long, TokenEntity>(expiresInMatcher, "expiresIn") {
            @Nonnull
            @Override
            protected Long getPropertyValue(@Nonnull TokenEntity item) throws Exception {
                return item.expiresIn;
            }
        };
    }

    public static Matcher<TokenEntity> tokenTokenType(@Nonnull String tokenType) {
        return new PropertyMatcher<String, TokenEntity>("tokenType", tokenType) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull TokenEntity item) throws Exception {
                return item.tokenType;
            }
        };
    }

    public static Matcher<TokenEntity> tokenRefreshToken(@Nonnull Matcher<? super String> refreshTokenMatcher) {
        return new PropertyMatcher<String, TokenEntity>(refreshTokenMatcher, "refreshToken") {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull TokenEntity item) throws Exception {
                return item.refreshToken;
            }
        };
    }
}
