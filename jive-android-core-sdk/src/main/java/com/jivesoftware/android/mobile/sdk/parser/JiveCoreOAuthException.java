package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;

public class JiveCoreOAuthException extends JiveCoreParsedException {
    public JiveCoreOAuthException(@Nonnull HttpResponse httpResponse, int statusCode, @Nonnull ErrorEntity errorEntity) {
        this(null, httpResponse, statusCode, errorEntity);
    }

    public JiveCoreOAuthException(Throwable cause, @Nonnull HttpResponse httpResponse, int statusCode, @Nonnull ErrorEntity errorEntity) {
        super(cause, httpResponse, statusCode, errorEntity);
    }
}
