package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreContentRequestOptions;
import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreCountRequestOptions;
import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreInboxOptions;
import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreSearchContentRequestOptions;
import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreSearchPeopleRequestOptions;
import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreSearchPlacesRequestOptions;
import com.jivesoftware.android.mobile.sdk.entity.ActivityListEntity;
import com.jivesoftware.android.mobile.sdk.entity.BatchRequestEntity;
import com.jivesoftware.android.mobile.sdk.entity.BatchResponseEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentListEntity;
import com.jivesoftware.android.mobile.sdk.entity.ImageEntity;
import com.jivesoftware.android.mobile.sdk.entity.ImageListEntity;
import com.jivesoftware.android.mobile.sdk.entity.JiveObjectEntity;
import com.jivesoftware.android.mobile.sdk.entity.ListEntity;
import com.jivesoftware.android.mobile.sdk.entity.MemberEntity;
import com.jivesoftware.android.mobile.sdk.entity.MemberListEntity;
import com.jivesoftware.android.mobile.sdk.entity.MetadataObjectEntity;
import com.jivesoftware.android.mobile.sdk.entity.MetadataPropertyEntity;
import com.jivesoftware.android.mobile.sdk.entity.NewMemberEntity;
import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import com.jivesoftware.android.mobile.sdk.entity.PersonListEntity;
import com.jivesoftware.android.mobile.sdk.entity.PlaceEntity;
import com.jivesoftware.android.mobile.sdk.entity.PlaceListEntity;
import com.jivesoftware.android.mobile.sdk.entity.StreamEntity;
import com.jivesoftware.android.mobile.sdk.entity.StreamListEntity;
import com.jivesoftware.android.mobile.sdk.gson.JiveGson;
import com.jivesoftware.android.mobile.sdk.httpclient.JiveCoreHttpClientAuthUtils;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreExceptionFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.AbstractHttpClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

