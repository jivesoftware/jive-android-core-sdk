package com.jivesoftware.android.mobile.sdk.core.options;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Commonly used directives.
 */
@ParametersAreNonnullByDefault
public enum JiveCoreDirective implements JiveCoreDirectiveValue {

    include_rtc,

    collapse;

    // collapseSkip is handled separately in JiveCoreRequestOptions

    /**
     * Convenience method to create a directive instance not in the common set.
     *
     * @param name directive name
     * @return value instance
     */
    public static JiveCoreDirectiveValue fromString(final String name) {
        return new JiveCoreDirectiveValue() {
            @Override
            public String toString() {
                return name;
            }
        };
    }

}
