package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;

import javax.annotation.Nullable;
import java.io.IOException;

public interface JiveCoreTokenEntityStore {
    @Nullable
    TokenEntity getTokenEntity() throws IOException;
}
