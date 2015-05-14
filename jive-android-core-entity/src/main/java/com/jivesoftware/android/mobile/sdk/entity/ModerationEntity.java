package com.jivesoftware.android.mobile.sdk.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

@JsonSerialize(include= Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModerationEntity {

    // TODO see JiveCoreStatus to implement with factory etc
    public enum ModerationState {
        approved,
        rejected
    }

    public int id;
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
