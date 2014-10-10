package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreUnknownException extends JiveCoreHttpEntitiedException {
    @Nonnull
    public final byte[] contentBodyBytes;

    public JiveCoreUnknownException(HttpResponse httpResponse, @Nullable HttpEntity httpEntity, byte[] contentBodyBytes) {
        this((Throwable)null, httpResponse, httpEntity, contentBodyBytes);
    }

    public JiveCoreUnknownException(@Nullable Throwable cause, HttpResponse httpResponse, @Nullable HttpEntity httpEntity, byte[] contentBodyBytes) {
        this(new String(contentBodyBytes), cause, httpResponse, httpEntity, contentBodyBytes);
    }

    public JiveCoreUnknownException(@Nullable String message, HttpResponse httpResponse, @Nullable HttpEntity httpEntity, byte[] contentBodyBytes) {
        this(message, null, httpResponse, httpEntity, contentBodyBytes);
    }

    public JiveCoreUnknownException(@Nullable String message, @Nullable Throwable cause, HttpResponse httpResponse, @Nullable HttpEntity httpEntity, byte[] contentBodyBytes) {
        super(message, cause, httpResponse, httpEntity);
        this.contentBodyBytes = contentBodyBytes;
    }
}
