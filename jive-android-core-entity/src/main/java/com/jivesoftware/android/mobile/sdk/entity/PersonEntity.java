package com.jivesoftware.android.mobile.sdk.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreObjectTypeValue;

import java.util.List;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@JsonSerialize(include= NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonEntity extends JiveObjectEntity<JiveCoreObjectTypeValue> {
    public String displayName;
    public NameEntity name;
    public String thumbnailUrl;
    public Integer followingCount;
    public Integer followerCount;
    public String status;
    public String location;

    @JsonProperty("jive")
    public JiveExtensionEntity jiveExtension;

    public List<EmailEntity> emails;

    public List<PhoneNumberEntity> phoneNumbers;

    public List<AddressEntity> addresses;

    public List<PhotoEntity> photos;

    @JsonSerialize(include= NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NameEntity {
        public String familyName;
        public String givenName;
        public String formatted;
    }

    @JsonSerialize(include= NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
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

    @JsonSerialize(include= NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JiveLevelEntity {
        public String name;
        public Integer points;
    }
}
