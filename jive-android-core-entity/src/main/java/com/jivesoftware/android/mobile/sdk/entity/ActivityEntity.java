package com.jivesoftware.android.mobile.sdk.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by mark.schisler on 8/14/14.
 */
public class ActivityEntity {
    public ActivityObjectEntity actor;
    public ActivityObjectEntity object;
    public ActivityObjectEntity provider;
    public ActivityObjectEntity target;
    public ActivityObjectEntity generator;
    public String content;
    public String title;
    public String verb;
    public String url;
    public Date published;
    public Date updated;

    @SerializedName("jive")
    public JiveExtensionEntity jiveExtension;

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
        public String outcomeTypeName;
        public Boolean canLike;

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
