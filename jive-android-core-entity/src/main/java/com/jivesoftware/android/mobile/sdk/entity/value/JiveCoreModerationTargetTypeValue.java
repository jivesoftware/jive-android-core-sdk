package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Tagging interface for moderation target type instances.
 */
@JsonDeserialize(converter = JiveCoreModerationTargetTypeValue.Converter.class)
public interface JiveCoreModerationTargetTypeValue {

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
    class Converter extends StdConverter<String, JiveCoreModerationTargetTypeValue> {
        @Override
        public JiveCoreModerationTargetTypeValue convert(String stringValue) {
            return JiveCoreValueFactory.createModerationTargetTypeValue(stringValue);
        }
    }

}
