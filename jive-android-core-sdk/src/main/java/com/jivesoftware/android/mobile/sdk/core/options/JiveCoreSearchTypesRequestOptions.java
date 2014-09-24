package com.jivesoftware.android.mobile.sdk.core.options;

import com.jivesoftware.android.mobile.sdk.util.Joiner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This only has one subclass right now, but will be required in the future by other classes (pulled from jive-ios-sdk):
 * JiveSearchContentRequestOptions
 */
public class JiveCoreSearchTypesRequestOptions extends JiveCoreSearchRequestOptions {
    private boolean collapsed;
    private List<String> types;
    private List<String> authorSelfUrls;

    public void setAuthorSelfUrls(List<String> authorSelfUrls) {
        this.authorSelfUrls = authorSelfUrls;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getTypes() {
        return types;
    }

    @Override
    protected ArrayList<String> copyFilters() {
        ArrayList<String> filters = super.copyFilters();

        if ((types != null) && !types.isEmpty()) {
            StringBuilder joinedTypes = new StringBuilder("type(");
            Joiner.on(",").appendTo(joinedTypes, types);
            joinedTypes.append(')');
            filters.add(joinedTypes.toString());
        }

        if ((authorSelfUrls != null) && !authorSelfUrls.isEmpty()) {
            StringBuilder joinedAuthorUris = new StringBuilder("author(");
            Joiner.on(",").appendTo(joinedAuthorUris, authorSelfUrls);
            joinedAuthorUris.append(')');
            filters.add(joinedAuthorUris.toString());
        }

        return filters;
    }

    @Override
    public LinkedHashMap<String, List<String>> copyQueryParameters() {
        LinkedHashMap<String, List<String>> queryParameters = super.copyQueryParameters();

        if (collapsed) {
            ArrayList<String> values = new ArrayList<String>();
            values.add("true");
            queryParameters.put("collapse", values);
        }

        return queryParameters;
    }
}
