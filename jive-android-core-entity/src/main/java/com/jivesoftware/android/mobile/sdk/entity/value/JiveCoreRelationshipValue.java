package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Tagging interface for relationship type instances.
 */
@JsonDeserialize(converter = JiveCoreRelationshipValue.Converter.class)
public interface JiveCoreRelationshipValue {

    /**
     * Get the relationship name.
     *
     * @return name
     */
    String toString();

    /**
     * Converter used to deserialize as common type instances when the value matches, falling back
     * to deserializing as an arbitrary value when an unknown value is encountered.
     */
    public static class Converter extends StdConverter<String, JiveCoreRelationshipValue> {
        @Override
        public JiveCoreRelationshipValue convert(String stringValue) {
            return JiveCoreValueFactory.createRelationshipValue(stringValue);
        }
    }

}
