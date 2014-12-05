package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public class EmptyHttpResponseParser extends HttpResponseParser<Void> {
    public EmptyHttpResponseParser(JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        super(jiveCoreExceptionFactory);
    }

    @Nullable
    @Override
    protected Void parseValidResponse(
            HttpResponse httpResponse,
            int statusCode,
            @Nullable HttpEntity httpEntity) throws IOException {
        if (httpEntity != null) {
            httpEntity.consumeContent();
        }
        return null;
    }
}
