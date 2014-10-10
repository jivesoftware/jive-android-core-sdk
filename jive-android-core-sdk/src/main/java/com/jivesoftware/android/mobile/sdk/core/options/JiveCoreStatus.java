package com.jivesoftware.android.mobile.sdk.core.options;

/**
 * Commonly used content status values.
 */
public enum JiveCoreStatus implements JiveCoreStatusValue {

    draft;

    /**
     * Convenience method to create a status value instance not in the common set.
     *
     * @param name status name
     * @return value instance
     */
    public static JiveCoreStatusValue fromString(final String name) {
        return new JiveCoreStatusValue() {
            @Override
            public String toString() {
                return name;
            }
        };
    }

}

