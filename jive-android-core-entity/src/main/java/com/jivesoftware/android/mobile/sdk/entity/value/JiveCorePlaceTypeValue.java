package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Tagging interface for place type instances.
 */
@JsonDeserialize(converter = JiveCorePlaceTypeValue.Converter.class)
public interface JiveCorePlaceTypeValue extends JiveCoreTypeValue {

    /**
     * Get the place type name.
     *
     * @return name
     */
    @Override
    String toString();

    /**
     * Converter used to deserialize as common type instances when the value matches, falling back
     * to deserializing as an arbitrary value when an unknown value is encountered.
     */
    public static class Converter extends StdConverter<String, JiveCorePlaceTypeValue> {
        @Override
        public JiveCorePlaceTypeValue convert(String stringValue) {
            return JiveCoreValueFactory.createPlaceTypeValue(stringValue);
        }
    }

}
