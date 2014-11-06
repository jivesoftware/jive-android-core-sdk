package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.httpclient.util.JiveEntityUtil;
import com.jivesoftware.android.mobile.sdk.entity.BatchRequestEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentEntity;
import com.jivesoftware.android.mobile.sdk.entity.JiveObjectEntity;
import com.jivesoftware.android.mobile.sdk.entity.NewMemberEntity;
import com.jivesoftware.android.mobile.sdk.entity.PlaceEntity;
import com.jivesoftware.android.mobile.sdk.entity.StreamEntity;
import com.jivesoftware.android.mobile.sdk.http.JsonBody;
import com.jivesoftware.android.mobile.sdk.http.JsonEntity;
import com.jivesoftware.android.mobile.sdk.json.JiveJson;
import com.jivesoftware.android.mobile.sdk.util.JiveURIUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;

import javax.annotation.Nonnull;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JiveCoreRequestFactory {
    @Nonnull
    private final URL baseURL;
    @Nonnull
    private final JiveJson jiveJson;


    public JiveCoreRequestFactory(@Nonnull URL baseURL, @Nonnull JiveJson jiveJson) {
        this.baseURL = baseURL;
        this.jiveJson = jiveJson;
    }

    @Nonnull
    public HttpGet fetchMetadataProperties() {
        URI fetchMetadataPropertiesURI = JiveURIUtil.createURI(baseURL, JiveCoreEndpoints.METADATA_PROPERTIES);
        return new HttpGet(fetchMetadataPropertiesURI);
    }

    @Nonnull
    public HttpGet fetchMePerson() {
        URI fetchMePersonURI = JiveURIUtil.createURI(baseURL, JiveCoreEndpoints.ME_URL);
        return new HttpGet(fetchMePersonURI);
    }

    @Nonnull
    public HttpGet fetchInbox(@Nonnull JiveCoreQueryParameterProvider options) {
        URI inboxURI = JiveURIUtil.createURI(baseURL, JiveCoreEndpoints.INBOX, options);
        return new HttpGet(inboxURI);
    }

    public HttpPost executeBatchOperation(@Nonnull BatchRequestEntity[] requestEntities) {
        URI uri = JiveURIUtil.createURI(baseURL, JiveCoreConstants.CORE_API_V3_PREFIX + "/executeBatch");
        HttpPost post = new HttpPost(uri);
        post.setEntity(JsonEntity.from(jiveJson, requestEntities));
        return post;
    }

    @Nonnull
    public HttpGet createHttpGet(@Nonnull String requestPathAndQuery) {
        URI uri = JiveURIUtil.createURI(baseURL, requestPathAndQuery);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpPost createHttpPost(@Nonnull String requestPathAndQuery) {
        URI uri = JiveURIUtil.createURI(baseURL, requestPathAndQuery);
        return new HttpPost(uri);
    }

    @Nonnull
    public HttpDelete createHttpDelete(@Nonnull String requestPathAndQuery) {
        URI uri = JiveURIUtil.createURI(baseURL, requestPathAndQuery);
        return new HttpDelete(uri);
    }

    @Nonnull
    public HttpGet searchContents(@Nonnull JiveCoreQueryParameterProvider options) {
        URI uri = JiveURIUtil.createURI(baseURL, JiveCoreEndpoints.SEARCH_CONTENT, options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchActivityByPerson(@Nonnull String personUrl, @Nonnull JiveCoreQueryParameterProvider options) {
        URI uri = JiveURIUtil.createURI(baseURL, personUrl + "/activities", options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchContents(@Nonnull JiveCoreQueryParameterProvider options) {
        URI uri = JiveURIUtil.createURI(baseURL, JiveCoreEndpoints.CONTENT_ROOT, options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchPlaces(@Nonnull JiveCoreQueryParameterProvider options) {
        URI uri = JiveURIUtil.createURI(baseURL, JiveCoreEndpoints.PLACES_ROOT, options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchPeople(@Nonnull JiveCoreQueryParameterProvider options) {
        URI uri = JiveURIUtil.createURI(baseURL, JiveCoreEndpoints.PEOPLE_ROOT, options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet searchPeople(@Nonnull JiveCoreQueryParameterProvider options) {
        URI uri = JiveURIUtil.createURI(baseURL, JiveCoreEndpoints.SEARCH_PEOPLE, options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet searchPlaces(@Nonnull JiveCoreQueryParameterProvider options) {
        URI uri = JiveURIUtil.createURI(baseURL, JiveCoreEndpoints.SEARCH_PLACES, options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchMembersByPerson(@Nonnull String personID, @Nonnull JiveCoreQueryParameterProvider options) {
        URI uri = JiveURIUtil.createURI(baseURL, JiveCoreEndpoints.PERSON_MEMBERSHIPS_ROOT + "/" + personID, options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpPost createMembership(@Nonnull String placeID, @Nonnull NewMemberEntity newMemberEntity) {
        URI uri = JiveURIUtil.createURI(baseURL, JiveCoreEndpoints.PLACE_MEMBERSHIPS_ROOT + "/" + placeID);
        HttpPost joinPlacePost = new HttpPost(uri);
        joinPlacePost.setEntity(JsonEntity.from(jiveJson, newMemberEntity));
        return joinPlacePost;
    }

    @Nonnull
    public HttpGet fetchMembersByPlace(@Nonnull String placeID, @Nonnull JiveCoreQueryParameterProvider options) {
        URI uri = JiveURIUtil.createURI(baseURL, JiveCoreEndpoints.PLACE_MEMBERSHIPS_ROOT + "/" + placeID, options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchMetadataObject(@Nonnull String metadataObjectName, @Nonnull String locale) {
        URI uri = JiveURIUtil.createURI(baseURL, JiveCoreEndpoints.METADATA_OBJECT_ROOT + "/" + metadataObjectName);
        HttpGet get = new HttpGet(uri);
        get.setHeader(JiveCoreHeaders.ACCEPT_LANGUAGE, locale);
        return get;
    }

    @Nonnull
    public HttpPost createContent(@Nonnull String pathAndQuery, @Nonnull ContentEntity contentEntity, @Nonnull List<FileBody> fileBodies) {
        URI uri = JiveURIUtil.createURI(baseURL, pathAndQuery);
        HttpPost post = new HttpPost(uri);
        HttpEntity entity = createHttpEntity(contentEntity, fileBodies);
        post.setEntity(entity);
        return post;
    }

    @Nonnull
    public HttpPut updateContent(@Nonnull ContentEntity contentEntity, @Nonnull List<FileBody> fileBodies) {
        URI uri = JiveURIUtil.createURI(baseURL, contentEntity.resources.get("self").ref);
        HttpPut put = new HttpPut(uri);
        HttpEntity entity = createHttpEntity(contentEntity, fileBodies);
        put.setEntity(entity);
        return put;
    }

    private HttpEntity createHttpEntity(@Nonnull Object entity, @Nonnull List<FileBody> fileBodies) {
        HttpEntity httpEntity;
        if (fileBodies.isEmpty()) {
            httpEntity = JsonEntity.from(jiveJson, entity);
        } else {
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            String json = jiveJson.toJson(entity);
            multipartEntity.addPart("json", JsonBody.create(json));

            int fileNumber = 1;
            for (FileBody fileBody : fileBodies) {
                multipartEntity.addPart("file" + fileNumber, fileBody);
                fileNumber++;
            }
            httpEntity = multipartEntity;
        }
        return httpEntity;
    }

    @Nonnull
    public HttpGet fetchContent(@Nonnull String pathAndQuery, @Nonnull JiveCoreQueryParameterProvider options) {
        URI uri = JiveURIUtil.createURI(baseURL, pathAndQuery, options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpGet fetchReplies(@Nonnull String pathAndQuery, @Nonnull JiveCoreQueryParameterProvider options) {
        URI uri = JiveURIUtil.createURI(baseURL, pathAndQuery, options);
        return new HttpGet(uri);
    }

    @Nonnull
    public HttpPost uploadImage(@Nonnull FileBody imageFileBody) {
        URI uri = JiveURIUtil.createURI(baseURL, JiveCoreEndpoints.IMAGES_ROOT);
        HttpPost post = new HttpPost(uri);

        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntity.addPart("image", imageFileBody);
        post.setEntity(multipartEntity);

        return post;
    }

    @Nonnull
    public HttpPost registerForPush(@Nonnull String gcmId, @Nonnull String deviceId) {
        URI uri = JiveURIUtil.createURI(baseURL, "/api/core/mobile/v1/pushNotification/register");
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
        URI uri = JiveURIUtil.createURI(baseURL, "/api/core/mobile/v1/pushNotification/unregister");
        HttpPost post = new HttpPost(uri);

        ArrayList<BasicNameValuePair> bodyNameValuePairs = new ArrayList<BasicNameValuePair>();
        bodyNameValuePairs.add(new BasicNameValuePair("deviceID", deviceId));
        bodyNameValuePairs.add(new BasicNameValuePair("deviceToken", gcmId));

        post.setEntity(JiveEntityUtil.createForm(bodyNameValuePairs));

        return post;
    }

    @Nonnull
    public HttpPost updateFollowingIn(@Nonnull String pathAndQuery, @Nonnull List<StreamEntity> streamEntities) {
        URI uri = JiveURIUtil.createURI(baseURL, pathAndQuery);
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
    public HttpPost updateFollowingIn(@Nonnull JiveObjectEntity<?> objectEntity, @Nonnull List<StreamEntity> streamEntities) {
        return updateFollowingIn(objectEntity.resources.get("followingIn").ref, streamEntities);
    }

    @Nonnull
    public HttpPost createPlace(@Nonnull PlaceEntity placeEntity) {
        URI uri = JiveURIUtil.createURI(baseURL, JiveCoreEndpoints.PLACES_ROOT);
        HttpPost createPlaceHttpPost = new HttpPost(uri);
        createPlaceHttpPost.setEntity(JsonEntity.from(jiveJson, placeEntity));
        return createPlaceHttpPost;
    }

    @Nonnull
    public HttpPost completeMission(@Nonnull String mission) {
        String path = "api/core/mobile/v1/quest/" + mission;
        URI uri = JiveURIUtil.createURI(baseURL, path);
        HttpPost completeMissionHttpPost = new HttpPost(uri);
        return completeMissionHttpPost;
    }
}
