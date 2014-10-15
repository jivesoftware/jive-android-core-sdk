package com.jivesoftware.android.mobile.sdk.core.options;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Commonly used verbs.
 */
@ParametersAreNonnullByDefault
public enum JiveCoreVerb implements JiveCoreVerbValue {

    bookmarked("jive:bookmarked"),

    created("jive:created"),

    commented("jive:commented"),

    correct_answer_set("jive:correct_answer_set"),

    joined("jive:joined"),

    liked("jive:liked"),

    mentioned("jive:mentioned"),

    modified("jive:modified"),

    moved("jive:moved"),

    notification("jive:notification"),

    outcome_set("jive:outcome_set"),

    outlook_install_client("uri:jiveName:outlook_install_client"),

    promoted("jive:promoted"),

    replied("jive:replied"),

    repost("jive:repost"),

    shared("jive:shared");


    private static final Map<String, JiveCoreVerb> BY_REPRESENTATION;
    static {
        Map<String, JiveCoreVerb> map = new HashMap<String, JiveCoreVerb>();
        for (JiveCoreVerb verb : values()) {
            map.put(verb.representation, verb);
        }
        BY_REPRESENTATION = Collections.unmodifiableMap(map);
    }

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
     * Gets the verb associated with the type representation string (e.g., {@code jive:replied}).
     *
     * @param string string
     * @return verb instance or {@code null} if not found
     */
    @Nullable
    public static JiveCoreVerbValue fromRepresentation(@Nullable String string) {
        if (string == null) {
            return null;
        } else {
            JiveCoreVerb jiveCoreVerb = BY_REPRESENTATION.get(string);
            if (jiveCoreVerb == null) {
                return new ArbitraryJiveCoreVerbValue(string);
            } else {
                return jiveCoreVerb;
            }
        }
    }

    /**
     * Convenience class to represent values that are not part of the common set.
     */
    @ParametersAreNonnullByDefault
    public static class ArbitraryJiveCoreVerbValue implements JiveCoreVerbValue {

        private final String representation;

        public ArbitraryJiveCoreVerbValue(String representation) {
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

            ArbitraryJiveCoreVerbValue that = (ArbitraryJiveCoreVerbValue) o;

            if (!representation.equals(that.representation)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return representation.hashCode();
        }

    }

}

