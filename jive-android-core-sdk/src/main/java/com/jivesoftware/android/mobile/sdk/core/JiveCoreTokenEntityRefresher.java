package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface JiveCoreTokenEntityRefresher {
    @Nullable
    TokenEntity refreshTokenEntity(@Nonnull String refreshToken) throws Exception;
}
