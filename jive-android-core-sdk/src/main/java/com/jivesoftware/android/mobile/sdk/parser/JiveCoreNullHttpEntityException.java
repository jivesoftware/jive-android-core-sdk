package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;

public class JiveCoreNullHttpEntityException extends JiveCoreException {
    public JiveCoreNullHttpEntityException(@Nonnull HttpResponse httpResponse, int statusCode) {
        super("HttpEntity was null", httpResponse, statusCode);
    }
}
