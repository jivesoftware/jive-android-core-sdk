package com.jivesoftware.android.mobile.sdk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@JsonSerialize(include= NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VersionEntity {
    public String jiveVersion;
    public String instanceURL;
    public List<String> ssoEnabled;
    public List<JiveCoreVersionEntity> jiveCoreVersions;
    public JiveEditionEntity jiveEdition;

    @JsonSerialize(include= NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JiveCoreVersionEntity {
        public Integer version;
        public Integer revision;
        public String uri;
        public String documentation;
    }

    @JsonSerialize(include= NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JiveEditionEntity {
        public String product;

    }
}
