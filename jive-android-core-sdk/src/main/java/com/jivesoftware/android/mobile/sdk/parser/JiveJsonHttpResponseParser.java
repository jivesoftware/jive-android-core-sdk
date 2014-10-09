package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.json.JiveJson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;

public class JiveJsonHttpResponseParser<E> extends InputStreamClosingHttpResponseParser<E> {
    @Nonnull
    private final JiveJson jiveJson;
    @Nonnull
    private final Class<E> entityClass;

    public JiveJsonHttpResponseParser(@Nonnull JiveCoreExceptionFactory jiveCoreExceptionFactory, @Nonnull Class<E> entityClass) {
        this(jiveCoreExceptionFactory, new JiveJson(), entityClass);
    }

    public JiveJsonHttpResponseParser(@Nonnull JiveCoreExceptionFactory jiveCoreExceptionFactory, @Nonnull JiveJson jiveJson, @Nonnull Class<E> entityClass) {
        super(jiveCoreExceptionFactory);
        this.jiveJson = jiveJson;
        this.entityClass = entityClass;
    }

    @Nullable
    @Override
    protected E parseContentInputStreamedResponseBeforeClosingContentInputStream(
            @Nonnull HttpResponse httpResponse,
            int statusCode,
            @Nonnull HttpEntity httpEntity,
            @Nonnull InputStream contentInputStream) throws IOException {
        E entity = jiveJson.fromJson(contentInputStream, entityClass);
        return entity;
    }
}
