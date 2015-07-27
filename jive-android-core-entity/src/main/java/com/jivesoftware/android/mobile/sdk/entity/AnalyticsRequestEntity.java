package com.jivesoftware.android.mobile.sdk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalyticsRequestEntity {
    public String after;
    public String before;

    public List<String> filters = new ArrayList<String>();

    public List<String> actions = new ArrayList<String>();

    public String uniqueField;

    public String name;

    public List<String> periods = new ArrayList<String>();

    public String toString() {
        return "AnalyticsRequestEntity{" +
                "after=" + after +
                ", before=" + before +
                ", filters=" + filters.toString() +
                ", actions=" + actions.toString() +
                ", uniqueField=" + uniqueField +
                ", name=" + name +
                ", periods=" + periods.toString() +
                '}';
    }
}