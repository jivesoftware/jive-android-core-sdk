package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.httpclient.util.JiveEntityUtil;
import com.jivesoftware.android.mobile.sdk.entity.BatchRequestEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentEntity;
import com.jivesoftware.android.mobile.sdk.entity.JiveObjectEntity;
import com.jivesoftware.android.mobile.sdk.entity.ModerationEntity;
import com.jivesoftware.android.mobile.sdk.entity.NewMemberEntity;
import com.jivesoftware.android.mobile.sdk.entity.PlaceEntity;
import com.jivesoftware.android.mobile.sdk.entity.StreamEntity;
import com.jivesoftware.android.mobile.sdk.entity.VoteEntity;
import com.jivesoftware.android.mobile.sdk.http.JsonBody;
import com.jivesoftware.android.mobile.sdk.http.JsonEntity;
import com.jivesoftware.android.mobile.sdk.json.JiveJson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.message.BasicNameValuePair;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
public class JiveCoreRequestFactory {
    @Nonnull
    private final String oauthCredentials;
    @Nonnull
    private final JiveCoreURIFactory uriFactory;
    @Nonnull
    private final JiveJson jiveJson;

    public JiveCoreRequestFactory(String oauthCredentials, URL baseURL, JiveJson jiveJson) {
        this.oauthCredentials = oauthCredentials;
        this.uriFactory = new JiveCoreURIFactory(baseURL);
        this.jiveJson = jiveJson;
    }

    @Nonnull
    public JiveCoreURIFactory getURIFactory() {
        return uriFactory;
    }

