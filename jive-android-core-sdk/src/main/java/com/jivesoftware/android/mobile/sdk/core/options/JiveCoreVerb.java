package com.jivesoftware.android.mobile.sdk.core.options;

import javax.annotation.Nonnull;

/**
 * Commonly used verbs.
 */
public enum JiveCoreVerb implements JiveCoreVerbValue {

    created("jive:created"),

    promoted("jive:promoted");

    @Nonnull
    private final String representation;

    JiveCoreVerb(String representation) {
        this.representation = representation;
    }

    @Override
    public String toString() {
        return representation;
    }

    /**
     * Convenience method to create a verb value instance not in the common set.
     *
     * @param name verb name
     * @return value instance
     */
    public static JiveCoreVerbValue fromString(final String name) {
        return new JiveCoreVerbValue() {
            @Override
            public String toString() {
                return name;
            }
        };
    }

}

