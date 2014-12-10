package com.jivesoftware.android.mobile.sdk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FavoriteObjectEntity {

    public String id;
    public String subject;
    public String type;
    public String image;
    public String url;
}