    @Nonnull
    public HttpGet fetchMetadataProperties() {
        URI uri = uriFactory.metadataPropertiesUri();
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchMePerson() {
        URI uri = uriFactory.personSelfUri();
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchMeNews() {
        URI uri = uriFactory.createNewsSelfUri();
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchInbox(JiveCoreQueryParameterProvider options) {
        URI uri = uriFactory.fetchInboxUri(options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpPost executeBatchOperation(BatchRequestEntity[] requestEntities) {
        URI uri = uriFactory.batchOperationUri();
        HttpPost post = new HttpPost(uri);
        post.setEntity(JsonEntity.from(jiveJson, requestEntities));
        return post;
    }

    @Nonnull
    public HttpGet fetchImage(String pathAndQuery, JiveCoreQueryParameterProvider options) {
        URI uri = uriFactory.createURI(pathAndQuery, options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet createHttpGet(String requestPathAndQuery) {
        URI uri = uriFactory.createURI(requestPathAndQuery);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpPost createHttpPost(String requestPathAndQuery) {
        URI uri = uriFactory.createURI(requestPathAndQuery);
        return new HttpPost(uri);
    }

    @Nonnull
    public HttpDelete createHttpDelete(String requestPathAndQuery) {
        URI uri = uriFactory.createURI(requestPathAndQuery);
        return new HttpDelete(uri);
    }

    @Nonnull
    public HttpGet searchContents(JiveCoreQueryParameterProvider options) {
        URI uri = uriFactory.searchContentsUri(options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchActivity() {
        URI uri = uriFactory.activityUri();
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchActivityByPerson(String personUrl, JiveCoreQueryParameterProvider options) {
        URI uri = uriFactory.fetchActivitiesByPersonUri(personUrl, options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchContents(JiveCoreQueryParameterProvider options) {
        URI uri = uriFactory.fetchContentsUri(options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchPlaces(JiveCoreQueryParameterProvider options) {
        URI uri = uriFactory.fetchPlacesUri(options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchPeople(@Nonnull JiveCoreQueryParameterProvider options) {
        URI uri = uriFactory.fetchPeopleUri(options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet searchPeople(JiveCoreQueryParameterProvider options) {
        URI uri = uriFactory.searchPeopleUri(options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet searchPlaces(JiveCoreQueryParameterProvider options) {
        URI uri = uriFactory.searchPlacesUri(options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchMembersByPerson(String personID, JiveCoreQueryParameterProvider options) {
        URI uri = uriFactory.fetchMembersByPersonUri(personID, options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpPost createMembership(String placeID, NewMemberEntity newMemberEntity) {
        URI uri = uriFactory.createMembershipUri(placeID);
        HttpPost joinPlacePost = new HttpPost(uri);
        joinPlacePost.setEntity(JsonEntity.from(jiveJson, newMemberEntity));
        return joinPlacePost;
    }

    @Nonnull
    public HttpPost createPromoteContent(String promoteRequestUrl, VoteEntity voteEntity) {
        URI uri = uriFactory.createURI(promoteRequestUrl);
        HttpPost post = new HttpPost(uri);
        post.setEntity(JsonEntity.from(jiveJson, voteEntity));
        return post;
    }

    @Nonnull
    public HttpGet fetchMembersByPlace(String placeID, JiveCoreQueryParameterProvider options) {
        URI uri = uriFactory.fetchMembersByPlaceUri(placeID, options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchMetadataObject(String metadataObjectName, String locale) {
        URI uri = uriFactory.metadataObjectUri(metadataObjectName);
        HttpGet get = new HttpGet(uri);
        get.setHeader(JiveCoreHeaders.ACCEPT_LANGUAGE, locale);
        return get;
    }

    @Nonnull
    public HttpPost createContent(ContentEntity contentEntity, List<? extends AbstractContentBody> fileBodies) {
        URI uri = uriFactory.createContentUri();
        HttpPost post = new HttpPost(uri);
        HttpEntity entity = createHttpEntity(contentEntity, fileBodies);
        post.setEntity(entity);
        return post;
    }

    @Nonnull
    public HttpPost createContent(String pathAndQuery, ContentEntity contentEntity, List<? extends AbstractContentBody> fileBodies) {
        URI uri = uriFactory.createURI(pathAndQuery);
        HttpPost post = new HttpPost(uri);
        HttpEntity entity = createHttpEntity(contentEntity, fileBodies);
        post.setEntity(entity);
        return post;
    }

    @Nonnull
    public HttpPut updateContent(ContentEntity contentEntity, List<? extends AbstractContentBody> fileBodies) {
        URI uri = uriFactory.updateContentUri(contentEntity);
        HttpPut put = new HttpPut(uri);
        HttpEntity entity = createHttpEntity(contentEntity, fileBodies);
        put.setEntity(entity);
        return put;
    }

    @Nonnull
    private HttpEntity createHttpEntity(Object entity, List<? extends AbstractContentBody> fileBodies) {
        HttpEntity httpEntity;
        if (fileBodies.isEmpty()) {
            httpEntity = JsonEntity.from(jiveJson, entity);
        } else {
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            String json = jiveJson.toJson(entity);
            multipartEntity.addPart("json", JsonBody.create(json));

            int fileNumber = 1;
            for (AbstractContentBody fileBody : fileBodies) {
                multipartEntity.addPart("file" + fileNumber, fileBody);
                fileNumber++;
            }
            httpEntity = multipartEntity;
        }
        return httpEntity;
    }

    @Nonnull
    public HttpGet fetchContent(String pathAndQuery, JiveCoreQueryParameterProvider options) {
        URI uri = uriFactory.createURI(pathAndQuery, options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchReplies(String pathAndQuery, JiveCoreQueryParameterProvider options) {
        URI uri = uriFactory.createURI(pathAndQuery, options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpPost uploadImage(AbstractContentBody imageFileBody) {
        URI uri = uriFactory.uploadImageUri();
        HttpPost post = new HttpPost(uri);

        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntity.addPart("image", imageFileBody);
        post.setEntity(multipartEntity);

        return post;
    }

    @Nonnull
    public HttpPost registerForPush(String gcmId, String deviceId) {
        URI uri = uriFactory.registerForPushUri();
        HttpPost post = new HttpPost(uri);

        ArrayList<BasicNameValuePair> bodyNameValuePairs = new ArrayList<BasicNameValuePair>();
        bodyNameValuePairs.add(new BasicNameValuePair("deviceID", deviceId));
        bodyNameValuePairs.add(new BasicNameValuePair("deviceToken", gcmId));
        bodyNameValuePairs.add(new BasicNameValuePair("deviceType", JiveCoreConstants.ANDROID_DEVICE_TYPE.toString()));
        bodyNameValuePairs.add(new BasicNameValuePair("activated", Boolean.TRUE.toString()));

        post.setEntity(JiveEntityUtil.createForm(bodyNameValuePairs));

        return post;
    }

    @Nonnull
    public HttpPost unregisterFromPush(String gcmId, String deviceId) {
        URI uri = uriFactory.unregisterForPushUri();
        HttpPost post = new HttpPost(uri);

        ArrayList<BasicNameValuePair> bodyNameValuePairs = new ArrayList<BasicNameValuePair>();
        bodyNameValuePairs.add(new BasicNameValuePair("deviceID", deviceId));
        bodyNameValuePairs.add(new BasicNameValuePair("deviceToken", gcmId));

        post.setEntity(JiveEntityUtil.createForm(bodyNameValuePairs));

        return post;
    }

    @Nonnull
    public HttpPost updateFollowingIn(String pathAndQuery, List<StreamEntity> streamEntities) {
        URI uri = uriFactory.createURI(pathAndQuery);
        HttpPost post = new HttpPost(uri);
        if (streamEntities.size() > 0) {
            StreamEntity[] entities = new StreamEntity[streamEntities.size()];
            streamEntities.toArray(entities);
            post.setEntity(JsonEntity.from(jiveJson, entities));
        } else {
            post.setEntity(JsonEntity.from(jiveJson, Collections.emptyList()));
        }
        return post;
    }

    @Nonnull
    public HttpPost updateFollowingIn(JiveObjectEntity<?> objectEntity, List<StreamEntity> streamEntities) {
        return updateFollowingIn(objectEntity.resources.get("followingIn").ref, streamEntities);
    }

    @Nonnull
    public HttpPost createPlace(PlaceEntity placeEntity) {
        URI uri = uriFactory.placesUri();
        HttpPost createPlaceHttpPost = new HttpPost(uri);
        createPlaceHttpPost.setEntity(JsonEntity.from(jiveJson, placeEntity));
        return createPlaceHttpPost;
    }

    @Nonnull
    public HttpPost createRsvp(String eventRsvpPathAndQuery, String rsvpVal) {
        try {
            HttpPost createPlaceHttpPost = new HttpPost(eventRsvpPathAndQuery);
            createPlaceHttpPost.setEntity(new JsonEntity(rsvpVal));
            return createPlaceHttpPost;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UnsupportedEncodingException for UTF-8 should be impossible", e);
        }
    }

    @Nonnull
    public HttpPost completeMission(String mission) {
        URI uri = uriFactory.completedMissionUri(mission);
        HttpPost completeMissionHttpPost = new HttpPost(uri);
        return completeMissionHttpPost;
    }

    @Nonnull
    public HttpPost authorizeDeviceFromSession() {
        final URI uri = uriFactory.oAuth2RequestUri();
        HttpPost authorizeDeviceHttpPost = new HttpPost(uri);
        authorizeDeviceHttpPost.setHeader(JiveCoreHeaders.AUTHORIZATION, "Basic " + oauthCredentials);

        authorizeDeviceHttpPost.setEntity(JiveEntityUtil.createForm("grant_type", "session"));

        return authorizeDeviceHttpPost;
    }

    @Nonnull
    public HttpPost deauthorizeDevice() {
        URI uri = uriFactory.oAuth2RevokeUri();
        HttpPost deauthorizeDevicePost = new HttpPost(uri);

        return deauthorizeDevicePost;
    }

    @Nonnull
    public HttpGet fetchModerationPending() {
        URI uri = uriFactory.fetchModerationPending();
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpPut updateModeration(ModerationEntity moderationEntity) {
        URI uri = uriFactory.updateModeration(moderationEntity.id);
        HttpPut put = new HttpPut(uri);
        put.setEntity(JsonEntity.from(jiveJson, moderationEntity));
        return put;
    }
}
