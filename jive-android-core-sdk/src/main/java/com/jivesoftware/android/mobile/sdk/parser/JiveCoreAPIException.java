package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreAPIException extends JiveCoreParsedException {
    @Nullable
    public final String apiErrorCode;
    public JiveCoreAPIException(HttpResponse httpResponse, ErrorEntity errorEntity, @Nullable String apiErrorCode) {
        super(httpResponse, errorEntity);
        this.apiErrorCode = apiErrorCode;
    }
}
