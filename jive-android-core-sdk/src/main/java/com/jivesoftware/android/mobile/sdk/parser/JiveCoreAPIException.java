package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JiveCoreAPIException extends JiveCoreParsedException {
    @Nullable
    public final String apiErrorCode;
    public JiveCoreAPIException(@Nonnull HttpResponse httpResponse, int statusCode, @Nonnull ErrorEntity errorEntity, @Nullable String apiErrorCode) {
        super(httpResponse, statusCode, errorEntity);
        this.apiErrorCode = apiErrorCode;
    }
}
