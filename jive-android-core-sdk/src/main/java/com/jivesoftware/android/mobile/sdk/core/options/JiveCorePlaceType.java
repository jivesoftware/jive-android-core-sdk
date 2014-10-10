package com.jivesoftware.android.mobile.sdk.core.options;

/**
 * Commonly used place types.
 */
public enum JiveCorePlaceType implements JiveCoreTypeValue {

    blog,

    group,

    project,

    space;

    /**
     * Convenience method to create a place type instance not in the common set.
     *
     * @param name place type name
     * @return value instance
     */
    public static JiveCoreTypeValue fromString(final String name) {
        return new JiveCoreTypeValue() {
            @Override
            public String toString() {
                return name;
            }
        };
    }

}

