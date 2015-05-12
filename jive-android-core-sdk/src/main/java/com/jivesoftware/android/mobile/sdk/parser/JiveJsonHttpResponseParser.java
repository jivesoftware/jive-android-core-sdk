package com.jivesoftware.android.mobile.sdk.parser;

import com.fasterxml.jackson.core.type.TypeReference;
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
    @Nullable
    private final Class<E> entityClass;
    @Nullable
    private final TypeReference<E> typeReference;

    public JiveJsonHttpResponseParser(
            JiveCoreExceptionFactory jiveCoreExceptionFactory,
            JiveJson jiveJson,
            @Nullable Class<E> entityClass,
            @Nullable TypeReference<E> typeReference) {
        super(jiveCoreExceptionFactory);
        this.jiveJson = jiveJson;
        this.entityClass = entityClass;
        this.typeReference = typeReference;
    }

    @Nullable
    @Override
    protected E parseContentInputStreamedResponseBeforeClosingContentInputStream(
            HttpResponse httpResponse,
            int statusCode,
            HttpEntity httpEntity,
            InputStream contentInputStream) throws IOException {
        E entity;
        if (null != entityClass) {
            entity = jiveJson.fromJson(contentInputStream, entityClass);
        } else {
            entity = jiveJson.fromJson(contentInputStream, typeReference);
        }
        return entity;
    }
}
