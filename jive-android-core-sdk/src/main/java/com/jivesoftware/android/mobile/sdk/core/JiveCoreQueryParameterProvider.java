package com.jivesoftware.android.mobile.sdk.core;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.List;

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
    LinkedHashMap<String, List<String>> provideQueryParameters();

}
