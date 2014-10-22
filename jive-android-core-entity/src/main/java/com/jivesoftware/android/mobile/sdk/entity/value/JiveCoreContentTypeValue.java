package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Tagging interface for content type instances.
 */
@JsonDeserialize(converter = JiveCoreContentTypeValue.Converter.class)
public interface JiveCoreContentTypeValue extends JiveCoreTypeValue {

    /**
     * Get the content type name.
     *
     * @return name
     */
    @Override
    String toString();

    /**
     * Converter used to deserialize as common type instances when the value matches, falling back
     * to deserializing as an arbitrary value when an unknown value is encountered.
     */
    public static class Converter extends StdConverter<String, JiveCoreContentTypeValue> {
        @Override
        public JiveCoreContentTypeValue convert(String stringValue) {
            return JiveCoreValueFactory.createContentTypeValue(stringValue);
        }
    }

}
