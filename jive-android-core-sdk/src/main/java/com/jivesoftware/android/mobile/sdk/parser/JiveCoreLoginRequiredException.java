package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreLoginRequiredException extends JiveCoreException {
    @Nullable
    public final ErrorEntity errorEntity;

    public JiveCoreLoginRequiredException(HttpResponse httpResponse, @Nullable ErrorEntity errorEntity) {
        super(errorEntity, httpResponse);
        this.errorEntity = errorEntity;
    }
}
