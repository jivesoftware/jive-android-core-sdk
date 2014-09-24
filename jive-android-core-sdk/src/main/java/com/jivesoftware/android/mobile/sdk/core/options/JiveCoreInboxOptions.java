package com.jivesoftware.android.mobile.sdk.core.options;

import com.jivesoftware.android.mobile.sdk.util.Joiner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class JiveCoreInboxOptions extends JiveCoreDateLimitedRequestOptions {
    private boolean unread;
    private String authorPathAndQuery;
    private List<String> types;
    private List<String> directives;
    private boolean oldestUnread;
    private List<String> collapseSkipCollectionIds;

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public void setAuthorPathAndQuery(String authorPathAndQuery) {
        this.authorPathAndQuery = authorPathAndQuery;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void setDirectives(List<String> directives) {
        this.directives = directives;
    }

    public void setOldestUnread(boolean oldestUnread) {
        this.oldestUnread = oldestUnread;
    }

    public void setCollapseSkipCollectionIds(List<String> collapseSkipCollectionIds) {
        this.collapseSkipCollectionIds = collapseSkipCollectionIds;
    }

    @Override
    public LinkedHashMap<String, List<String>> copyQueryParameters() {
        LinkedHashMap<String, List<String>> queryParameters = super.copyQueryParameters();

        List<String> filters = new ArrayList<String>();
        if (unread) {
            filters.add("unread");
        }

        if (authorPathAndQuery != null) {
            String authorFilter = "author(" + authorPathAndQuery + ")";
            filters.add(authorFilter);
        }

        if (types != null) {
            String typeFilter = "type(" + Joiner.on(",").join(types) + ")";
            filters.add(typeFilter);
        }

        if (filters.size() > 0) {
            queryParameters.put("filter", filters);
        }

        List<String> directivesAndCollapseSkipDirective = new ArrayList<String>();
        if (directives != null) {
            directivesAndCollapseSkipDirective.addAll(directives);
        }
        if (collapseSkipCollectionIds != null) {
            String collapseSkipDirective = "collapseSkip(" + Joiner.on(",").join(collapseSkipCollectionIds) + ")";
            directivesAndCollapseSkipDirective.add(collapseSkipDirective);
        }
        if (directivesAndCollapseSkipDirective.size() > 0) {
            String directivesAndCollapseSkipDirectiveValue = Joiner.on(",").join(directivesAndCollapseSkipDirective);
            queryParameters.put("directive", Collections.singletonList(directivesAndCollapseSkipDirectiveValue));
        }

        if (oldestUnread) {
            queryParameters.put("oldestUnread", Collections.singletonList("true"));
        }

        return queryParameters;
    }
}
