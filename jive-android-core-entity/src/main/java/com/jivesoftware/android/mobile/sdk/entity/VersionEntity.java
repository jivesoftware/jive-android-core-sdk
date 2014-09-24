package com.jivesoftware.android.mobile.sdk.entity;

import java.util.List;

public class VersionEntity {
    public String jiveVersion;
    public String instanceURL;
    public List<String> ssoEnabled;
    public List<JiveCoreVersionEntity> jiveCoreVersions;
    public JiveEditionEntity jiveEdition;

    public static class JiveCoreVersionEntity {
        public Integer version;
        public Integer revision;
        public String uri;
        public String documentation;
    }

    public static class JiveEditionEntity {
        public String product;

    }
}
