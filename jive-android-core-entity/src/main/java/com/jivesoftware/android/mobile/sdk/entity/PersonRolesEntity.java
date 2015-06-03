package com.jivesoftware.android.mobile.sdk.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.annotation.Nullable;
import java.util.Set;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@JsonSerialize(include = NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonRolesEntity {
    @Nullable
    public Boolean fullAccess;

    @Nullable
    public Boolean manageCommunity;

    @Nullable
    public Boolean manageSystem;

    @Nullable
    public Boolean manageUsers;

    @Nullable
    public Boolean manageGroups;

    @Nullable
    public Boolean manageNewsStreams;

    @Nullable
    public Boolean moderate;

    @Override
    @JsonIgnore
    public String toString() {
        return "PersonRolesEntity{" +
                "fullAccess=" + fullAccess +
                ", manageCommunity=" + manageCommunity +
                ", manageSystem=" + manageSystem +
                ", manageUsers=" + manageUsers +
                ", manageGroups=" + manageGroups +
                ", manageNewsStreams=" + manageNewsStreams +
                ", moderate=" + moderate +
                '}';
    }

}
