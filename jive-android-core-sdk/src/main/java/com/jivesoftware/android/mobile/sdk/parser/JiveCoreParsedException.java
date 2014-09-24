package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;

public class JiveCoreParsedException extends JiveCoreException {
    @Nonnull
    public final ErrorEntity errorEntity;

    public JiveCoreParsedException(@Nonnull HttpResponse httpResponse, int statusCode, @Nonnull ErrorEntity errorEntity) {
        this(null, httpResponse, statusCode, errorEntity);
    }

    public JiveCoreParsedException(Throwable cause, @Nonnull HttpResponse httpResponse, int statusCode, @Nonnull ErrorEntity errorEntity) {
        super(errorEntity, cause, httpResponse, statusCode);
        this.errorEntity = errorEntity;
    }
}
