package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;

public class PlainInputStreamHttpResponseParser extends InputStreamHttpResponseParser<InputStream> {
    public PlainInputStreamHttpResponseParser(@Nonnull JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        super(jiveCoreExceptionFactory);
    }

    @Nonnull
    @Override
    protected InputStream parseContentInputStreamedResponse(@Nonnull HttpResponse httpResponse,
                                                            int statusCode,
                                                            @Nonnull HttpEntity httpEntity,
                                                            @Nonnull InputStream contentInputStream) throws IOException {
        return contentInputStream;
    }
}
