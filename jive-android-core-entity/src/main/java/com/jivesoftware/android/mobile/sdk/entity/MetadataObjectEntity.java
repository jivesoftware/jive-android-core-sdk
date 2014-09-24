package com.jivesoftware.android.mobile.sdk.entity;


import java.util.List;

public class MetadataObjectEntity {
    public String name;
    public String description;
    public List<MetadataFieldEntity> fields;

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
