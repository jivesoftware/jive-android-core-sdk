package com.jivesoftware.android.mobile.sdk.core.options;

import com.jivesoftware.android.mobile.sdk.util.DateFormatUtil;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class JiveCoreDateLimitedRequestOptions extends JiveCoreCountRequestOptions {
    private Date date;
    private boolean after;
    private boolean collapse;

    public void setDate(Date date, boolean after) {
        this.date = date;
        this.after = after;
    }

    public void setCollapse(boolean collapse) {
        this.collapse = collapse;
    }

    @Override
    public LinkedHashMap<String, List<String>> copyQueryParameters() {
        LinkedHashMap<String, List<String>> queryParameters = super.copyQueryParameters();

        if (collapse) {
            queryParameters.put("collapse", Collections.singletonList("true"));
        }

        DateFormat gmtIso8601DateFormat = DateFormatUtil.getGmtIso8601DateFormat();
        if (date != null) {
            String formatted = gmtIso8601DateFormat.format(date);
            String type = after ? "after" : "before";
            queryParameters.put(type, Collections.singletonList(formatted));
        }

        return queryParameters;
    }
}
