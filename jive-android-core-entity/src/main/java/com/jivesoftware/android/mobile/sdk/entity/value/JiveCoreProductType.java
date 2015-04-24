package com.jivesoftware.android.mobile.sdk.entity.value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Commonly used product types.
 */
@ParametersAreNonnullByDefault
public enum JiveCoreProductType implements JiveCoreProductTypeValue {

    on_prem("on-prem"),

    cloud("cloud"),

    cloudExternal("cloud-external");



    private static final Map<String, JiveCoreProductType> BY_REPRESENTATION;
    static {
        Map<String, JiveCoreProductType> map = new HashMap<String, JiveCoreProductType>();
        for (JiveCoreProductType verb : values()) {
            map.put(verb.representation, verb);
        }
        BY_REPRESENTATION = Collections.unmodifiableMap(map);
    }

    @Nonnull
    private final String representation;

    JiveCoreProductType(String representation) {
        this.representation = representation;
    }

    @Override
    public String toString() {
        return representation;
    }

    @Nullable
    public static JiveCoreProductType getByRepresentation(String stringForm) {
        return BY_REPRESENTATION.get(stringForm);
    }

}

