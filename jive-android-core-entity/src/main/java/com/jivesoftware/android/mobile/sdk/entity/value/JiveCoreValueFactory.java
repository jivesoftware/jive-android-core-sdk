package com.jivesoftware.android.mobile.sdk.entity.value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Convenience class for obtaining value instances of specific types from arbitrary strings.
 */
@ParametersAreNonnullByDefault
public class JiveCoreValueFactory {

    private static final CopyOnWriteArraySet<JiveCoreObjectTypeValue> SEARCHABLE_TYPES = new CopyOnWriteArraySet<JiveCoreObjectTypeValue>();
    private static final CopyOnWriteArraySet<JiveCoreObjectTypeValue> INBOX_TYPES = new CopyOnWriteArraySet<JiveCoreObjectTypeValue>();
    private static final CopyOnWriteArraySet<JiveCoreObjectTypeValue> TRENDING_TYPES = new CopyOnWriteArraySet<JiveCoreObjectTypeValue>();
    private static final ConcurrentHashMap<Integer, JiveCoreObjectTypeValue> OBJECT_TYPES = new ConcurrentHashMap<Integer, JiveCoreObjectTypeValue>();
    private static final ConcurrentHashMap<String, JiveCoreObjectTypeValue> NAMED_TYPES = new ConcurrentHashMap<String, JiveCoreObjectTypeValue>();

    static {
        for (JiveCoreObjectType type : JiveCoreObjectType.values()) {
            registerJiveObjectTypeValue(type);
        }
    }

    /**
     * Register an object type with the factory.
     *
     * @param type object type to register
     */
    public static void registerJiveObjectTypeValue(JiveCoreObjectTypeValue type) {
        Integer objectType = type.getObjectType();
        registerNameMapping(type);
        if (!type.isAlias()) {
            registerObjectTypeMapping(objectType, type);
            for (Integer objectTypeAlias : type.getObjectTypeAliases()) {
                registerObjectTypeMapping(objectTypeAlias, type);
            }
            if (type.isSearchableType()) {
                SEARCHABLE_TYPES.add(type);
            }
            if (type.isInboxFilterType()) {
                INBOX_TYPES.add(type);
            }
            if (type.isTrendingFilterType()) {
                TRENDING_TYPES.add(type);
            }
        }
    }

    private static void registerObjectTypeMapping(@Nullable Integer objectType, JiveCoreObjectTypeValue type) {
        if (objectType != null) {
            if (OBJECT_TYPES.putIfAbsent(objectType, type) != null) {
                throw (new IllegalStateException(
                        "Cannot register '" + type + "' since objectType '" + objectType + "' is already registered to: "
                                + OBJECT_TYPES.get(objectType)));
            }
        }
    }

    private static void registerNameMapping(JiveCoreObjectTypeValue type) {
        if (NAMED_TYPES.putIfAbsent(type.name(), type) != null) {
            throw (new IllegalStateException("objectType '" + type + "' already registered to: " + NAMED_TYPES.get(type.name())));
        }
    }

    private JiveCoreValueFactory() {
        // Prevent construction
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
    public static JiveCoreModerationStateValue createStateValue(String stringValue) {
        JiveCoreModerationStateValue value = createEnumValueOrNull(JiveCoreModerationState.class, stringValue);
        return value == null ? new JiveCoreValueImpl(stringValue) : value;
    }

    @Nonnull
    public static JiveCoreModerationTargetTypeValue createModerationTargetTypeValue(String stringValue) {
        JiveCoreModerationTargetType value = createEnumValueOrNull(JiveCoreModerationTargetType.class, stringValue);
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

    @Nonnull
    public static JiveCoreObjectTypeValue createObjectTypeValue(@Nullable String stringValue) {
        String lookup = (stringValue == null) ? "" : stringValue;
        if (lookup.startsWith("jive:")) {
            lookup = lookup.replaceFirst("jive:","");
        }

        JiveCoreObjectTypeValue typeValue = NAMED_TYPES.get(lookup);
        if (typeValue == null) {
            return new UnregisteredObjectType(lookup);
        } else {
            return typeValue;
        }
    }

    @Nullable
    private static String getObjectTypeFromInboxCollectionString(@Nullable String type) {
        if (type == null) {
            return null;
        }
        int idx = type.indexOf('-', 1);
        if (idx < 1) {
            return type;
        }
        return type.substring(0, idx);
    }

    @Nonnull
    public static JiveCoreObjectTypeValue createObjectTypeFromInboxCollectionString(@Nullable String collection) {
        String objectTypeString = getObjectTypeFromInboxCollectionString(collection);
        try {
            int objectType = Integer.parseInt(objectTypeString);
            JiveCoreObjectTypeValue itemType = createObjectTypeFromInteger(objectType);
            if (itemType != JiveCoreObjectType.UNKNOWN) {
                return itemType;
            }
        } catch (NumberFormatException nfx) {
            // Fall through
        }
        return createObjectTypeValue(objectTypeString);
    }

    @Nonnull
    public static JiveCoreObjectTypeValue createObjectTypeFromInteger(@Nullable Integer type) {
        JiveCoreObjectTypeValue itemType = type == null ? null : OBJECT_TYPES.get(type);
        return (itemType == null) ? JiveCoreObjectType.UNKNOWN : itemType;
    }

    /**
     * Get all searchable types excepting for those in the provided exclusion list.
     *
     * @return immutable iterable of searchable types
     */
    @Nonnull
    public static Iterable<JiveCoreObjectTypeValue> getSearchableTypes(JiveCoreObjectTypeValue... exclusions) {
        Set<JiveCoreObjectTypeValue> typeSet = new HashSet<JiveCoreObjectTypeValue>();
        typeSet.addAll(SEARCHABLE_TYPES);
        for (JiveCoreObjectTypeValue exclusion : exclusions) {
            typeSet.remove(exclusion);
        }
        return Collections.unmodifiableSet(typeSet);
    }

    /**
     * Get all inbox types.
     *
     * @return immutable iterable of all searchable types.
     */
    @Nonnull
    public static Iterable<JiveCoreObjectTypeValue> getInboxTypes() {
        return INBOX_TYPES;
    }

    /**
     * Get all trending/discovery types.
     *
     * @return immutable iterable of all trending/discovery types.
     */
    @Nonnull
    public static Iterable<? extends JiveCoreObjectTypeValue> getTrendingTypes() {
        return TRENDING_TYPES;
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
