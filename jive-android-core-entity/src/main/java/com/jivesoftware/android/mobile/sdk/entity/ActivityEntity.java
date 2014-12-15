package com.jivesoftware.android.mobile.sdk.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreOutcomeTypeValue;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreVerbValue;

import java.util.Date;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

@JsonSerialize(include= Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityEntity {
    public ActivityObjectEntity actor;
    public ActivityObjectEntity object;
    public ActivityObjectEntity provider;
    public ActivityObjectEntity target;
    public ActivityObjectEntity generator;
    public String content;
    public String title;
    public JiveCoreVerbValue verb;
    public String url;
    public MediaLinkEntity previewImage;
    public Date published;
    public Date updated;

    @JsonProperty("jive")
    public JiveExtensionEntity jiveExtension;

    @JsonSerialize(include= Inclusion.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JiveExtensionEntity {
        public String collection;
        public Date collectionUpdated;
        public Boolean followingInStream;
        public ActivityObjectEntity parent;
        public ActivityObjectEntity parentActor;
        public Integer parentReplyCount;
        public Integer parentLikeCount;
        public Boolean canReply;
        public Boolean canComment;
        public Integer replyCount;
        public Integer likeCount;
        public Integer imagesCount;
        public Integer objectID;
        public Integer objectType;
        public String resolved;
        public String answer;
        public Boolean question;
        public Boolean liked;
        public Boolean parentLiked;
        public JiveCoreOutcomeTypeValue outcomeTypeName;
        public Boolean canLike;
        public Boolean objectViewed;

        public ActivityObjectEntity mentioned;
        public Boolean read;
        public String update;
        public String updateCollection;

        public String productName;
        public String productIcon;

        public OnBehalfOfEntity onBehalfOf;
        public OnBehalfOfEntity parentOnBehalfOf;
    };
}
