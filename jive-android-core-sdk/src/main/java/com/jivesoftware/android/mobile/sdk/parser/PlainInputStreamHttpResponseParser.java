package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStream;

@ParametersAreNonnullByDefault
public class PlainInputStreamHttpResponseParser extends InputStreamHttpResponseParser<InputStream> {
    public PlainInputStreamHttpResponseParser(JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        super(jiveCoreExceptionFactory);
    }

    @Nonnull
    @Override
    protected InputStream parseContentInputStreamedResponse(HttpResponse httpResponse,
                                                            int statusCode,
                                                            HttpEntity httpEntity,
                                                            InputStream contentInputStream) throws IOException {
        return contentInputStream;
    }
}
