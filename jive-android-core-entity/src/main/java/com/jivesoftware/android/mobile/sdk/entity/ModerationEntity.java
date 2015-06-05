package com.jivesoftware.android.mobile.sdk.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreModerationStateValue;

import java.awt.*;
import java.util.Date;
import java.util.Map;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

@JsonSerialize(include= Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModerationEntity {

    public int id;
    public JiveCoreModerationStateValue resolution;
    public Integer abuseCount;
    public ModerationObject object;
    public ImageEntity image;
    public Map<String, ResourceEntity> resources;

    @JsonSerialize(include= Inclusion.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ModerationObject {
        public String displayName;
        public String summary;
        public String objectType;
        public String html;
        public String url;
        public ImageEntity image;
        public PersonEntity actor;
        public Date updated;
    }
}
