package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Tagging interface for verb instances.
 */
@JsonDeserialize(converter = JiveCoreVerbValue.Converter.class)
public interface JiveCoreVerbValue {

    /**
     * Get the verb name.
     *
     * @return name
     */
    @Override
    String toString();

    /**
     * Converter used to deserialize as common type instances when the value matches, falling back
     * to deserializing as an arbitrary value when an unknown value is encountered.
     */
    class Converter extends StdConverter<String, JiveCoreVerbValue> {
        @Override
        public JiveCoreVerbValue convert(String stringValue) {
            return JiveCoreValueFactory.createVerbValue(stringValue);
        }
    }

}
