package com.jivesoftware.android.mobile.sdk.entity.value;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Common object type values used to represent content, places, stream entities, and other miscellaneous
 * types.
 */
@ParametersAreNonnullByDefault
public enum JiveCoreObjectType implements JiveCoreObjectTypeValue {

    acclaim,
    announcement,
    app,
    bulkUpload,
    blog,
    comment,
    connectionUpdates,
    discussion,
    dm,
    document,
    event,
    extStreamActivity,
    favorite,
    file,
    group,
    idea,
    latestLikes,
    level,
    market,
    member,
    mention,
    message,
    outcome,
    peopleUpdates,
    person,
    photoAlbum,
    placePages,
    poll,
    post,
    profileUpdates,
    profileImage,
    project,
    share,
    slide,
    socialNews,
    space,
    stage,
    stream,
    streamEntry,
    task,
    update,
    url,
    user,
    video,
    welcome,
    contentRelationship,

    // NOTE: The following are pseudo-types or are types of limited confidence
    QUESTION,
    ANSWERED_QUESTION,
    CAROUSEL,
    CONTENT_VERSION,
    TAG,
    UNKNOWN;

    /*
     * Here we take advantage of the fact that the enum value constructor is invoked prior to the
     * static initializer so that we can use enum value names as part of the value definitions.
     * The only reason why we need this is so that we can refer to item reply types and aliases defined by
     * this same class.
     */
    static {
        acclaim.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(1150305777)
                .setInboxFilterType()
                .build());
        announcement.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(22)
                .setInboxFilterType()
                .setContentType()
                .build());
        app.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .build());
        blog.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(37)
                .setPlaceType()
                .setFollowableType()
                .build());
        bulkUpload.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(77711763)
                .setInboxFilterType()
                .build());
        comment.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(105)
                .setContentType()
                .setInboxFilterType()
                .setSearchableType()
                .setFollowableType()
                .setItemReplyType(comment)
                .build());
        connectionUpdates.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(-1805099612)
                .build());
        discussion.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(1)
                .setContentType()
                .setInboxFilterType()
                .setTrendingFilterType()
                .setSearchableType()
                .setFollowableType()
                .setItemReplyType(message)
                .build());
        dm.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(109016030)
                .setInboxFilterType()
                .setContentType()
                .setItemReplyType(comment)
                .build());
        document.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(102)
                .setContentType()
                .setInboxFilterType()
                .setTrendingFilterType()
                .setSearchableType()
                .setFollowableType()
                .setItemReplyType(comment)
                .build());
        event.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(96891546)
                .setInboxFilterType()
                .setTrendingFilterType()
                .setContentType()
                .setSearchableType()
                .setFollowableType()
                .setItemReplyType(comment)
                .build());
        extStreamActivity.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setInboxFilterType()
                .setTrendingFilterType()
                .setContentType()
                .setFollowableType()
                .setItemReplyType(comment)
                .build());
        favorite.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(800, 801) // 800=internal, 801=external
                .setContentType()
                .setInboxFilterType()
                .setFollowableType()
                .build());
        file.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setContentType()
                .setInboxFilterType()
                .setSearchableType()
                .setFollowableType()
                .setItemReplyType(comment)
                .build());
        group.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(700, 701, 702, 703) // 700=open, 701=members, 702=private, 703=shared
                .setPlaceType()
                .setFollowableType()
                .build());
        idea.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(3227383)
                .setInboxFilterType()
                .setTrendingFilterType()
                .setContentType()
                .setSearchableType()
                .setFollowableType()
                .setItemReplyType(comment)
                .build());
        latestLikes.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(447369365)
                .build());
        level.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .build());
        market.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .build());
        member.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .build());
        mention.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setInboxFilterType()
                .setObjectType(550)
                .build());
        message.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(2)
                .setContentType()
                .setInboxFilterType()
                .setSearchableType()
                .setFollowableType()
                .setItemReplyType(message)
                .build());
        outcome.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(2700)
                .build());
        peopleUpdates.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(61669422)
                .build());
        person.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(3)
                .setFollowableType()
                .build());
        photoAlbum.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(535535779)
                .setContentType()
                .setFollowableType()
                .setItemReplyType(comment)
                .build());
        placePages.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(-2106121635)
                .build());
        poll.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(18)
                .setInboxFilterType()
                .setContentType()
                .setSearchableType()
                .setFollowableType()
                .setItemReplyType(comment)
                .build());
        post.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(38)
                .setContentType()
                .setInboxFilterType()
                .setTrendingFilterType()
                .setSearchableType()
                .setFollowableType()
                .setItemReplyType(comment)
                .build());
        profileUpdates.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(2018307648)
                .build());
        profileImage.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(501)
                .build());
        project.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(600)
                .setPlaceType()
                .setFollowableType()
                .build());
        share.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(109400031)
                .setInboxFilterType()
                .setContentType()
                .setFollowableType()
                .setItemReplyType(comment)
                .build());
        slide.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setContentType()
                .build());
        socialNews.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(-1864883264)
                .build());
        space.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(14)
                .setPlaceType()
                .setFollowableType()
                .build());
        stage.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setContentType()
                .build());
        stream.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setFollowableType()
                .build());
        streamEntry.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setContentType()
                .setObjectType(1501)
                .setFollowableType()
                .setItemReplyType(comment)
                .build());
        task.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(602)
                .setInboxFilterType()
                .setContentType()
                .setFollowableType()
                .build());
        update.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(1464927464)
                .setContentType()
                .setInboxFilterType()
                .setTrendingFilterType()
                .setFollowableType()
                .setItemReplyType(comment)
                .build());
        url.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setAliasOf(favorite)
                .build());
        user.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setFollowableType()
                .build());
        video.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(1100)
                .setInboxFilterType()
                .setTrendingFilterType()
                .setContentType()
                .setSearchableType()
                .setFollowableType()
                .setItemReplyType(comment)
                .build());
        welcome.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(-734134279)
                .setInboxFilterType()
                .build());
        contentRelationship.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setObjectType(2901)
                .build());

        // NOTE: The following are pseudo-types or are types of limited confidence
        QUESTION.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setAliasOf(discussion)
                .build());
        ANSWERED_QUESTION.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setAliasOf(discussion)
                .build());
        CAROUSEL.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setFollowableType()
                .build());
        CONTENT_VERSION.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setFollowableType()
                .build());
        TAG.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .build());
        UNKNOWN.delegateRef.set(JiveCoreObjectTypeImpl.builder()
                .setContentType()
                .setFollowableType()
                .setItemReplyType(comment)
                .build());

        // Sanity check to make sure we got them all:
        for (JiveCoreObjectType value : values()) {
            if (value.delegateRef.get() == null) {
                throw new IllegalStateException("No delegate set for JiveCoreObjectType." + value);
            }
        }
    }

    @Nonnull
    private final AtomicReference<JiveCoreObjectTypeImpl> delegateRef = new AtomicReference<JiveCoreObjectTypeImpl>();

    @Override
    public boolean isAlias() {
        return delegateRef.get().isAlias();
    }

    @Nonnull
    @Override
    public JiveCoreObjectTypeValue getPrimaryType() {
        return delegateRef.get().getPrimaryType();
    }

    @Override
    public boolean isContentType() {
        return delegateRef.get().isContentType();
    }

    @Override
    public boolean isInboxFilterType() {
        return delegateRef.get().isInboxFilterType();
    }

    @Override
    public boolean isTrendingFilterType() {
        return delegateRef.get().isTrendingFilterType();
    }

    @Override
    public boolean isPlaceType() {
        return delegateRef.get().isPlaceType();
    }

    @Override
    public boolean isSearchableType() {
        return delegateRef.get().isSearchableType();
    }

    @Override
    public boolean isFollowable() {
        return delegateRef.get().isFollowable();
    }

    @Nullable
    public Integer getObjectType() {
        return delegateRef.get().getObjectType();
    }

    @Nonnull
    @Override
    public Set<Integer> getObjectTypeAliases() {
        return delegateRef.get().getObjectTypeAliases();
    }

    @Nullable
    public JiveCoreObjectTypeValue getReplyType() {
        return delegateRef.get().getReplyType();
    }

}
