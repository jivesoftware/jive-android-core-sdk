package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;

public class JiveCoreNullContentInputStreamException extends JiveCoreException {
    @Nonnull
    public final HttpEntity httpEntity;
    public JiveCoreNullContentInputStreamException(@Nonnull HttpResponse httpResponse, int statusCode, HttpEntity httpEntity) {
        super((String)null, httpResponse, statusCode);
        this.httpEntity = httpEntity;
    }
}
