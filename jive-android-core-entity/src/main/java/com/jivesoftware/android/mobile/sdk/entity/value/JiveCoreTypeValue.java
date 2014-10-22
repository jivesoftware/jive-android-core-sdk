package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Tagging interface for core type instances.
 */
@JsonDeserialize(converter = JiveCoreTypeValue.Converter.class)
public interface JiveCoreTypeValue {

    /**
     * Get the core type name.
     *
     * @return name
     */
    String toString();

    /**
     * Converter used to deserialize as common type instances when the value matches, falling back
     * to deserializing as an arbitrary value when an unknown value is encountered.
     */
    public static class Converter extends StdConverter<String, JiveCoreTypeValue> {
        @Override
        public JiveCoreTypeValue convert(String stringValue) {
            return JiveCoreValueFactory.createTypeValue(stringValue);
        }
    }

}
