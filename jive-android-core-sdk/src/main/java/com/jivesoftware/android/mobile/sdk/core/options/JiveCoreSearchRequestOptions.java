package com.jivesoftware.android.mobile.sdk.core.options;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class JiveCoreSearchRequestOptions extends JiveCoreSortedRequestOptions {
    private List<String> searchTerms;

    public void setSearchTerms(List<String> searchTerms) {
        this.searchTerms = searchTerms;
    }

    public List<String> getSearchTerms() {
        return searchTerms;
    }

    protected ArrayList<String> copyFilters() {
        ArrayList<String> filters = new ArrayList<String>();
        if ((searchTerms != null) && !searchTerms.isEmpty()) {
            StringBuilder escapedJoinedSearchTerms = new StringBuilder("search(");
            boolean first = true;
            for (String searchTerm : searchTerms) {
                if (first) {
                    first = false;
                } else {
                    escapedJoinedSearchTerms.append(',');
                }
                for (int i = 0, l = searchTerm.length(); i < l; i++) {
                    char c = searchTerm.charAt(i);
                    switch (c) {
                        case '\\':
                        case '(':
                        case ')':
                        case ',':
                            escapedJoinedSearchTerms.append('\\');
                            break;
                        default:
                            // no escape
                            break;
                    }
                    escapedJoinedSearchTerms.append(c);
                }
            }
            escapedJoinedSearchTerms.append(')');

            filters.add(escapedJoinedSearchTerms.toString());
        }
        return filters;
    }

    @Override
    public LinkedHashMap<String, List<String>> copyQueryParameters() {
        LinkedHashMap<String, List<String>> queryParameters = super.copyQueryParameters();
        List<String> filters = copyFilters();

        if (filters.size() > 0) {
            queryParameters.put("filter", filters);
        }

        return queryParameters;
    }
}
