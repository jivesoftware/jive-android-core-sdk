package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import com.jivesoftware.android.mobile.sdk.entity.SimpleErrorEntity;
import org.apache.http.HttpResponse;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreOAuthInvalidClientException extends JiveCoreOAuthException {
    public static final int ERROR_CODE = SimpleErrorEntity.OAuth2ErrorType.INVALID_CLIENT.ordinal();

    public JiveCoreOAuthInvalidClientException(HttpResponse httpResponse, ErrorEntity errorEntity) {
        super(httpResponse, errorEntity);
    }
}
