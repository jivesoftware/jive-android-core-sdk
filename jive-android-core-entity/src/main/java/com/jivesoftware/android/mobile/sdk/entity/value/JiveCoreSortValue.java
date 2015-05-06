package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Tagging interface for sort type instances.
 */
@JsonDeserialize(converter = JiveCoreSortValue.Converter.class)
public interface JiveCoreSortValue {

    /**
     * Get the sort name.
     *
     * @return name
     */
    @Override
    String toString();

    /**
     * Converter used to deserialize as common type instances when the value matches, falling back
     * to deserializing as an arbitrary value when an unknown value is encountered.
     */
    class Converter extends StdConverter<String, JiveCoreSortValue> {
        @Override
        public JiveCoreSortValue convert(String stringValue) {
            return JiveCoreValueFactory.createSortValue(stringValue);
        }
    }

}
