package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.util.HttpEntityUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;

public abstract class InputStreamHttpResponseParser<T> extends HttpEntitiedHttpResponseParser<T> {
    protected InputStreamHttpResponseParser(@Nonnull JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        super(jiveCoreExceptionFactory);
    }

    @Nullable
    @Override
    protected T parseHttpEntitiedResponse(@Nonnull HttpResponse httpResponse, int statusCode, @Nonnull HttpEntity httpEntity) throws IOException, JiveCoreException {
        InputStream maybeTransformedContentInputStream = httpEntity.getContent();
        if (maybeTransformedContentInputStream == null) {
            throw new JiveCoreNullContentInputStreamException(httpResponse, httpEntity);
        } else {
            InputStream contentInputStream = HttpEntityUtil.getUntransformedContentInputStream(httpEntity, maybeTransformedContentInputStream);
            T result = parseContentInputStreamedResponse(httpResponse, statusCode, httpEntity, contentInputStream);
            return result;
        }
    }

    @Nullable
    protected abstract T parseContentInputStreamedResponse(
            @Nonnull HttpResponse httpResponse,
            int statusCode,
            @Nonnull HttpEntity httpEntity,
            @Nonnull InputStream contentInputStream) throws IOException;
}
