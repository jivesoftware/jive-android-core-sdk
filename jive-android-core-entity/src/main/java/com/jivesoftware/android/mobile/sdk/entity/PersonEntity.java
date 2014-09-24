package com.jivesoftware.android.mobile.sdk.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PersonEntity extends JiveObjectEntity {
    public String displayName;
    public NameEntity name;
    public String thumbnailUrl;
    public Integer followingCount;
    public Integer followerCount;
    public String status;
    public String location;

    @SerializedName("jive")
    public JiveExtensionEntity jiveExtension;

    public List<EmailEntity> emails;

    public List<PhoneNumberEntity> phoneNumbers;

    public List<AddressEntity> addresses;

    public List<PhotoEntity> photos;

    public static class NameEntity {
        public String familyName;
        public String givenName;
        public String formatted;
    }

    public static class JiveExtensionEntity {
        public String userName;
        public Boolean external;
        public Boolean externalContributor;
        public Boolean enabled;
        public Boolean visible;
        public Boolean federated;
        public Boolean termsAndConditionsRequired;
        public String timeZone;
        public String locale;
        public JiveProfileEntryEntity[] profile;

        public JiveLevelEntity level;

        public static class JiveProfileEntryEntity extends GenericEntity<String> { }
    }

    public static class JiveLevelEntity {
        public String name;
        public Integer points;
    }
}
