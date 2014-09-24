package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JiveCoreUnknownException extends JiveCoreException {
    @Nullable
    public final HttpEntity httpEntity;
    @Nonnull
    public final byte[] contentBodyBytes;

    public JiveCoreUnknownException(@Nonnull HttpResponse httpResponse, int statusCode, HttpEntity httpEntity, @Nonnull byte[] contentBodyBytes) {
        this((Throwable)null, httpResponse, statusCode, httpEntity, contentBodyBytes);
    }

    public JiveCoreUnknownException(Throwable cause, @Nonnull HttpResponse httpResponse, int statusCode, HttpEntity httpEntity, @Nonnull byte[] contentBodyBytes) {
        this(new String(contentBodyBytes), httpResponse, statusCode, httpEntity, contentBodyBytes);
    }

    public JiveCoreUnknownException(@Nullable String message, @Nonnull HttpResponse httpResponse, int statusCode, HttpEntity httpEntity, @Nonnull byte[] contentBodyBytes) {
        this(message, null, httpResponse, statusCode, httpEntity, contentBodyBytes);
    }

    public JiveCoreUnknownException(@Nullable String message, @Nullable Throwable cause, @Nonnull HttpResponse httpResponse, int statusCode, HttpEntity httpEntity, @Nonnull byte[] contentBodyBytes) {
        super(message, cause, httpResponse, statusCode);
        this.httpEntity = httpEntity;
        this.contentBodyBytes = contentBodyBytes;
    }
}
