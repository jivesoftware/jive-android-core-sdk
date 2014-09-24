package com.jivesoftware.android.mobile.sdk.http;

import org.apache.http.entity.mime.content.StringBody;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class JsonBody extends StringBody {
    public static JsonBody create(
            final String json) throws IllegalArgumentException {
        try {
            return new JsonBody(json);
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException("Charset UTF-8 is not supported", ex);
        }
    }

    public JsonBody(String json) throws UnsupportedEncodingException {
        super(json, "application/json", Charset.forName("UTF-8"));
    }
}
