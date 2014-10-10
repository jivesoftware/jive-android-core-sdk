package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpResponse;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreNullHttpEntityException extends JiveCoreException {
    public JiveCoreNullHttpEntityException(HttpResponse httpResponse) {
        super("HttpEntity was null", httpResponse);
    }
}
