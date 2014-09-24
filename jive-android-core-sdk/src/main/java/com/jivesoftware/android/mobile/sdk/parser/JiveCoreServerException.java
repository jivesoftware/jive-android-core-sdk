package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;

public class JiveCoreServerException extends JiveCoreException {
    public JiveCoreServerException(@Nonnull HttpResponse httpResponse, int statusCode) {
        super((String)null, httpResponse, statusCode);
    }
}
