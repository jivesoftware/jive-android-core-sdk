package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

public abstract class HttpEntitiedHttpResponseParser<T> extends HttpResponseParser<T> {
    public HttpEntitiedHttpResponseParser(@Nonnull JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        super(jiveCoreExceptionFactory);
    }

    @Nullable
     @Override
     protected T parseValidResponse(
            @Nonnull HttpResponse httpResponse,
            int statusCode,
            @Nullable HttpEntity httpEntity) throws IOException {
        if (httpEntity == null) {
            throw new JiveCoreNullHttpEntityException(httpResponse, statusCode);
        } else {
            T result = parseHttpEntitiedResponse(httpResponse, statusCode, httpEntity);
            return result;
        }
    }

    @Nullable
    protected abstract T parseHttpEntitiedResponse(
            @Nonnull HttpResponse httpResponse,
            int statusCode,
            @Nonnull HttpEntity httpEntity) throws IOException;
}
