package com.jivesoftware.android.mobile.sdk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@JsonSerialize(include= NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttendanceEntity {

    public Integer response;
    public Boolean canAttend;
    public Boolean capped;
    public Boolean anonymous;
    public Boolean containerAllowsAttendance;
    public Boolean canJoinGroupToContribute;
    public Integer yesStatus;
    public Integer noStatus;
    public Integer maybeStatus;
    public Boolean ended;
    public AttendeesEntity yesAttendees;
    public AttendeesEntity noAttendees;
    public AttendeesEntity maybeAttendees;
    public AttendeesEntity unansweredInvitees;
}
