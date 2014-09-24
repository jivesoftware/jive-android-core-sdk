package com.jivesoftware.android.mobile.sdk.core.options;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This only has one subclass right now, but will be required in the future by other classes (pulled from jive-ios-sdk):
 * JiveDateLimitedRequestOptions
 * JiveTrendingPeopleRequestOptions
 */
public class JiveCoreCountRequestOptions extends JiveCoreReturnFieldsRequestOptions {
    private int count;

    public void setCount(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Invalid count: " + count);
        } else {
            this.count = count;
        }
    }

    public int getCount() {
        return count;
    }

    @Override
    public LinkedHashMap<String, List<String>> copyQueryParameters() {
        LinkedHashMap<String, List<String>> queryParameters = super.copyQueryParameters();
        if (count > 0) {
            ArrayList<String> list = new ArrayList<String>();
            list.add(Integer.toString(count));
            queryParameters.put("count", list);
        }
        return queryParameters;
    }
}
