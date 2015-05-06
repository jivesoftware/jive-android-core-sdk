package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Tagging interface for outcome type instances.
 */
@JsonDeserialize(converter = JiveCoreOutcomeTypeValue.Converter.class)
public interface JiveCoreOutcomeTypeValue {

    /**
     * Get the outcome type name.
     *
     * @return name
     */
    @Override
    String toString();

    /**
     * Converter used to deserialize as common type instances when the value matches, falling back
     * to deserializing as an arbitrary value when an unknown value is encountered.
     */
    class Converter extends StdConverter<String, JiveCoreOutcomeTypeValue> {
        @Override
        public JiveCoreOutcomeTypeValue convert(String stringValue) {
            return JiveCoreValueFactory.createOutcomeTypeValue(stringValue);
        }
    }

}
