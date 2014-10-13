package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpResponse;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreMobileGatewayException extends JiveCoreException {
    public JiveCoreMobileGatewayException(HttpResponse httpResponse) {
        super((String)null, httpResponse);
    }
}
