package com.jivesoftware.android.mobile.sdk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreObjectTypeValue;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@JsonSerialize(include= NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentEntity extends JiveObjectEntity<JiveCoreObjectTypeValue> {
    // keep this alphabetized
    /** For discussion */
    public String answer;
    public List<AttachmentEntity> attachments;
    public PersonEntity author;
    public String authorship;
    /** For file */
    public String binaryURL;
    public ContentBodyEntity content;
    public String contentID;
    public Integer version;
    /** For file */
    public String contentType;
    public List<ImageEntity> contentImages;
    public String discussion;
    public Integer followerCount;
    public String highlightBody;
    public String highlightSubject;
    public String highlightTags;
    /** For update */
    public Double latitude;
    public Integer likeCount;
    /** For update */
    public Double longitude;
    /** For file */
    public String name;
    public String parent;
    public SummaryEntity parentPlace;
    public SummaryEntity parentContent;
    /** For ideas */
    public Integer score;
    public String stage;
    public Integer voteCount; // also used by polls
    public Boolean voted;
    public Boolean promote;
    /** For share, message.
     *  Can be a PersonEntity[] or a String[] of PersonEntity self urls
     * */
    public Object[] participants;
    public Date published;
    /** For discussion */
    public Boolean question;
    public Integer replyCount;
    /** For update */
    public ContentEntity repost;
    /** For discussion */
    public String resolved;
    public String rootType;
    public String rootURI;
    /** For share */
    public String shared;
    /** For share */
    public ContentEntity sharedContent;
    /** For share */
    public PlaceEntity sharedPlace;
    /** For file */
    public Long size;
    public String status;
    public String subject;
    public List<String> tags;
    /** Can be a PersonEntity[] or a String[] of PersonEntity self urls */
    public Object[] users;
    public Date updated;
    public Integer viewCount;
    public Boolean visibleToExternalContributors;
    public String visibility;
    public ActorEntity actor;
    public String productName;
    public ObjectEntity object;
    public FavoriteObjectEntity favoriteObject;
    public Integer favoriteCount;
    public String url;

    /** For event */
    public String location;
    public Date startDate; // also for polls
    public Date endDate; // also for polls
    public AttendanceEntity attendance;
    public String eventType;
    public String eventAccess;

    /** For polls */
    public List<String> options;
    public List<PollOptionImageEntity> optionsImages;
    public List<Date> voteDates;
    public List<String> votes;
    // voteCount is also for ideas
    // startDate
    // endDate

    /* Helpful/Unhelpful */
    public Integer helpfulCount;
    public Integer unhelpfulCount;
    public Boolean unHelpfulEnabled;
    public Boolean canMarkHelpful;
    public Boolean canMarkUnHelpful;
    public Boolean hasVotedHelpful;
    public Boolean hasVotedUnHelpful;

    /* For video */
    public String stillImageURL;
    public String playerBaseURL;
    public String externalID; // also for external activity
    public String authtoken;
    public Integer width;
    public Integer height;

    /* Announcements */
    public String subjectURI;

    /* External activity */
    public Integer externalStreamID;
    public Map<String, String> properties;
    public Map<String, String> productIcons;
    public String productIcon;
    public ActorEntity onBehalfOf;

}
