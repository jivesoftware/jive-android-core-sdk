package com.jivesoftware.android.mobile.sdk.entity;

import java.util.Map;

/**
 * Created by mark.schisler on 8/19/14.
 */
public class EndpointRequestEntity {
    public RequestMethod method;
    public String endpoint;
    public Map<String, Object> queryParams;
}
