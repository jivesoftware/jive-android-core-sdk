package com.jivesoftware.android.mobile.sdk.entity;

import java.util.Date;

/**
 * Created by mark.schisler on 8/18/14.
 */
public class MemberEntity extends JiveObjectEntity {
    public PlaceEntity group;
    public PersonEntity person;
    public String state;
    public Date published;
    public Date updated;
}
