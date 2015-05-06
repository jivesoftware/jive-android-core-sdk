package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Tagging interface for status instances.
 */
@JsonDeserialize(converter = JiveCoreStatusValue.Converter.class)
public interface JiveCoreStatusValue {

    /**
     * Get the status name.
     *
     * @return name
     */
    @Override
    String toString();

    /**
     * Converter used to deserialize as common type instances when the value matches, falling back
     * to deserializing as an arbitrary value when an unknown value is encountered.
     */
    class Converter extends StdConverter<String, JiveCoreStatusValue> {
        @Override
        public JiveCoreStatusValue convert(String stringValue) {
            return JiveCoreValueFactory.createStatusValue(stringValue);
        }
    }

}
