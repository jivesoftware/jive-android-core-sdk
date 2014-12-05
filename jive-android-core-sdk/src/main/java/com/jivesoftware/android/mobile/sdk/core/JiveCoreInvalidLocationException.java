package com.jivesoftware.android.mobile.sdk.core;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreInvalidLocationException extends Exception {
    public JiveCoreInvalidLocationException() {
    }

    public JiveCoreInvalidLocationException(Throwable throwable) {
        super(throwable);
    }
}
