package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.json.JiveJson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStream;

@ParametersAreNonnullByDefault
public class JiveJsonHttpResponseParser<E> extends InputStreamClosingHttpResponseParser<E> {
    @Nonnull
    private final JiveJson jiveJson;
    @Nonnull
    private final Class<E> entityClass;

    public JiveJsonHttpResponseParser(JiveCoreExceptionFactory jiveCoreExceptionFactory, Class<E> entityClass) {
        this(jiveCoreExceptionFactory, new JiveJson(), entityClass);
    }

    public JiveJsonHttpResponseParser(
            JiveCoreExceptionFactory jiveCoreExceptionFactory,
            JiveJson jiveJson,
            Class<E> entityClass) {
        super(jiveCoreExceptionFactory);
        this.jiveJson = jiveJson;
        this.entityClass = entityClass;
    }

    @Nullable
    @Override
    protected E parseContentInputStreamedResponseBeforeClosingContentInputStream(
            HttpResponse httpResponse,
            int statusCode,
            HttpEntity httpEntity,
            InputStream contentInputStream) throws IOException {
        E entity = jiveJson.fromJson(contentInputStream, entityClass);
        return entity;
    }
}
