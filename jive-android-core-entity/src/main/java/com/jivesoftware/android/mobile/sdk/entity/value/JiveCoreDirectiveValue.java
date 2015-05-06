package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Tagging interface for directive instances.
 */
@JsonDeserialize(converter = JiveCoreDirectiveValue.Converter.class)
public interface JiveCoreDirectiveValue {

    /**
     * Get the directive name.
     *
     * @return name
     */
    @Override
    String toString();

    /**
     * Converter used to deserialize as common type instances when the value matches, falling back
     * to deserializing as an arbitrary value when an unknown value is encountered.
     */
    class Converter extends StdConverter<String, JiveCoreDirectiveValue> {
        @Override
        public JiveCoreDirectiveValue convert(String stringValue) {
            return JiveCoreValueFactory.createDirectiveValue(stringValue);
        }
    }

}
