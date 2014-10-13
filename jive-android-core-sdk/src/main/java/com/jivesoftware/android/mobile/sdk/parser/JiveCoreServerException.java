package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpResponse;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreServerException extends JiveCoreException {
    public JiveCoreServerException(HttpResponse httpResponse) {
        super((String)null, httpResponse);
    }
}
