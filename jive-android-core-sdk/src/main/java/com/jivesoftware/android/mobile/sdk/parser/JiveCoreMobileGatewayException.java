package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;

public class JiveCoreMobileGatewayException extends JiveCoreException {
    public JiveCoreMobileGatewayException(@Nonnull HttpResponse httpResponse, int statusCode) {
        super((String)null, httpResponse, statusCode);
    }
}
