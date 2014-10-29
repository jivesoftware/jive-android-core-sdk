package com.jivesoftware.android.mobile.sdk.entity.value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Convenience class for obtaining value instances of specific types from arbitrary strings.
 */
@ParametersAreNonnullByDefault
public class JiveCoreValueFactory {

    private JiveCoreValueFactory() {
        // Prevent construction
    }

    @Nonnull
    public static JiveCoreContentTypeValue createContentTypeValue(String stringValue) {
        JiveCoreContentTypeValue value = createEnumValueOrNull(JiveCoreContentType.class, stringValue);
        return value == null ? new JiveCoreValueImpl(stringValue) : value;
    }

    @Nonnull
    public static JiveCoreDirectiveValue createDirectiveValue(String stringValue) {
        JiveCoreDirective value = createEnumValueOrNull(JiveCoreDirective.class, stringValue);
        return value == null ? new JiveCoreValueImpl(stringValue) : value;
    }

    @Nonnull
    public static JiveCoreEntryStateValue createEntryStateValue(String stringValue) {
        JiveCoreEntryStateValue value = createEnumValueOrNull(JiveCoreEntryState.class, stringValue);
        return value == null ? new JiveCoreValueImpl(stringValue) : value;
    }

    @Nonnull
    public static JiveCoreOutcomeTypeValue createOutcomeTypeValue(String stringValue) {
        JiveCoreOutcomeTypeValue value = createEnumValueOrNull(JiveCoreOutcomeType.class, stringValue);
        return value == null ? new JiveCoreValueImpl(stringValue) : value;
    }

    @Nonnull
    public static JiveCorePlaceTypeValue createPlaceTypeValue(String stringValue) {
        JiveCorePlaceType value = createEnumValueOrNull(JiveCorePlaceType.class, stringValue);
        return value == null ? new JiveCoreValueImpl(stringValue) : value;
    }

    @Nonnull
    public static JiveCoreRelationshipValue createRelationshipValue(String stringValue) {
        JiveCoreRelationshipValue value = createEnumValueOrNull(JiveCoreRelationship.class, stringValue);
        return value == null ? new JiveCoreValueImpl(stringValue) : value;
    }

    @Nonnull
    public static JiveCoreSortValue createSortValue(String stringValue) {
        JiveCoreSortValue value = createEnumValueOrNull(JiveCoreSort.class, stringValue);
        return value == null ? new JiveCoreValueImpl(stringValue) : value;
    }

    @Nonnull
    public static JiveCoreStatusValue createStatusValue(String stringValue) {
        JiveCoreStatusValue value = createEnumValueOrNull(JiveCoreStatus.class, stringValue);
        return value == null ? new JiveCoreValueImpl(stringValue) : value;
    }

    @Nonnull
    public static JiveCoreTypeValue createTypeValue(String stringValue) {
        JiveCoreTypeValue value = createEnumValueOrNull(JiveCoreContentType.class, stringValue);
        if (value == null) {
            value = createEnumValueOrNull(JiveCorePlaceType.class, stringValue);
        }
        return value == null ? new JiveCoreValueImpl(stringValue) : value;
    }

    @Nonnull
    public static JiveCoreVerbValue createVerbValue(String stringValue) {
        JiveCoreVerb representation = JiveCoreVerb.getByRepresentation(stringValue);
        return representation == null ? new JiveCoreValueImpl(stringValue) : representation;
    }

    @Nonnull
    public static JiveCoreProductTypeValue createProductTypeValue(String stringValue) {
        JiveCoreProductType representation = JiveCoreProductType.getByRepresentation(stringValue);
        return representation == null ? new JiveCoreValueImpl(stringValue) : representation;
    }

    @Nullable
    private static <E extends Enum<E>> E createEnumValueOrNull(Class<E> enumType, String value) {
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
