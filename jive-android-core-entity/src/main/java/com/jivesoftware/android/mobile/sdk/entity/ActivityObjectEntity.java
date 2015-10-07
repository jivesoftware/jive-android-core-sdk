package com.jivesoftware.android.mobile.sdk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreObjectTypeValue;

import java.util.Date;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.*;

@JsonSerialize(include= Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityObjectEntity {
    public String id;
    public String displayName;
    public JiveCoreObjectTypeValue objectType;
    public String summary;
    public Date published;
    public Date updated;
    public String url;
    public String resolved;
    public String answer;
    public Boolean question;
    public MediaLinkEntity image;

    public JiveExtensionEntity jive;
    public ActivityObjectPersonEntity author;

    @JsonSerialize(include= Inclusion.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JiveExtensionEntity {
        public Boolean externalContributor;
    }
}
