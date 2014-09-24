package com.jivesoftware.android.mobile.sdk.gson.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.jivesoftware.android.mobile.sdk.entity.ContentEntity;
import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;

import javax.annotation.Nonnull;
import java.io.IOException;

public class ContentEntityTypeAdapterFactory implements TypeAdapterFactory {

    public static class ContentEntityTypeAdapter extends TypeAdapter<ContentEntity> {
        @Nonnull
        private final TypeAdapter<ContentEntity> defaultContentEntityTypeAdapter;
        @Nonnull
        private final TypeAdapter<PersonEntity> personEntityTypeAdapter;

        public ContentEntityTypeAdapter(@Nonnull TypeAdapter<ContentEntity> defaultContentEntityTypeAdapter, @Nonnull TypeAdapter<PersonEntity> personEntityTypeAdapter) {
            this.defaultContentEntityTypeAdapter = defaultContentEntityTypeAdapter;
            this.personEntityTypeAdapter = personEntityTypeAdapter;
        }

        private Object[] deserializeStringArrayOrPersonEntityArray(JsonObject jsonObject, String jsonArrayName) throws JsonParseException {
            JsonArray jsonArray = jsonObject.getAsJsonArray(jsonArrayName);
            if (jsonArray == null) {
                return null;
            } else {
                int size = jsonArray.size();
                if (size > 0) {
                    JsonElement firstJsonElement = jsonArray.get(0);

                    Object[] array;
                    if (firstJsonElement.isJsonPrimitive()) {
                        array = new String[size];
                        String firstSelfURL = firstJsonElement.getAsString();
                        array[0] = firstSelfURL;
                        for (int i = 1, l = jsonArray.size(); i < l; i++) {
                            JsonElement userSelfURLJsonElement = jsonArray.get(i);
                            array[i] = userSelfURLJsonElement.getAsString();
                        }
                    } else if (firstJsonElement.isJsonObject()) {
                        array = new PersonEntity[size];
                        PersonEntity firstPersonEntity = personEntityTypeAdapter.fromJsonTree(firstJsonElement);
                        array[0] = firstPersonEntity;
                        for (int i = 1, l = jsonArray.size(); i < l; i++) {
                            JsonElement personEntityJsonElement = jsonArray.get(i);
                            PersonEntity personEntity = personEntityTypeAdapter.fromJsonTree(personEntityJsonElement);
                            array[i] = personEntity;
                        }
                    } else {
                        throw new JsonParseException("Expected Person entity or Person self URL");
                    }
                    return array;
                } else {
                    return new PersonEntity[0];
                }
            }
        }

        @Override
        public void write(JsonWriter out, ContentEntity value) throws IOException {
            defaultContentEntityTypeAdapter.write(out, value);
        }

        @Override
        public ContentEntity read(JsonReader in) throws IOException {
            JsonElement jsonElement = Streams.parse(in);
            ContentEntity contentEntity;
            try {
                contentEntity = defaultContentEntityTypeAdapter.fromJsonTree(jsonElement);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                contentEntity.participants = deserializeStringArrayOrPersonEntityArray(jsonObject, "participants");
                contentEntity.users = deserializeStringArrayOrPersonEntityArray(jsonObject, "users");
            } catch (JsonIOException e) {
                Throwable cause = e.getCause();
                if (cause instanceof IOException) {
                    throw (IOException) cause;
                } else {
                    throw e;
                }
            }
            return contentEntity;
        }

    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType() == ContentEntity.class) {
            TypeAdapter<ContentEntity> defaultContentEntityTypeAdapter = gson.getDelegateAdapter(this, new TypeToken<ContentEntity>() {
            });
            TypeAdapter<PersonEntity> personEntityTypeAdapter = gson.getAdapter(PersonEntity.class);
            return create(defaultContentEntityTypeAdapter, personEntityTypeAdapter);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> TypeAdapter<T> create(@Nonnull TypeAdapter<ContentEntity> defaultContentEntityTypeAdapter, @Nonnull TypeAdapter<PersonEntity> personEntityTypeAdapter) {
        return (TypeAdapter<T>) new ContentEntityTypeAdapter(defaultContentEntityTypeAdapter, personEntityTypeAdapter);
    }
}
