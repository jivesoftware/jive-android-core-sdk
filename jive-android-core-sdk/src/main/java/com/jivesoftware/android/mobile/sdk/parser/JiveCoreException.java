package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import com.jivesoftware.android.mobile.sdk.gson.JiveGson;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

public class JiveCoreException extends IOException {
    @Nonnull
    public final HttpResponse httpResponse;
    /**
     * -1 if unknown
     */
    public final int statusCode;


    public JiveCoreException(@Nullable String message, @Nonnull HttpResponse httpResponse, int statusCode) {
        this(message, null, httpResponse, statusCode);
    }

    public JiveCoreException(@Nullable String message, @Nullable Throwable cause, @Nonnull HttpResponse httpResponse, int statusCode) {
        super(message, cause);
        this.httpResponse = httpResponse;
        this.statusCode = statusCode;
    }

    protected JiveCoreException(@Nullable ErrorEntity errorEntity, @Nonnull HttpResponse httpResponse, int statusCode) {
        this(nullableErrorEntityToString(errorEntity), null, httpResponse, statusCode);
    }

    protected JiveCoreException(@Nullable ErrorEntity errorEntity, @Nullable Throwable cause, @Nonnull HttpResponse httpResponse, int statusCode) {
        this(nullableErrorEntityToString(errorEntity), cause, httpResponse, statusCode);
    }

    @Nonnull
    private static String nullableErrorEntityToString(@Nullable ErrorEntity errorEntity) {
        if (errorEntity == null) {
            return "no error entity";
        } else {
            String errorEntityString = new JiveGson().toJson(errorEntity);
            return errorEntityString;
        }
    }
}
