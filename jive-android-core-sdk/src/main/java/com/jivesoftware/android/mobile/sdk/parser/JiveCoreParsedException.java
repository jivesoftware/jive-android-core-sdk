package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreParsedException extends JiveCoreException {
    @Nonnull
    public final ErrorEntity errorEntity;

    public JiveCoreParsedException(HttpResponse httpResponse, ErrorEntity errorEntity) {
        this(null, httpResponse, errorEntity);
    }

    public JiveCoreParsedException(@Nullable Throwable cause, HttpResponse httpResponse, ErrorEntity errorEntity) {
        super(errorEntity, cause, httpResponse);
        this.errorEntity = errorEntity;
    }
}
