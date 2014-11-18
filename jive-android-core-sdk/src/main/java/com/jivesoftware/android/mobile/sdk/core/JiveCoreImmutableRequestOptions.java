package com.jivesoftware.android.mobile.sdk.core;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Immutable form of the {@code JiveCoreRequestOptions}.
 */
@ParametersAreNonnullByDefault
public final class JiveCoreImmutableRequestOptions implements JiveCoreQueryParameterProvider {

    @Nonnull
    final JiveCoreRequestOptionsValues values;

    public JiveCoreImmutableRequestOptions(JiveCoreRequestOptionsValues values) {
        this.values = values;
    }

    @Override
    @Nonnull
    public LinkedHashMap<String, List<String>> provideQueryParameters() {
        return values.provideQueryParameters();
    }

    @Override
    @Nonnull
    public String toString() {
        return "{" + getClass().getSimpleName() + " values=" + values + "}";
    }

}
