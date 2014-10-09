package com.jivesoftware.android.mobile.sdk.core.options;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This only has one subclass right now, but will be required in the future by other classes (pulled from jive-ios-sdk):
 * JiveFilterTagsRequestOptions
 */
public class JiveCoreSortedRequestOptions extends JiveCorePagedRequestOptions {
    public static enum Order {
        /**
          * Sort by the date this content object was created, in descending order.
          */
        DATE_CREATED_DESC("dateCreatedDesc"),
        /**
         * Sort by the date this content object was created, in ascending order
         */
        DATE_CREATED_ASC("dateCreatedAsc"),
        /**
         * Sort by the date this content object had the most recent activity, in descending order
         */
        LATEST_ACTIVITY_DESC("latestActivityDesc"),
        /**
         * Sort by the date this content object had the most recent activity, in ascending order
         */
        LATEST_ACTIVITY_ASC("latestActivityAsc"),
        /**
         * Sort by content object subject, in ascending order
         */
        TITLE_ASC("titleAsc"),
        /**
         * Sort by first name in ascending order
         */
        FIRST_NAME_ASC("firstNameAsc"),
        /**
         * Sort by last name in ascending order
         */
        LAST_NAME_ASC("lastNameAsc"),
        /**
         * Sort by joined date in descending order
         */
        DATE_JOINED_DESC("dateJoinedDesc"),
        /**
         * Sort by joined date in ascending order
         */
        DATE_JOINED_ASC("dateJoinedAsc"),
        /**
         * Sort by status level in descending order
         */
        STATUS_LEVEL_DESC("statusLevelDesc"),
        /**
         * Sort by relevance, in descending order.
         */
        RELEVANCE_DESC("relevanceDesc"),
        /**
         * Sort by the date this content object was most recently updated, in ascending order.
         */
        UPDATED_ASC("updatedAsc"),
        /**
         * Sort by the date this content object was most recently updated, in descending order.
         */
        UPDATED_DESC("updatedDesc");


        private final String sortName;

        Order(String sortName) {
            this.sortName = sortName;
        }

        public String getSortName() {
            return sortName;
        }
    }

    private Order order;

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public LinkedHashMap<String, List<String>> copyQueryParameters() {
        LinkedHashMap<String, List<String>> queryParameters = super.copyQueryParameters();
        if (order != null) {
            ArrayList<String> list = new ArrayList<String>();
            list.add(order.sortName);
            queryParameters.put("sort", list);
        }
        return queryParameters;
    }
}
