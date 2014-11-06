package com.jivesoftware.android.mobile.sdk.core;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

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
    public Map<String, List<String>> provideQueryParameters() {
        return values.provideQueryParameters();
    }

    @Override
    @Nonnull
    public String toString() {
        return "{" + getClass().getSimpleName() + " values=" + values + "}";
    }

}
