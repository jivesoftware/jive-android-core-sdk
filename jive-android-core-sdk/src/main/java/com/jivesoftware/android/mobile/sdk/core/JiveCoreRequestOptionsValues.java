package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.util.Joiner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * General purpose options value container.  Used to maintain distinction between filters, directives, and
 * query parameters while using this as an internal construct to be able to present {@code JiveCoreRequestOptions}
 * in both mutable and immutable forms.
 */
@ParametersAreNonnullByDefault
final class JiveCoreRequestOptionsValues implements JiveCoreQueryParameterProvider {

    private final LinkedHashMap<String, List<String>> filters = new LinkedHashMap<String, List<String>>();
    private final LinkedHashMap<String, List<String>> directives = new LinkedHashMap<String, List<String>>();
    private final LinkedHashMap<String, List<String>> queryParameters = new LinkedHashMap<String, List<String>>();

    public void removeFilter(String name) {
        filters.remove(name);
    }

    public void putFilter(String name, List<String> values) {
        filters.put(name, values);
    }

    public void removeDirective(String name) {
        directives.remove(name);
    }

    public void putDirective(String name, List<String> values) {
        directives.put(name, values);
    }

    public void removeQueryParameter(String name) {
        queryParameters.remove(name);
    }

    public void putQueryParameter(String name, List<String> values) {
        queryParameters.put(name, values);
    }

    /**
     * Applies all options defined in the provided options object to the local instance, overwriting
     * any pre-existing values.
     *
     * @param other instance to pull values from
     */
    public void overlay(JiveCoreRequestOptionsValues other) {
        overlayMap(filters, other.filters);
        overlayMap(directives, other.directives);
        overlayMap(queryParameters, other.queryParameters);
    }

    private void overlayMap(Map<String, List<String>> localMap, Map<String, List<String>> withMap) {
        for (Map.Entry<String, List<String>> entry : withMap.entrySet()) {
            localMap.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    @Nonnull
    public LinkedHashMap<String, List<String>> provideQueryParameters() {
        updateAllDerived();
        return queryParameters;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JiveCoreRequestOptionsValues that = (JiveCoreRequestOptionsValues) o;

        if (!directives.equals(that.directives)) return false;
        if (!filters.equals(that.filters)) return false;
        if (!queryParameters.equals(that.queryParameters)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = filters.hashCode();
        result = 31 * result + directives.hashCode();
        result = 31 * result + queryParameters.hashCode();
        return result;
    }

    @Override
    @Nonnull
    public String toString() {
        updateAllDerived();
        StringBuilder builder = new StringBuilder(getClass().getSimpleName()).append("{");
        boolean entryComma = false;
        for (Map.Entry<String, List<String>> entry : queryParameters.entrySet()) {
            if (entryComma) {
                builder.append(",");
            } else {
                entryComma = true;
            }
            builder.append(entry.getKey())
                    .append("=[");
            boolean valueComma = false;
            for (String value : entry.getValue()) {
                if (valueComma) {
                    builder.append(",");
                } else {
                    valueComma = true;
                }
                builder.append(value);
            }
            builder.append("]");
        }
        builder.append("}");
        return builder.toString();
    }

    private void updateAllDerived() {
        updateDerived("filter", filters);
        updateDerived("directive", directives);
    }

    private void updateDerived(String queryParameter, Map<String, List<String>> map) {
        if (map.isEmpty()) {
            queryParameters.remove(queryParameter);
        } else {
            queryParameters.put(queryParameter, buildValueList(map));
        }
    }

    @Nonnull
    private static List<String> buildValueList(Map<String, List<String>> map) {
        ArrayList<String> result = new ArrayList<String>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if (value.isEmpty()) {
                result.add(key);
            } else {
                String joined = Joiner.on(",").join(value);
                result.add(key + "(" + joined + ")");
            }
        }
        return result;
    }

}
