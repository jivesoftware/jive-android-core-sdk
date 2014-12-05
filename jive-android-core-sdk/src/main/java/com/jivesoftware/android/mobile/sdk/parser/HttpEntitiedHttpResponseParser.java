package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public abstract class HttpEntitiedHttpResponseParser<T> extends HttpResponseParser<T> {
    public HttpEntitiedHttpResponseParser(JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        super(jiveCoreExceptionFactory);
    }

    @Nullable
     @Override
     protected T parseValidResponse(
            HttpResponse httpResponse,
            int statusCode,
            @Nullable HttpEntity httpEntity) throws IOException, JiveCoreException {
        if (httpEntity == null) {
            throw new JiveCoreNullHttpEntityException(httpResponse);
        } else {
            T result = parseHttpEntitiedResponse(httpResponse, statusCode, httpEntity);
            return result;
        }
    }

    @Nullable
    protected abstract T parseHttpEntitiedResponse(
            HttpResponse httpResponse,
            int statusCode,
            HttpEntity httpEntity) throws IOException, JiveCoreException;
}
