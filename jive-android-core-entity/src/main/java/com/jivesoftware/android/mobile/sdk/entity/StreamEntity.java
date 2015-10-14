package com.jivesoftware.android.mobile.sdk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreObjectTypeValue;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreStreamSourceValue;
import java.util.Date;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@JsonSerialize(include= NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamEntity extends JiveObjectEntity<JiveCoreObjectTypeValue> {
    public String name;
    public JiveCoreStreamSourceValue source;
    public Date newUpdates;
    public Date previousUpdates;
    public Date published;
    public Date updated;
    public Boolean receiveEmails;
}
