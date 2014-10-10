package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreOAuthException extends JiveCoreParsedException {
    public JiveCoreOAuthException(HttpResponse httpResponse, ErrorEntity errorEntity) {
        this(null, httpResponse, errorEntity);
    }

    public JiveCoreOAuthException(@Nullable Throwable cause, HttpResponse httpResponse, ErrorEntity errorEntity) {
        super(cause, httpResponse, errorEntity);
    }
}
