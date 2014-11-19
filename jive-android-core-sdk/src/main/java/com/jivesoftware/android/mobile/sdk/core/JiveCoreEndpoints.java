package com.jivesoftware.android.mobile.sdk.core;

/**
 * Created by mark.schisler on 8/14/14.
 */
public class JiveCoreEndpoints {
    public static final String OAUTH2_TOKEN_REQUEST_URL = "/oauth2/token";
    public static final String OAUTH2_TOKEN_REFRESH_URL = "/oauth2/token";
    public static final String OAUTH2_TOKEN_REVOKE_URL = "/oauth2/revoke";

    public static final String INBOX = JiveCoreConstants.CORE_API_V3_PREFIX + "/inbox";

    public static final String ALL_ACTIVITY =  JiveCoreConstants.CORE_API_V3_PREFIX + "/activities";
    public static final String CONTENT_ROOT =  JiveCoreConstants.CORE_API_V3_PREFIX + "/contents";
    public static final String PEOPLE_ROOT =  JiveCoreConstants.CORE_API_V3_PREFIX + "/people";
    public static final String PLACES_ROOT = JiveCoreConstants.CORE_API_V3_PREFIX + "/places";

    public static final String DIRECT_MESSAGE = JiveCoreConstants.CORE_API_V3_PREFIX + "/dms";
    public static final String SEARCH_ROOT =  JiveCoreConstants.CORE_API_V3_PREFIX + "/search";
    public static final String SEARCH_PEOPLE = SEARCH_ROOT + "/people";
    public static final String SEARCH_PLACES = SEARCH_ROOT + "/places";
    public static final String SEARCH_CONTENT = SEARCH_ROOT + "/contents";

    public static final String METADATA_OBJECT_ROOT =  JiveCoreConstants.CORE_API_V3_PREFIX + "/metadata/objects";
    public static final String METADATA_PROPERTIES =  JiveCoreConstants.CORE_API_V3_PREFIX + "/metadata/properties";

    public static final String ME_URL = PEOPLE_ROOT + "/@me";
    public static final String ME_NEWS_URL = ME_URL + "/@news";

    public static final String MEMBERSHIPS_ROOT = JiveCoreConstants.CORE_API_V3_PREFIX + "/members";
    public static final String PERSON_MEMBERSHIPS_ROOT = MEMBERSHIPS_ROOT + "/people";
    public static final String PLACE_MEMBERSHIPS_ROOT = MEMBERSHIPS_ROOT + "/places";

    public static final String IMAGES_ROOT =  JiveCoreConstants.CORE_API_V3_PREFIX + "/images";
    public static final String VERSION = "/api/version";
}
