package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

/**
 * Created by mark.schisler on 8/14/14.
 */
public class EmptyHttpResponseParser extends HttpResponseParser<Void> {
    public EmptyHttpResponseParser(@Nonnull JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        super(jiveCoreExceptionFactory);
    }

    @Nullable
    @Override
    protected Void parseValidResponse(@Nonnull HttpResponse httpResponse, int statusCode, @Nullable HttpEntity httpEntity) throws IOException {
        if (httpEntity != null) {
            httpEntity.consumeContent();
        }
        return null;
    }
}
