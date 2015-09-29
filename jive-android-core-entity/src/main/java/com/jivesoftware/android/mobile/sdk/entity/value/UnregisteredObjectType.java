package com.jivesoftware.android.mobile.sdk.entity.value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

/**
 * Concrete implementation of the {@link JiveCoreObjectTypeValue} interface which returns the
 * name specified in the constructor and default values for everything else.

 */
class UnregisteredObjectType implements JiveCoreObjectTypeValue {

    @Nonnull
    private final String name;

    UnregisteredObjectType(@Nonnull String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Nonnull
    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean isAlias() {
        return false;
    }

    @Override
    public boolean isContentType() {
        return false;
    }

    @Override
    public boolean isPlaceType() {
        return false;
    }

    @Override
    public boolean isInboxFilterType() {
        return false;
    }

    @Override
    public boolean isTrendingFilterType() {
        return false;
    }

    @Override
    public boolean isSearchableType() {
        return false;
    }

    @Override
    public boolean isFollowable() {
        return false;
    }

    @Nonnull
    @Override
    public JiveCoreObjectTypeValue getPrimaryType() {
        return this;
    }

    @Nonnull
    @Override
    public Set<Integer> getObjectTypeAliases() {
        return Collections.emptySet();
    }

    @Nullable
    @Override
    public Integer getObjectType() {
        return null;
    }

    @Nullable
    @Override
    public JiveCoreObjectTypeValue getReplyType() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnregisteredObjectType that = (UnregisteredObjectType) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
