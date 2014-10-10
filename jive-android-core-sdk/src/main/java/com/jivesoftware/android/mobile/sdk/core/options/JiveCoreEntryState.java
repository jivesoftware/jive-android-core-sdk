package com.jivesoftware.android.mobile.sdk.core.options;

/**
 * Commonly used entry states.
 */
public enum JiveCoreEntryState implements JiveCoreEntryStateValue {

    awaiting_action,

    hidden,

    ignored;

    /**
     * Convenience method to create an entryState instance not in the common set.
     *
     * @param name entryState name
     * @return value instance
     */
    public static JiveCoreEntryStateValue fromString(final String name) {
        return new JiveCoreEntryStateValue() {
            @Override
            public String toString() {
                return name;
            }
        };
    }

}
