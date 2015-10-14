package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.ContentEntity;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class JiveCoreURIFactory {

    private static final String OAUTH2_TOKEN_REQUEST_URL = "/oauth2/token";
    private static final String OAUTH2_TOKEN_REFRESH_URL = "/oauth2/token";
    private static final String OAUTH2_TOKEN_REVOKE_URL = "/oauth2/revoke";
    private static final String INBOX = JiveCoreConstants.CORE_API_V3_PREFIX + "/inbox";
    private static final String ALL_ACTIVITY =  JiveCoreConstants.CORE_API_V3_PREFIX + "/activities";
    private static final String CONTENT_ROOT =  JiveCoreConstants.CORE_API_V3_PREFIX + "/contents";
    private static final String PEOPLE_ROOT =  JiveCoreConstants.CORE_API_V3_PREFIX + "/people";
    private static final String RECENT_PEOPLE_ROOT =  ALL_ACTIVITY + "/recent/people";
    private static final String RECENT_PLACES_ROOT =  ALL_ACTIVITY + "/recent/places";
    private static final String RECENT_CONTENT_ROOT =  ALL_ACTIVITY + "/recent/content";
    private static final String TRENDING = ALL_ACTIVITY + "/discovery";
    private static final String PLACES_ROOT = JiveCoreConstants.CORE_API_V3_PREFIX + "/places";
    private static final String SEARCH_ROOT =  JiveCoreConstants.CORE_API_V3_PREFIX + "/search";
    private static final String SEARCH_PEOPLE = SEARCH_ROOT + "/people";
    private static final String SEARCH_PLACES = SEARCH_ROOT + "/places";
    private static final String SEARCH_CONTENT = SEARCH_ROOT + "/contents";
    private static final String METADATA_OBJECT_ROOT =  JiveCoreConstants.CORE_API_V3_PREFIX + "/metadata/objects";
    private static final String METADATA_PROPERTIES =  JiveCoreConstants.CORE_API_V3_PREFIX + "/metadata/properties";
    private static final String MEMBERSHIPS_ROOT = JiveCoreConstants.CORE_API_V3_PREFIX + "/members";
    private static final String MODERATION_ROOT = JiveCoreConstants.CORE_API_V3_PREFIX + "/moderation";
    private static final String PERSON_MEMBERSHIPS_ROOT = MEMBERSHIPS_ROOT + "/people";
    private static final String PLACE_MEMBERSHIPS_ROOT = MEMBERSHIPS_ROOT + "/places";

    private final URL baseURL;

    public JiveCoreURIFactory(@Nonnull URL baseURL) {
        //noinspection ConstantConditions
        if (baseURL == null) {
            throw(new IllegalStateException("baseURL may not be null"));
        }
        URL normalized = baseURL;
        if (!baseURL.getPath().isEmpty() && !baseURL.getPath().endsWith("/")) {
            try {
                normalized = new URL(baseURL.toExternalForm() + "/");
            } catch (MalformedURLException e) {
                throw new IllegalStateException("invalid baseURL: " + baseURL);
            }
        } else {
            normalized = baseURL;
        }
        this.baseURL = normalized;
    }

    @Nonnull
    public URI createURI(@Nonnull String pathAndQuery) {
        try {
            if (pathAndQuery.length() > 1 && pathAndQuery.startsWith("/")) {
                pathAndQuery = pathAndQuery.substring(1);
            }
            URL url = new URL(baseURL, pathAndQuery);
            try {
                URI uri = url.toURI();
                return uri;
            } catch (URISyntaxException e) {
                throw new IllegalStateException("invalid uri: " + url.toExternalForm(), e);
            }
        } catch (MalformedURLException e) {
            throw new IllegalStateException("invalid pathAndQuery \"" + pathAndQuery + "\" for baseURL: " + baseURL);
        }
    }

    @Nonnull
    public URI createURI(@Nonnull String pathAndQuery, @Nonnull JiveCoreQueryParameterProvider options) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, List<String>> entry : options.provideQueryParameters().entrySet()) {
            for ( String value : entry.getValue() ) {
                pairs.add(new BasicNameValuePair(entry.getKey(), value));
            }
        }

        String additionalQueryParams = URLEncodedUtils.format(pairs, "UTF-8");
        String additionalQuery;
        //noinspection ConstantConditions
        if ((additionalQueryParams == null) || (additionalQueryParams.length() == 0)) {
            additionalQuery = "";
        } else if (pathAndQuery.contains("?")) {
            additionalQuery = "&" + additionalQueryParams;
        } else {
            additionalQuery = "?" + additionalQueryParams;
        }
        URI uri = createURI(pathAndQuery + additionalQuery);
        return uri;
    }

    @Nonnull
    public URI apiVersionUri() {
        return createURI("api/version");
    }

    @Nonnull
    public URI metadataPropertiesUri() {
        return createURI(METADATA_PROPERTIES);
    }

    @Nonnull
    public URI metadataPublicPropertiesUri() {
        return createURI(METADATA_PROPERTIES + "/public");
    }

    @Nonnull
    public URI sessionGrantUri(String addonUUID) {
        return createURI("/api/addons/" + addonUUID + "/session-grant-allowed");
    }

    @Nonnull
    public URI metadataObjectUri(String metadataObjectName) {
        return createURI(METADATA_OBJECT_ROOT + "/" + metadataObjectName);
    }

    @Nonnull
    public URI personSelfUri() {
        return personUri("@me");
    }

    @Nonnull
    public URI personUri(String personId) {
        return createURI(PEOPLE_ROOT + "/" + personId);
    }

    @Nonnull
    public URI createNewsSelfUri() {
        return personUri("@me/@news");
    }

    @Nonnull
    public URI createStreamsSelfUri() {
        return personUri("@me/streams");
    }

    @Nonnull
    public URI trendingUri(JiveCoreQueryParameterProvider options) {
        return createURI(TRENDING, options);
    }

    @Nonnull
    public URI activityUri() {
        return createURI(ALL_ACTIVITY);
    }

    @Nonnull
    public URI createContentUri() {
        return createURI(CONTENT_ROOT);
    }

    @Nonnull
    public URI updateContentUri(ContentEntity contentEntity) {
        return createURI(contentEntity.resources.get("self").ref);
    }

    @Nonnull
    public URI fetchInboxUri(JiveCoreQueryParameterProvider options) {
        return createURI(INBOX, options);
    }

    @Nonnull
    public URI batchOperationUri() {
        return createURI(JiveCoreConstants.CORE_API_V3_PREFIX + "/executeBatch");
    }

    @Nonnull
    public URI fetchActivities(String requestPathAndQuery, JiveCoreQueryParameterProvider options) {
        return createURI(requestPathAndQuery, options);
    }

    @Nonnull
    public URI fetchActivitiesByPersonUri(String personUrl, JiveCoreQueryParameterProvider options) {
        return createURI(personUrl + "/activities", options);
    }

    @Nonnull
    public URI fetchContentsUri(JiveCoreQueryParameterProvider options) {
        return createURI(CONTENT_ROOT, options);
    }

    @Nonnull
    public URI fetchPlacesUri(JiveCoreQueryParameterProvider options) {
        return createURI(PLACES_ROOT, options);
    }

    @Nonnull
    public URI fetchPeople(String peopleUrl, JiveCoreQueryParameterProvider options) {
        return createURI(peopleUrl, options);
    }

    @Nonnull
    public URI fetchRecentPeople(JiveCoreQueryParameterProvider options) {
        return createURI(RECENT_PEOPLE_ROOT, options);
    }

    @Nonnull
    public URI fetchRecentPlaces(JiveCoreQueryParameterProvider options) {
        return createURI(RECENT_PLACES_ROOT, options);
    }

    @Nonnull
    public URI fetchRecentContent(JiveCoreQueryParameterProvider options) {
        return createURI(RECENT_CONTENT_ROOT, options);
    }

    @Nonnull
    public URI fetchPeopleUri(JiveCoreQueryParameterProvider options) {
        return createURI(PEOPLE_ROOT, options);
    }

    @Nonnull
    public URI searchContentsUri(JiveCoreQueryParameterProvider options) {
        return createURI(SEARCH_CONTENT, options);
    }

    @Nonnull
    public URI searchPeopleUri(JiveCoreQueryParameterProvider options) {
        return createURI(SEARCH_PEOPLE, options);
    }

    @Nonnull
    public URI searchPlacesUri(JiveCoreQueryParameterProvider options) {
        return createURI(SEARCH_PLACES, options);
    }

    @Nonnull
    public URI fetchMembersByPersonUri(String personID, JiveCoreQueryParameterProvider options) {
        return createURI(PERSON_MEMBERSHIPS_ROOT + "/" + personID, options);
    }

    @Nonnull
    public URI createMembershipUri(String placeID) {
        return createURI(PLACE_MEMBERSHIPS_ROOT + "/" + placeID);
    }

    @Nonnull
    public URI fetchMembersByPlaceUri(String placeID, JiveCoreQueryParameterProvider options) {
        return createURI(PLACE_MEMBERSHIPS_ROOT + "/" + placeID, options);
    }

    @Nonnull
    public URI uploadImageUri() {
        return createURI(JiveCoreConstants.CORE_API_V3_PREFIX + "/images");
    }

    @Nonnull
    public URI placesUri() {
        return createURI(JiveCoreConstants.CORE_API_V3_PREFIX + "/places");
    }

    @Nonnull
    public URI registerForPushUri() {
        return createURI("/api/core/mobile/v1/pushNotification/register");
    }

    @Nonnull
    public URI unregisterForPushUri() {
        return createURI("/api/core/mobile/v1/pushNotification/unregister");
    }

    @Nonnull
    public URI completedMissionUri(String mission) {
        return createURI("/api/core/mobile/v1/quest/" + mission);
    }

    @Nonnull
    public URI oAuth2RequestUri() {
        return createURI(OAUTH2_TOKEN_REQUEST_URL);
    }

    @Nonnull
    public URI oAuth2RefreshUri() {
        return createURI(OAUTH2_TOKEN_REFRESH_URL);
    }

    @Nonnull
    public URI oAuth2RevokeUri() {
        return createURI(OAUTH2_TOKEN_REVOKE_URL);
    }

    @Nonnull
    public URI fetchModerationPending(JiveCoreQueryParameterProvider options) {
        return createURI(MODERATION_ROOT + "/pending", options);
    }

    @Nonnull
    public URI updateModeration(int moderationId) {
        return createURI(MODERATION_ROOT + "/" + moderationId);
    }

}
