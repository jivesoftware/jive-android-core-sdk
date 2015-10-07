package com.jivesoftware.android.mobile.sdk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

@JsonSerialize(include= Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityObjectPersonEntity {
    public String id;
    public String displayName;
    public MediaLinkEntity image;

}
