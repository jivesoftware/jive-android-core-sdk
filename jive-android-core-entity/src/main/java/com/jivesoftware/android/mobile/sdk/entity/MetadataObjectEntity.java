package com.jivesoftware.android.mobile.sdk.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@JsonSerialize(include= NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataObjectEntity {
    public String name;
    public String description;
    public List<MetadataFieldEntity> fields;

    @JsonSerialize(include= NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MetadataFieldEntity {
        public Boolean array;
        public String displayName;
        public Boolean editable;
        public String name;
        public Boolean required;
        public String type;
        public Boolean unpublished;
    }

}
