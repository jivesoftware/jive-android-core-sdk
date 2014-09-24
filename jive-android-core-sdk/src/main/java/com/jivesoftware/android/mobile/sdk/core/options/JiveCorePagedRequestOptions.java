package com.jivesoftware.android.mobile.sdk.core.options;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This only has one subclass right now, but will be required in the future by other classes (pulled from jive-ios-sdk):
 * JiveAnnouncementRequestOptions
 * JiveAssociationsRequestOptions
 * JiveCommentsRequestOptions
 * JiveOutcomeRequestOptions
 * JiveStateRequestOptions
 */
public class JiveCorePagedRequestOptions extends JiveCoreCountRequestOptions {
    private int startIndex;

    public void setStartIndex(int startIndex) {
        if (startIndex < 0) {
            throw new IllegalArgumentException("Invalid startIndex: " + startIndex);
        } else {
            this.startIndex = startIndex;
        }
    }

    public int getStartIndex() {
        return startIndex;
    }

    @Override
    public LinkedHashMap<String, List<String>> copyQueryParameters() {
        LinkedHashMap<String, List<String>> queryParameters = super.copyQueryParameters();
        if (startIndex > 0) {
            List<String> list = new ArrayList<String>();
            list.add(Integer.toString(startIndex));
            queryParameters.put("startIndex", list);
        }
        return queryParameters;
    }
}
