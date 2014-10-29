package com.jivesoftware.android.mobile.sdk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@JsonSerialize(include= NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceEntity extends JiveObjectEntity {
    public String description;
    public String displayName;
    public String name;
    public Date published;
    public Date updated;
    public int memberCount;
    public String placeID;
    public String groupType;
    public boolean visibleToExternalContributors;
    public List<String> contentTypes;
    public int followerCount;
}
