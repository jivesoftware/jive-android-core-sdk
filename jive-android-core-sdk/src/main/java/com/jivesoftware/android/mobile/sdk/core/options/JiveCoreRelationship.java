package com.jivesoftware.android.mobile.sdk.core.options;

/**
 * Commonly used relationship types.
 */
public enum JiveCoreRelationship implements JiveCoreRelationshipValue {

    following,

    member,

    owner,

    participated,

    recentlyViewed;

    /**
     * Convenience method to create a relationship instance not in the common set.
     *
     * @param name relationship name
     * @return value instance
     */
    public static JiveCoreRelationshipValue fromString(final String name) {
        return new JiveCoreRelationshipValue() {
            @Override
            public String toString() {
                return name;
            }
        };
    }

}
