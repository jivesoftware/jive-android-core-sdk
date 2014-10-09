package com.jivesoftware.android.mobile.sdk.core.options;

/**
 * Common sort types.
 */
public enum JiveCoreSort implements JiveCoreSortValue {

    /**
     * Sort by the date this content object was created, in descending order.
     */
    dateCreatedDesc,

    /**
     * Sort by the date this content object was created, in ascending order
     */
    dateCreatedAsc,

    /**
     * Sort by the date this content object had the most recent activity, in descending order
     */
    latestActivityDesc,

    /**
     * Sort by the date this content object had the most recent activity, in ascending order
     */
    latestActivityAsc,

    /**
     * Sort by content object subject, in ascending order
     */
    titleAsc,

    /**
     * Sort by first name in ascending order
     */
    firstNameAsc,

    /**
     * Sort by last name in ascending order
     */
    lastNameAsc,

    /**
     * Sort by joined date in descending order
     */
    dateJoinedDesc,

    /**
     * Sort by joined date in ascending order
     */
    dateJoinedAsc,

    /**
     * Sort by status level in descending order
     */
    statusLevelDesc,

    /**
     * Sort by relevance, in descending order.
     */
    relevanceDesc,

    /**
     * Sort by the date this content object was most recently updated, in ascending order.
     */
    updatedAsc,

    /**
     * Sort by the date this content object was most recently updated, in descending order.
     */
    updatedDesc

}

