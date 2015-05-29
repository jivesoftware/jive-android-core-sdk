package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.Nonnull;

/**
 * Convenience class for representing values not in the common sets.
 */
class JiveCoreValueImpl implements JiveCoreDirectiveValue, JiveCoreEntryStateValue,
        JiveCoreOutcomeTypeValue, JiveCoreRelationshipValue, JiveCoreSortValue,
        JiveCoreStatusValue, JiveCoreVerbValue, JiveCoreProductTypeValue,
        JiveCoreModerationStateValue, JiveCoreModerationTargetTypeValue {

    @Nonnull
    private final String representation;

    JiveCoreValueImpl(String representation) {
        if (representation == null) {
            throw(new IllegalArgumentException("value may not be null"));
        }
        this.representation = representation;
    }

    @Override
    @JsonValue
    public String toString() {
        return representation;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass") // We are simply encapsulating the representation
    @Override
    public boolean equals(Object o) {
        return this == o || representation.equals(o.toString());
    }

    @Override
    public int hashCode() {
        return representation.hashCode();
    }

}
