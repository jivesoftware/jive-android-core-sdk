package com.jivesoftware.android.mobile.sdk.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by mark.schisler on 8/14/14.
 */
public class ContentEntity extends JiveObjectEntity {
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
}