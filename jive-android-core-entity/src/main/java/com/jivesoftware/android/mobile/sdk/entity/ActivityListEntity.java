package com.jivesoftware.android.mobile.sdk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.*;

@JsonSerialize(include= Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityListEntity extends ListEntity<ActivityEntity> {
    public Integer unread;
}
