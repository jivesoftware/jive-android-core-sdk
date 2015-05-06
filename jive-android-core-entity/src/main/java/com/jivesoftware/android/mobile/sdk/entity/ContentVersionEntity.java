package com.jivesoftware.android.mobile.sdk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreObjectTypeValue;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreStatusValue;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@JsonSerialize(include= NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentVersionEntity extends JiveObjectEntity<JiveCoreObjectTypeValue> {
    public JiveCoreStatusValue status;
    public Integer versionNumber;
}
