package com.jivesoftware.android.mobile.sdk.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import com.jivesoftware.android.mobile.sdk.gson.adapter.ContentEntityTypeAdapterFactory;
import com.jivesoftware.android.mobile.sdk.gson.adapter.ErrorEntityAdapter;
import com.jivesoftware.android.mobile.sdk.gson.adapter.Iso8601DateAdapter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

public class JiveGson {
    @Nonnull
    private final Gson gson;

    public JiveGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder
                .registerTypeAdapterFactory(new ContentEntityTypeAdapterFactory())
                .registerTypeAdapter(ErrorEntity.class, new ErrorEntityAdapter())
                .registerTypeAdapter(Date.class, new Iso8601DateAdapter());
        this.gson = gsonBuilder.create();
    }

    public <T> T fromJson(String json, Class<T> clazz) throws IOException {
        try {
            T object = gson.fromJson(json, clazz);
            return object;
        } catch (JsonIOException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            } else {
                throw new IOException(e);
            }
        } catch (JsonParseException e) {
            throw new InvalidJsonException(e);
        }
    }

    public <T> T fromJsonElement(JsonElement jsonElement, Class<T> clazz) throws IOException {
        try {
            T object = gson.fromJson(jsonElement, clazz);
            return object;
        } catch (JsonIOException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            } else {
                throw new IOException(e);
            }
        } catch (JsonParseException e) {
            throw new InvalidJsonException(e);
        }
    }

    public <T> T fromJson(InputStream inputStream, Class<T> clazz) throws IOException {
        JiveJsonInputStream jiveJsonInputStream = new JiveJsonInputStream(inputStream);
        InputStreamReader inputStreamReader = new InputStreamReader(jiveJsonInputStream);
        try {
            T object = gson.fromJson(inputStreamReader, clazz);
            return object;
        } catch (JsonIOException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            } else {
                throw new IOException(e);
            }
        } catch (JsonParseException e) {
            throw new InvalidJsonException(e);
        }
    }

    public String toJson(Object src) {
        try {
            String json = gson.toJson(src);
            return json;
        } catch (JsonIOException e) {
            throw new IllegalStateException("This shouldn't be possible because we're writing to memory", e);
        }
    }
}
