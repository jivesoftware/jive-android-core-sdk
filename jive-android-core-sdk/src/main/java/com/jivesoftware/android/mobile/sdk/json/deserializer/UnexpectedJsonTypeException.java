package com.jivesoftware.android.mobile.sdk.json.deserializer;

import com.fasterxml.jackson.core.JsonProcessingException;

public class UnexpectedJsonTypeException extends JsonProcessingException {
    public UnexpectedJsonTypeException(String msg) {
        super(msg);
    }
}
