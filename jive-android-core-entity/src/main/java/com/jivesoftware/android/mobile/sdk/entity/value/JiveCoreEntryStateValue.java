package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Tagging interface for entry state instances.
 */
@JsonDeserialize(converter = JiveCoreEntryStateValue.Converter.class)
public interface JiveCoreEntryStateValue {

    /**
     * Get the entry state name.
     *
     * @return name
     */
    @Override
    String toString();

    /**
     * Converter used to deserialize as common type instances when the value matches, falling back
     * to deserializing as an arbitrary value when an unknown value is encountered.
     */
    public static class Converter extends StdConverter<String, JiveCoreEntryStateValue> {
        @Override
        public JiveCoreEntryStateValue convert(String stringValue) {
            return JiveCoreValueFactory.createEntryStateValue(stringValue);
        }
    }

}
