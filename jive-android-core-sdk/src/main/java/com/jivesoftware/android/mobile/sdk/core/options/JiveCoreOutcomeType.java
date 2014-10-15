package com.jivesoftware.android.mobile.sdk.core.options;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Commonly used outcome types.
 */
public enum JiveCoreOutcomeType implements JiveCoreOutcomeTypeValue {

    decision,

    finalized,

    pending,

    helpful,

    resolved,

    success,

    outdated,

    official,

    wip;

    /**
     * Gets the verb associated with the type representation string (e.g., {@code helpful}).
     *
     * @param string string
     * @return verb instance or {@code null} if not found
     */
    @Nullable
    public static JiveCoreOutcomeTypeValue fromRepresentation(@Nullable String string) {
        if (string == null) {
            return null;
        } else {
            try {
                return JiveCoreOutcomeType.valueOf(string);
            } catch (IllegalArgumentException e) {
                return new ArbitraryJiveCoreOutcomeTypeValue(string);
            }
        }
    }

    /**
     * Convenience class to represent values that are not part of the common set.
     */
    @ParametersAreNonnullByDefault
    public static class ArbitraryJiveCoreOutcomeTypeValue implements JiveCoreOutcomeTypeValue {

        private final String representation;

        public ArbitraryJiveCoreOutcomeTypeValue(String representation) {
            this.representation = representation;
        }

        @Override
        public String toString() {
            return representation;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ArbitraryJiveCoreOutcomeTypeValue that = (ArbitraryJiveCoreOutcomeTypeValue) o;

            if (!representation.equals(that.representation)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return representation.hashCode();
        }

    }

}
