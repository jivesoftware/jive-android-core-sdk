package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpResponse;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreInvalidJsonException extends JiveCoreException {
    public JiveCoreInvalidJsonException(@Nullable String message, HttpResponse httpResponse) {
        super(message, httpResponse);
    }

    public JiveCoreInvalidJsonException(@Nullable String message, @Nullable Throwable cause, HttpResponse httpResponse) {
        super(message, cause, httpResponse);
    }
}
