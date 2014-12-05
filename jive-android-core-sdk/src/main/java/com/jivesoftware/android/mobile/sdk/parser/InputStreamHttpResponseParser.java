package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.util.HttpEntityUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStream;

@ParametersAreNonnullByDefault
public abstract class InputStreamHttpResponseParser<T> extends HttpEntitiedHttpResponseParser<T> {
    protected InputStreamHttpResponseParser(JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        super(jiveCoreExceptionFactory);
    }

    @Nullable
    @Override
    protected T parseHttpEntitiedResponse(
            HttpResponse httpResponse,
            int statusCode,
            HttpEntity httpEntity) throws IOException, JiveCoreException {
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
            HttpResponse httpResponse,
            int statusCode,
            HttpEntity httpEntity,
            InputStream contentInputStream) throws IOException;
}
