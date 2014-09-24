package com.jivesoftware.android.mobile.sdk.http;

import com.jivesoftware.android.mobile.sdk.gson.JiveGson;
import org.apache.http.entity.StringEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.UnsupportedEncodingException;

public class JsonEntity extends StringEntity {
    public JsonEntity(@Nonnull String json) throws UnsupportedEncodingException {
        super(json, "UTF-8");
        setContentType("application/json");
    }

    @Nonnull
    public static JsonEntity from(@Nullable Object src) {
        String json = new JiveGson().toJson(src);

        JsonEntity jsonEntity;
        try {
            jsonEntity = new JsonEntity(json);

        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UnsupportedEncodingException should be impossible since we just encoded the String", e);
        }
        return jsonEntity;
    }
}
