package com.jivesoftware.android.mobile.sdk.core.options;

import java.util.ArrayList;

public class JiveCoreSearchPeopleRequestOptions extends JiveCoreSearchRequestOptions {
    private boolean nameOnly;

    public void setNameOnly(boolean nameOnly) {
        this.nameOnly = nameOnly;
    }

    public boolean isNameOnly() {
        return nameOnly;
    }

    @Override
    protected ArrayList<String> copyFilters() {
        ArrayList<String> filters = super.copyFilters();
        if (nameOnly) {
            filters.add("nameonly");
        }
        return filters;
    }


}
