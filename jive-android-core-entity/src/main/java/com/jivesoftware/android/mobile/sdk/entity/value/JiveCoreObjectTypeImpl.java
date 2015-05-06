package com.jivesoftware.android.mobile.sdk.entity.value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a utility object used to associate various operational characteristics to object types.
 */
@ParametersAreNonnullByDefault
public class JiveCoreObjectTypeImpl implements JiveCoreObjectTypeValue {

    private final String name;
    private final boolean amAlias;
    private final boolean amContentType;
    private final boolean amPlaceType;
    private final boolean amInboxType;
    private final boolean amSearchableType;
    private final boolean amFollowableType;
    private final JiveCoreObjectTypeValue aliasOf;

    @Nullable
    private final Integer objectType;

    private final Set<Integer> objectTypeAliases;

    @Nullable
    private final JiveCoreObjectTypeValue replyType;

    JiveCoreObjectTypeImpl(JiveCoreObjectTypeBuilder builder) {
        this.name = builder.name;
        this.amAlias = builder.aliasOf != null;
        this.aliasOf = builder.getAliasOf();
        this.amContentType = builder.isContentType();
        this.amPlaceType = builder.isPlaceType();
        this.amInboxType = builder.isInboxFilterType();
        this.amSearchableType = builder.isSearchableType();
        this.amFollowableType = builder.isFollowable();
        this.objectType = builder.getObjectType();
        this.objectTypeAliases = builder.getObjectTypeAliases();
        this.replyType = builder.getItemReplyType();
    }

    /**
     * We give ourselves a special nonnull value since we intercept this in our enum definition as
     * we delegate.
     */
    static JiveCoreObjectTypeBuilder builder() {
        return new JiveCoreObjectTypeBuilder("");
    }

    public static JiveCoreObjectTypeBuilder builder(String name) {
        if (name.isEmpty()) {
            throw new IllegalStateException("Name must not be empty");
        }
        return new JiveCoreObjectTypeBuilder(name);
    }

    @Nonnull
    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean isAlias() {
        return amAlias;
    }

    @Nonnull
    @Override
    public JiveCoreObjectTypeValue getPrimaryType() {
        if (aliasOf == null) {
            return this;
        } else {
            return aliasOf.getPrimaryType();
        }
    }

    @Override
    public boolean isContentType() {
        return amContentType;
    }

    @Override
    public boolean isPlaceType() {
        return amPlaceType;
    }

    @Override
    public boolean isInboxFilterType() {
        return amInboxType;
    }

    @Override
    public boolean isSearchableType() {
        return amSearchableType;
    }

    @Override
    public boolean isFollowable() {
        return amFollowableType;
    }

    @Nullable
    @Override
    public Integer getObjectType() {
        return objectType;
    }

    @Nonnull
    @Override
    public Set<Integer> getObjectTypeAliases() {
        return objectTypeAliases;
    }

    @Nullable
    @Override
    public JiveCoreObjectTypeValue getReplyType() {
        return replyType;
    }

    public static class JiveCoreObjectTypeBuilder {

        @Nonnull
        String name;

        @Nullable
        JiveCoreObjectTypeValue aliasOf;

        @Nullable
        JiveCoreObjectTypeValue itemReplyType;

        @Nullable
        Integer objectType;

        @Nullable
        Set<Integer> objectTypeAliases;

        @Nullable
        Boolean inboxFilterType;

        @Nullable
        Boolean contentType;

        @Nullable
        Boolean placeType;

        @Nullable
        Boolean searchableType;

        @Nullable
        Boolean followable;

        protected JiveCoreObjectTypeBuilder(@Nonnull String name) {
            this.name = name;
        }

        public JiveCoreObjectTypeBuilder setAliasOf(JiveCoreObjectTypeValue otherType) {
            this.aliasOf = otherType;
            return this;
        }

        public JiveCoreObjectTypeBuilder setInboxFilterType() {
            inboxFilterType = true;
            return this;
        }

        public JiveCoreObjectTypeBuilder setContentType() {
            contentType = true;
            return this;
        }

        public JiveCoreObjectTypeBuilder setPlaceType() {
            placeType = true;
            return this;
        }

        public JiveCoreObjectTypeBuilder setSearchableType() {
            searchableType = true;
            return this;
        }

        public JiveCoreObjectTypeBuilder setFollowableType() {
            followable = true;
            return this;
        }

        public JiveCoreObjectTypeBuilder setItemReplyType(JiveCoreObjectTypeValue itemReplyType) {
            this.itemReplyType = itemReplyType;
            return this;
        }

        public JiveCoreObjectTypeBuilder setObjectType(int objectType, @Nullable int... objectTypeAliases) {
            this.objectType = objectType;
            if (objectTypeAliases == null) {
                this.objectTypeAliases = Collections.emptySet();
            } else {
                HashSet<Integer> set = new HashSet<Integer>();
                for (int alias : objectTypeAliases) {
                    set.add(alias);
                }
                this.objectTypeAliases = Collections.unmodifiableSet(set);
            }
            return this;
        }

        @Nullable
        JiveCoreObjectTypeValue getAliasOf() {
            return aliasOf;
        }

        @Nullable
        JiveCoreObjectTypeValue getItemReplyType() {
            if (itemReplyType == null && aliasOf != null) {
                return aliasOf.getReplyType();
            } else {
                return itemReplyType;
            }
        }

        @Nullable
        Integer getObjectType() {
            if (objectType == null && aliasOf != null) {
                return aliasOf.getObjectType();
            } else {
                return objectType;
            }
        }

        @Nonnull
        Set<Integer> getObjectTypeAliases() {
            if (objectTypeAliases == null && aliasOf != null) {
                return aliasOf.getObjectTypeAliases();
            } else if (objectTypeAliases == null) {
                return Collections.emptySet();
            } else {
                return objectTypeAliases;
            }
        }

        boolean isInboxFilterType() {
            if (inboxFilterType == null && aliasOf != null) {
                return aliasOf.isInboxFilterType();
            } else if (inboxFilterType == null) {
                return false;
            } else {
                return inboxFilterType;
            }
        }

        boolean isContentType() {
            if (contentType == null && aliasOf != null) {
                return aliasOf.isContentType();
            } else if (contentType == null) {
                return false;
            } else {
                return contentType;
            }
        }

        boolean isPlaceType() {
            if (placeType == null && aliasOf != null) {
                return aliasOf.isPlaceType();
            } else if (placeType == null) {
                return false;
            } else {
                return placeType;
            }
        }

        boolean isSearchableType() {
            if (searchableType == null && aliasOf != null) {
                return aliasOf.isSearchableType();
            } else if (searchableType == null) {
                return false;
            } else {
                return searchableType;
            }
        }

        boolean isFollowable() {
            if (followable == null && aliasOf != null) {
                return aliasOf.isFollowable();
            } else if (followable == null) {
                return false;
            } else {
                return followable;
            }
        }

        public JiveCoreObjectTypeImpl build() {
            return new JiveCoreObjectTypeImpl(this);
        }

    }

}
