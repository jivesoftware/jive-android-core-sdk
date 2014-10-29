package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Tagging interface for product type instances.
 */
@JsonDeserialize(converter = JiveCoreProductTypeValue.Converter.class)
public interface JiveCoreProductTypeValue {

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
    public static class Converter extends StdConverter<String, JiveCoreProductTypeValue> {
        @Override
        public JiveCoreProductTypeValue convert(String stringValue) {
            return JiveCoreValueFactory.createProductTypeValue(stringValue);
        }
    }

}
