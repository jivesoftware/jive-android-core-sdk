package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStream;

@ParametersAreNonnullByDefault
public abstract class InputStreamClosingHttpResponseParser<T> extends InputStreamHttpResponseParser<T> {
    protected InputStreamClosingHttpResponseParser(JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        super(jiveCoreExceptionFactory);
    }

    @Nullable
    @Override
    protected T parseContentInputStreamedResponse(
            HttpResponse httpResponse,
            int statusCode,
            HttpEntity httpEntity,
            InputStream contentInputStream) throws IOException {
        try {
            T result = parseContentInputStreamedResponseBeforeClosingContentInputStream(
                    httpResponse,
                    statusCode,
                    httpEntity,
                    contentInputStream);
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
            HttpResponse httpResponse,
            int statusCode,
            HttpEntity httpEntity,
            InputStream contentInputStream) throws IOException;
}
