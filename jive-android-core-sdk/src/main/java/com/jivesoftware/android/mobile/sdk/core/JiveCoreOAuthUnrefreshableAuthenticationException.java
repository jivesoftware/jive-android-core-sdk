package com.jivesoftware.android.mobile.sdk.core;

import org.apache.http.auth.AuthenticationException;

public class JiveCoreOAuthUnrefreshableAuthenticationException extends AuthenticationException {
    public JiveCoreOAuthUnrefreshableAuthenticationException(String message) {
        super(message);
    }

    public JiveCoreOAuthUnrefreshableAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
