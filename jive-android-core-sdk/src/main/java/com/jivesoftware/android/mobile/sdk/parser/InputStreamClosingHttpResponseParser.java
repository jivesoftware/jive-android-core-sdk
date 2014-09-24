package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;

public abstract class InputStreamClosingHttpResponseParser<T> extends InputStreamHttpResponseParser<T> {
    protected InputStreamClosingHttpResponseParser(@Nonnull JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        super(jiveCoreExceptionFactory);
    }

    @Nullable
    @Override
    protected T parseContentInputStreamedResponse(@Nonnull HttpResponse httpResponse, int statusCode, @Nonnull HttpEntity httpEntity, @Nonnull InputStream contentInputStream) throws IOException {
        try {
            T result = parseContentInputStreamedResponseBeforeClosingContentInputStream(httpResponse, statusCode, httpEntity, contentInputStream);
            return result;
        } finally {
            try {
                contentInputStream.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    @Nullable
    protected abstract T parseContentInputStreamedResponseBeforeClosingContentInputStream(
            @Nonnull HttpResponse httpResponse,
            int statusCode,
            @Nonnull HttpEntity httpEntity,
            @Nonnull InputStream contentInputStream) throws IOException;
}
