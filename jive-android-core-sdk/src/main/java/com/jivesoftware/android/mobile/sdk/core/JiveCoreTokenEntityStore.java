package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;

import javax.annotation.Nullable;

public interface JiveCoreTokenEntityStore {
    @Nullable
    TokenEntity getTokenEntity() throws Exception;
}
