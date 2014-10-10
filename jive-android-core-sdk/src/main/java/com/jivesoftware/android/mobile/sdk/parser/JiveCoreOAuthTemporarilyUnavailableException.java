package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import com.jivesoftware.android.mobile.sdk.entity.SimpleErrorEntity;
import org.apache.http.HttpResponse;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreOAuthTemporarilyUnavailableException extends JiveCoreOAuthException {
    public static final int ERROR_CODE = SimpleErrorEntity.OAuth2ErrorType.TEMPORARILY_UNAVAILABLE.ordinal();

    public JiveCoreOAuthTemporarilyUnavailableException(HttpResponse httpResponse, ErrorEntity errorEntity) {
        super(httpResponse, errorEntity);
    }
}
