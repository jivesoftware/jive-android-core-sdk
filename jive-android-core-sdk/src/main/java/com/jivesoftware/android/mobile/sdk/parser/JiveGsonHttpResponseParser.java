package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.gson.JiveGson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;

public class JiveGsonHttpResponseParser<E> extends InputStreamClosingHttpResponseParser<E> {
    @Nonnull
    private final JiveGson jiveGson;
    @Nonnull
    private final Class<E> entityClass;

    public JiveGsonHttpResponseParser(@Nonnull JiveCoreExceptionFactory jiveCoreExceptionFactory, @Nonnull Class<E> entityClass) {
        this(jiveCoreExceptionFactory, new JiveGson(), entityClass);
    }

    public JiveGsonHttpResponseParser(@Nonnull JiveCoreExceptionFactory jiveCoreExceptionFactory, @Nonnull JiveGson jiveGson, @Nonnull Class<E> entityClass) {
        super(jiveCoreExceptionFactory);
        this.jiveGson = jiveGson;
        this.entityClass = entityClass;
    }

    @Nullable
    @Override
    protected E parseContentInputStreamedResponseBeforeClosingContentInputStream(
            @Nonnull HttpResponse httpResponse,
            int statusCode,
            @Nonnull HttpEntity httpEntity,
            @Nonnull InputStream contentInputStream) throws IOException {
        E entity = jiveGson.fromJson(contentInputStream, entityClass);
        return entity;
    }
}