@ParametersAreNonnullByDefault
public class JiveCore {
    static {
        // Uncomment to turn on HttpClient Wire Debugging
        //noinspection ConstantConditions,ConstantIfStatement
        if (false) {
            java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);

            System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
            System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
            System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
            System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "debug");
            System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "debug");
        }

        // also run the following in adb
        // adb shell setprop log.tag.org.apache.http VERBOSE
        // adb shell setprop log.tag.org.apache.http.wire VERBOSE
        // adb shell setprop log.tag.org.apache.http.headers VERBOSE
    }

    @Nonnull
    private final JiveCoreRequestFactory jiveCoreRequestFactory;
    @Nonnull
    private final JiveCoreGsonCallableFactory jiveCoreGsonCallableFactory;
    @Nonnull
    private final JiveCoreEmptyCallableFactory jiveCoreEmptyCallableFactory;
    @Nonnull
    private final JiveCoreInputStreamCallableFactory jiveCoreInputStreamCallableFactory;
    @Nonnull
    private final JiveCoreGenericCallableFactory jiveCoreGenericCallableFactory;

    public JiveCore(
            URL baseURL,
            AbstractHttpClient httpClient,
            JiveCoreTokenEntityStore tokenEntityStore,
            JiveCoreTokenEntityRefresher tokenEntityRefresher) {
        this(new JiveCoreRequestFactory(baseURL), JiveCoreHttpClientAuthUtils.initHttpClientAuth(httpClient, tokenEntityStore, tokenEntityRefresher), new JiveGson());
    }

    public JiveCore(
            JiveCoreRequestFactory jiveCoreRequestFactory,
            HttpClient httpClient,
            JiveGson jiveGson) {
        this(jiveCoreRequestFactory, httpClient, jiveGson, new JiveCoreExceptionFactory(jiveGson));
    }

    public JiveCore(
            JiveCoreRequestFactory jiveCoreRequestFactory,
            HttpClient httpClient,
            JiveGson jiveGson,
            JiveCoreExceptionFactory jiveCoreExceptionFactory) {
        this(
                jiveCoreRequestFactory,
                new JiveCoreGsonCallableFactory(httpClient, jiveGson, jiveCoreExceptionFactory),
                new JiveCoreEmptyCallableFactory(httpClient, jiveCoreExceptionFactory),
                new JiveCoreInputStreamCallableFactory(httpClient, jiveCoreExceptionFactory),
                new JiveCoreGenericCallableFactory(httpClient, jiveCoreExceptionFactory));
    }

    public JiveCore(
            JiveCoreRequestFactory jiveCoreRequestFactory,
            JiveCoreGsonCallableFactory jiveCoreGsonCallableFactory,
            JiveCoreEmptyCallableFactory jiveCoreEmptyCallableFactory,
            JiveCoreInputStreamCallableFactory jiveCoreInputStreamCallableFactory,
            JiveCoreGenericCallableFactory jiveCoreGenericCallableFactory) {
        this.jiveCoreRequestFactory = jiveCoreRequestFactory;
        this.jiveCoreGsonCallableFactory = jiveCoreGsonCallableFactory;
        this.jiveCoreEmptyCallableFactory = jiveCoreEmptyCallableFactory;
        this.jiveCoreInputStreamCallableFactory = jiveCoreInputStreamCallableFactory;
        this.jiveCoreGenericCallableFactory = jiveCoreGenericCallableFactory;
    }

    @Nonnull
    public JiveCoreCallable<MetadataPropertyEntity[]> fetchMetadataProperties() {
        HttpGet get = jiveCoreRequestFactory.fetchMetadataProperties();
        return jiveCoreGsonCallableFactory.createGsonCallable(get, MetadataPropertyEntity[].class);
    }

    @Nonnull
    public JiveCoreCallable<PersonEntity> fetchMePerson() {
        HttpGet fetchMePersonHttpGet = jiveCoreRequestFactory.fetchMePerson();
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchMePersonHttpGet, PersonEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<StreamListEntity> fetchStreams(String streamsPathAndQuery) {
        HttpGet fetchStreamsHttpGet = jiveCoreRequestFactory.createHttpGet(streamsPathAndQuery);
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchStreamsHttpGet, StreamListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<ActivityListEntity> fetchActivities(String requestPathAndQuery) {
        HttpGet fetchActivitiesHttpGet = jiveCoreRequestFactory.createHttpGet(requestPathAndQuery);
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchActivitiesHttpGet, ActivityListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<ActivityListEntity> fetchInbox(JiveCoreInboxOptions options) {
        HttpGet fetchInboxHttpGet = jiveCoreRequestFactory.fetchInbox(options);
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchInboxHttpGet, ActivityListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<ActivityListEntity> fetchAllActivities() {
        return fetchActivities(JiveCoreEndpoints.ALL_ACTIVITY);
    }

    @Nonnull
    public JiveCoreCallable<Void> markInboxEntryAsRead(String markReadPathAndQuery) {
        HttpPost markInboxEntryAsReadHttpPost = jiveCoreRequestFactory.createHttpPost(markReadPathAndQuery);
        return jiveCoreEmptyCallableFactory.createEmptyCallable(markInboxEntryAsReadHttpPost);
    }

    @Nonnull
    public JiveCoreCallable<Void> markInboxEntryAsUnread(String markUnreadPathAndQuery) {
        HttpDelete markInboxEntryAsUnreadHttpDelete = jiveCoreRequestFactory.createHttpDelete(markUnreadPathAndQuery);
        return jiveCoreEmptyCallableFactory.createEmptyCallable(markInboxEntryAsUnreadHttpDelete);
    }

    @Nonnull
    public JiveCoreCallable<BatchResponseEntity[]> executeBatchOperation(BatchRequestEntity[] requestEntities) {
        HttpPost executeBatchOperationHttpPost = jiveCoreRequestFactory.executeBatchOperation(requestEntities);
        return jiveCoreGsonCallableFactory.createGsonCallable(executeBatchOperationHttpPost, BatchResponseEntity[].class);
    }

    @Nonnull
    public JiveCoreCallable<ContentListEntity> searchContents(String requestPathAndQuery) {
        HttpGet searchContentsHttpGet = jiveCoreRequestFactory.createHttpGet(requestPathAndQuery);
        return jiveCoreGsonCallableFactory.createGsonCallable(searchContentsHttpGet, ContentListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<ContentListEntity> fetchContents(JiveCoreContentRequestOptions options) {
        HttpGet fetchContentsHttpGet = jiveCoreRequestFactory.fetchContents(options);
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchContentsHttpGet, ContentListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<ContentListEntity> fetchContents(String pathAndQuery) {
        HttpGet fetchContentsHttpGet = jiveCoreRequestFactory.createHttpGet(pathAndQuery);
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchContentsHttpGet, ContentListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<ContentListEntity> searchContents(JiveCoreSearchContentRequestOptions options) {
        HttpGet searchContentsHttpGet = jiveCoreRequestFactory.searchContents(options);
        return jiveCoreGsonCallableFactory.createGsonCallable(searchContentsHttpGet, ContentListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<PersonListEntity> searchPeople(String requestPathAndQuery) {
        HttpGet searchPeopleHttpGet = jiveCoreRequestFactory.createHttpGet(requestPathAndQuery);
        return jiveCoreGsonCallableFactory.createGsonCallable(searchPeopleHttpGet, PersonListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<PersonListEntity> searchPeople(JiveCoreSearchPeopleRequestOptions options) {
        HttpGet searchPeopleHttpGet = jiveCoreRequestFactory.searchPeople(options);
        return jiveCoreGsonCallableFactory.createGsonCallable(searchPeopleHttpGet, PersonListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<PlaceListEntity> searchPlaces(JiveCoreSearchPlacesRequestOptions options) {
        HttpGet searchPlacesHttpGet = jiveCoreRequestFactory.searchPlaces(options);
        return jiveCoreGsonCallableFactory.createGsonCallable(searchPlacesHttpGet, PlaceListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<PlaceListEntity> searchPlaces(String requestPathAndQuery) {
        HttpGet searchPlacesHttpGet = jiveCoreRequestFactory.createHttpGet(requestPathAndQuery);
        return jiveCoreGsonCallableFactory.createGsonCallable(searchPlacesHttpGet, PlaceListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<PersonEntity> fetchPerson(String personPathAndQuery) {
        HttpGet fetchPersonHttpGet = jiveCoreRequestFactory.createHttpGet(personPathAndQuery);
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchPersonHttpGet, PersonEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<PlaceEntity> fetchPlace(String placePathAndQuery) {
        HttpGet fetchPlaceHttpGet = jiveCoreRequestFactory.createHttpGet(placePathAndQuery);
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchPlaceHttpGet, PlaceEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<PlaceEntity> createPlace(PlaceEntity placeEntity) {
        HttpPost createPlaceHttpPost = jiveCoreRequestFactory.createPlace(placeEntity);
        return jiveCoreGsonCallableFactory.createGsonCallable(createPlaceHttpPost, PlaceEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<Void> deletePlace(String pathAndQuery) {
        HttpDelete deletePlaceHttpDelete = jiveCoreRequestFactory.createHttpDelete(pathAndQuery);
        return jiveCoreEmptyCallableFactory.createEmptyCallable(deletePlaceHttpDelete);
    }

    @Nonnull
    public JiveCoreCallable<MemberListEntity> fetchMembersByPerson(String personID, JiveCoreCountRequestOptions options) {
        HttpGet fetchMembersByPersonHttpGet = jiveCoreRequestFactory.fetchMembersByPerson(personID, options);
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchMembersByPersonHttpGet, MemberListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<MemberListEntity> fetchMembersByPlace(String placeID, JiveCoreCountRequestOptions options) {
        HttpGet fetchMembersByPlaceHttpGet = jiveCoreRequestFactory.fetchMembersByPlace(placeID, options);
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchMembersByPlaceHttpGet, MemberListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<MemberListEntity> fetchMembers(String pathAndQuery) {
        HttpGet fetchMembersHttpGet = jiveCoreRequestFactory.createHttpGet(pathAndQuery);
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchMembersHttpGet, MemberListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<MemberEntity> createMember(String placeID, NewMemberEntity newMemberEntity) {
        HttpPost createMembershipHttpPost = jiveCoreRequestFactory.createMembership(placeID, newMemberEntity);
        return jiveCoreGsonCallableFactory.createGsonCallable(createMembershipHttpPost, MemberEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<Void> deleteMember(String memberPathAndQuery) {
        HttpDelete deleteMemberHttpDelete = jiveCoreRequestFactory.createHttpDelete(memberPathAndQuery);
        return jiveCoreEmptyCallableFactory.createEmptyCallable(deleteMemberHttpDelete);
    }

    @Nonnull
    public JiveCoreCallable<MetadataObjectEntity> fetchMetadataObject(String metadataObjectName, String locale) {
        HttpGet fetchMetadataObjectHttpGet = jiveCoreRequestFactory.fetchMetadataObject(metadataObjectName, locale);
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchMetadataObjectHttpGet, MetadataObjectEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<Void> likeContent(String likePathAndQuery) {
        HttpPost likeContentHttpPost = jiveCoreRequestFactory.createHttpPost(likePathAndQuery);
        return jiveCoreEmptyCallableFactory.createEmptyCallable(likeContentHttpPost);
    }

    @Nonnull
    public JiveCoreCallable<Void> unlikeContent(String unlikePathAndQuery) {
        HttpDelete unlikeContentHttpDelete = jiveCoreRequestFactory.createHttpDelete(unlikePathAndQuery);
        return jiveCoreEmptyCallableFactory.createEmptyCallable(unlikeContentHttpDelete);
    }

    @Nonnull
    public JiveCoreCallable<Void> registerForPush(String gcmId, String deviceId) {
        HttpPost registerForPushHttpPost = jiveCoreRequestFactory.registerForPush(gcmId, deviceId);
        return jiveCoreEmptyCallableFactory.createEmptyCallable(registerForPushHttpPost);
    }

    @Nonnull
    public JiveCoreCallable<Void> unregisterFromPush(String gcmId, String deviceId) {
        HttpPost unregisterFromPushHttpPost = jiveCoreRequestFactory.unregisterFromPush(gcmId, deviceId);
        return jiveCoreEmptyCallableFactory.createEmptyCallable(unregisterFromPushHttpPost);
    }

    @Nonnull
    public JiveCoreCallable<ContentEntity> createContent(ContentEntity contentEntity, List<FileBody> fileBodies) {
        return createContent(JiveCoreEndpoints.CONTENT_ROOT, contentEntity, fileBodies);
    }

    @Nonnull
    public JiveCoreCallable<ContentEntity> createContent(String pathAndQuery, ContentEntity contentEntity, List<FileBody> fileBodies) {
        HttpPost createContentHttpPost = jiveCoreRequestFactory.createContent(pathAndQuery, contentEntity, fileBodies);
        return jiveCoreGsonCallableFactory.createGsonCallable(createContentHttpPost, ContentEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<ContentEntity> fetchContent(String pathAndQuery, JiveCoreContentRequestOptions options) {
        HttpGet fetchContentHttpGet = jiveCoreRequestFactory.fetchContent(pathAndQuery, options);
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchContentHttpGet, ContentEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<ContentEntity> updateContent(ContentEntity contentEntity, List<FileBody> fileBodies) {
        HttpPut updateContentHttpPut = jiveCoreRequestFactory.updateContent(contentEntity, fileBodies);
        return jiveCoreGsonCallableFactory.createGsonCallable(updateContentHttpPut, ContentEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<ContentListEntity> fetchReplies(String pathAndQuery, JiveCoreContentRequestOptions options) {
        HttpGet fetchRepliesHttpGet = jiveCoreRequestFactory.fetchReplies(pathAndQuery, options);
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchRepliesHttpGet, ContentListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<ImageListEntity> fetchImages(String pathAndQuery) {
       HttpGet fetchImagesHttpGet = jiveCoreRequestFactory.createHttpGet(pathAndQuery);
       return jiveCoreGsonCallableFactory.createGsonCallable(fetchImagesHttpGet, ImageListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<ImageEntity> uploadImage(FileBody imageFileBody) {
        HttpPost uploadImageHttpPost = jiveCoreRequestFactory.uploadImage(imageFileBody);
        return jiveCoreGsonCallableFactory.createGsonCallable(uploadImageHttpPost, ImageEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<InputStream> fetchImage(String pathAndQuery) {
        HttpGet fetchImageHttpGet = jiveCoreRequestFactory.createHttpGet(pathAndQuery);
        return jiveCoreInputStreamCallableFactory.createInputStreamCallable(fetchImageHttpGet);
    }

    @Nonnull
    public JiveCoreCallable<StreamListEntity> updateFollowingInEntitiesForObject(JiveObjectEntity objectEntity, List<StreamEntity> activityListEntity) {
        HttpPost updateFollowingInHttpPost = jiveCoreRequestFactory.updateFollowingIn(objectEntity, activityListEntity);
        return jiveCoreGsonCallableFactory.createGsonCallable(updateFollowingInHttpPost, StreamListEntity.class);
    }

    @Nonnull
    public JiveCoreCallable<StreamListEntity> updateFollowingInEntitiesForUrl(String url, List<StreamEntity> streamEntities) {
        HttpPost updateFollowingInHttpPost = jiveCoreRequestFactory.updateFollowingIn(url, streamEntities);
        return jiveCoreGsonCallableFactory.createGsonCallable(updateFollowingInHttpPost, StreamListEntity.class);
    }

    @Nonnull
    public <E extends ListEntity> JiveCoreCallable<E> fetchList(String requestPathAndQuery, Class<E> listEntityClass) {
        HttpGet fetchListHttpGet = jiveCoreRequestFactory.createHttpGet(requestPathAndQuery);
        return jiveCoreGsonCallableFactory.createGsonCallable(fetchListHttpGet, listEntityClass);
    }

    @Nonnull
    public <T> JiveCoreCallable<T> createCallable(HttpRequestBase httpRequestBase, HttpResponseParserFactory<T> httpResponseParserFactory) {
        return jiveCoreGenericCallableFactory.createGenericCallable(httpRequestBase, httpResponseParserFactory);
    }
}
