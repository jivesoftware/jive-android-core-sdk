package com.jivesoftware.android.mobile.sdk.gson.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jivesoftware.android.mobile.sdk.entity.CodeErrorEntity;
import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import com.jivesoftware.android.mobile.sdk.entity.ObjectErrorEntity;
import com.jivesoftware.android.mobile.sdk.entity.SimpleErrorEntity;

import java.lang.reflect.Type;

public class ErrorEntityAdapter implements JsonDeserializer<ErrorEntity> {
    @Override
    public ErrorEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement error = jsonObject.get("error");
            Class<? extends ErrorEntity> type;
            if ( error == null ) {
                type = CodeErrorEntity.class;
            } else if ( error.isJsonPrimitive() ) {
                type = SimpleErrorEntity.class;
            } else {
                type = ObjectErrorEntity.class;
            }
            return context.deserialize(json, type);
        }
        catch ( Exception e ) {
            throw new JsonParseException("Unknown element type", e);
        }
    }
}
