package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JiveCoreLoginRequiredException extends JiveCoreException {
    @Nullable
    public final ErrorEntity errorEntity;
    public JiveCoreLoginRequiredException(@Nonnull HttpResponse httpResponse, int statusCode, @Nullable ErrorEntity errorEntity) {
        super(errorEntity, httpResponse, statusCode);
        this.errorEntity = errorEntity;
    }
}
