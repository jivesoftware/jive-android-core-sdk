package com.jivesoftware.android.mobile.sdk.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreOutcomeTypeValue;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreVerbValue;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

@JsonSerialize(include= Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModerationEntity {
    public String id;
    public String resolution;
    public Integer abuseCount;
    public ModerationObject object;

    // TODO smclaughry Finish this when API stabilizes

    public Map<String, ResourceEntity> resources;

    @JsonSerialize(include= Inclusion.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ModerationObject {
        public String displayName;
        public String summary;
        public String objectType;
        public String html;
        public String url;
        public String image;
    }
}
