package com.jivesoftware.android.mobile.sdk.core.options;

import com.jivesoftware.android.mobile.sdk.util.Joiner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class JiveCoreReturnFieldsRequestOptions implements JiveCoreRequestOptions {
    private List<String> fields;

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getFields() {
        return fields;
    }

    public LinkedHashMap<String, List<String>> copyQueryParameters() {
        LinkedHashMap<String, List<String>> queryParameters = new LinkedHashMap<String, List<String>>();
        if ((fields != null) && !fields.isEmpty()) {
            String joinedFields = Joiner.on(",").join(fields);
            ArrayList<String> list = new ArrayList<String>();
            list.add(joinedFields);
            queryParameters.put("fields", list);
        }
        return queryParameters;
    }
}
