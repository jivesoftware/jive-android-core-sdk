package com.jivesoftware.android.mobile.sdk.core.options;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * Interface used by options instances to expose the query parameters which need to be applied to
 * the REST request URI.
 */
public interface JiveCoreQueryParameterProvider {

    /**
     * Get a map representing the query parameters and their values.
     *
     * @return query parameter map
     */
    @Nonnull
    Map<String, List<String>> provideQueryParameters();

}
