package com.jivesoftware.android.mobile.sdk.gson.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.jivesoftware.android.mobile.sdk.util.DateFormatUtil;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

public class Iso8601DateAdapter implements JsonDeserializer<Date>, JsonSerializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String stringValue = json.getAsString();
        try {
            return DateFormatUtil.getGmtIso8601DateFormat().parse(stringValue);
        } catch (ParseException e) {
            throw new JsonSyntaxException(stringValue, e);
        }
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(DateFormatUtil.getGmtIso8601DateFormat().format(src));
    }
}
