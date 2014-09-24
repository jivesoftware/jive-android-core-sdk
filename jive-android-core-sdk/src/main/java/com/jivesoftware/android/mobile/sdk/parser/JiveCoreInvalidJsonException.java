package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JiveCoreInvalidJsonException extends JiveCoreException {
    public JiveCoreInvalidJsonException(@Nullable String message, @Nonnull HttpResponse httpResponse, int statusCode) {
        super(message, httpResponse, statusCode);
    }

    public JiveCoreInvalidJsonException(@Nullable String message, @Nullable Throwable cause, @Nonnull HttpResponse httpResponse, int statusCode) {
        super(message, cause, httpResponse, statusCode);
    }
}
