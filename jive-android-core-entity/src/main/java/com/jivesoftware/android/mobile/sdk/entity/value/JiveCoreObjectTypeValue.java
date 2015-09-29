package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

/**
 * Used in conjunction with the entity field data to agrregate useful information about the
 * object type subsystem.
 */
@ParametersAreNonnullByDefault
@JsonDeserialize(converter = JiveCoreObjectTypeValue.Converter.class)
public interface JiveCoreObjectTypeValue {

    /**
     * Object type name/identifier.
     */
    @Nonnull
    String name();

    /**
     * Determines whether this type is an alias or alternate representation of another {@code JiveCoreObjectTypeValue} instance.
     *
     * @return {@code true} if it is, {@code false} otherwise.
     */
    boolean isAlias();

    /**
     * Determines if the type is served by the {@code /contents} service endpoint.
     *
     * @return {@code true} if it is a content type, {@code false} otherwise
     */
    boolean isContentType();

    /**
     * Determines if the type is served by the {@code /places} service endpoint.
     *
     * @return {@code true} if it is a place type, {@code false} otherwise
     */
    boolean isPlaceType();

    /**
     * Determines if the type can be used as a filter value for the {@code /inbox} service endpoint.
     *
     * @return {@code true} if it is a inbox filter type, {@code false} otherwise
     */
    boolean isInboxFilterType();

    /**
     * Determines if the type can be used as a filter value for the {@code /activity/discover} service endpoint.
     *
     * @return {@code true} if it is a inbox filter type, {@code false} otherwise
     */
    boolean isTrendingFilterType();

    /**
     * Determines if the type can be used as a filter value for the {@code /search} service endpoint.
     *
     * @return {@code true} if it is a searchable type, {@code false} otherwise
     */
    boolean isSearchableType();

    /**
     * Determines if the type supports following.
     *
     * @return {@code true} if it does, {@code false} otherwise
     */
    boolean isFollowable();

    /**
     * If this object type is an alias for another type, returns the primary object type instance.
     *
     * @return the primary object type or {@code this} if not an alias
     */
    @Nonnull
    JiveCoreObjectTypeValue getPrimaryType();

    /**
     * Gets the additional object type values associated with this object type, if any.
     *
     * @return object type array or {@code null} if unknown / unassociated
     */
    @Nonnull
    Set<Integer> getObjectTypeAliases();

    /**
     * Gets the primary object type value, if defined.
     *
     * @return object type or {@code null} if unknown / unassociated
     */
    @Nullable
    Integer getObjectType();

    /**
     * Gets the {@code JiveObjectType} type used to reply to the the current instance.
     *
     * @return {@code JiveObjectType} of the reply, or {@code null} if replies are not supported
     */
    @Nullable
    JiveCoreObjectTypeValue getReplyType();

    class Converter extends StdConverter<String, JiveCoreObjectTypeValue> {
        @Override
        public JiveCoreObjectTypeValue convert(String stringValue) {
            return JiveCoreValueFactory.createObjectTypeValue(stringValue);
        }
    }

}
