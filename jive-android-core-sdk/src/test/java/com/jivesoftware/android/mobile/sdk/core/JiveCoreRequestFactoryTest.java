package com.jivesoftware.android.mobile.sdk.core;

import com.google.common.collect.ImmutableList;
import com.jivesoftware.android.httpclient.util.JiveEntityUtil;
import com.jivesoftware.android.mobile.httpclient.matcher.HttpMatchers;
import com.jivesoftware.android.mobile.sdk.entity.BatchRequestEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentBodyEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentEntity;
import com.jivesoftware.android.mobile.sdk.entity.EndpointRequestEntity;
import com.jivesoftware.android.mobile.sdk.entity.RequestMethod;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreObjectType;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreSort;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreValueFactory;
import com.jivesoftware.android.mobile.sdk.http.JsonBody;
import com.jivesoftware.android.mobile.sdk.http.JsonEntity;
import com.jivesoftware.android.mobile.sdk.json.JiveJson;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jivesoftware.android.mobile.httpclient.matcher.HttpMatchers.header;
import static com.jivesoftware.android.mobile.httpclient.matcher.HttpMatchers.requestEntity;
import static com.jivesoftware.android.mobile.httpclient.matcher.HttpMatchers.requestHeaders;
import static com.jivesoftware.android.mobile.httpclient.matcher.HttpMatchers.requestUrl;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.AllOf.allOf;

public class JiveCoreRequestFactoryTest {
    private JiveJson json = new JiveJson();
    private JiveCoreRequestFactory testObject;

    @Before
    public void setUp() throws MalformedURLException {
        testObject = new JiveCoreRequestFactory("oauthCredentials", new URL("http://jiveland.com"), json);
    }

    @Test
    public void fetchInbox() {
        JiveCoreRequestOptions options = new JiveCoreRequestOptions();
        options.setCount(1);
        options.setUnreadFilter(true);
        options.setAuthorFilter(Collections.singletonList("/the/author"));
        options.setTypeFilter(ImmutableList.of(
                JiveCoreValueFactory.createObjectTypeValue("typeUUUU"),
                JiveCoreValueFactory.createObjectTypeValue("typeTTTT")));
        options.setDirectives(ImmutableList.of(
                JiveCoreValueFactory.createDirectiveValue("prime(www)"),
                JiveCoreValueFactory.createDirectiveValue("subprime(vvvv)")));
        options.setCollapseSkipCollectionIdsDirective(ImmutableList.of("zzz", "yyy"));
        options.setOldestUnread(true);

        HttpGet fetchInboxHttpGet = testObject.fetchInbox(options);

        assertThat(fetchInboxHttpGet, requestUrl(
                "http://jiveland.com/api/core/v3/inbox?" +
                        "count=1&" +
                        "oldestUnread=true&" +
                        "filter=unread&" +
                        "filter=author%28%2Fthe%2Fauthor%29&" +
                        "filter=type%28typeUUUU%2CtypeTTTT%29&" +
                        "directive=prime%28www%29&" +
                        "directive=subprime%28vvvv%29&" +
                        "directive=collapseSkip%28zzz%2Cyyy%29"));
    }

    @Test
    public void fetchPeople() {
        JiveCoreRequestOptions options = new JiveCoreRequestOptions();
        options.setSort(JiveCoreSort.firstNameAsc);
        HttpGet httpGet = testObject.fetchPeople(options);
        assertThat(httpGet, requestUrl("http://jiveland.com/api/core/v3/people?sort=firstNameAsc"));
    }

    @Test
    public void searchPeople() {
        JiveCoreRequestOptions options = new JiveCoreRequestOptions();
        options.setCount(25);
        options.setSearchTermFilter(Arrays.asList("heath", "borders"));

        HttpGet searchPeopleHttpGet = testObject.searchPeople(options);

        assertThat(searchPeopleHttpGet, requestUrl("http://jiveland.com/api/core/v3/search/people?count=25&filter=search%28heath%2Cborders%29"));
    }


    @Test
    public void searchPlaces() {
        JiveCoreRequestOptions options = new JiveCoreRequestOptions();
        options.setCount(25);
        options.setSearchTermFilter(Arrays.asList("sales", "marketing"));

        HttpGet searchPlacesHttpGet = testObject.searchPlaces(options);

        assertThat(searchPlacesHttpGet, requestUrl("http://jiveland.com/api/core/v3/search/places?count=25&filter=search%28sales%2Cmarketing%29"));
    }

    @Test
    public void fetchMetadataObject() {
        HttpGet fetchMetadataObjectHttpGet = testObject.fetchMetadataObject("profile", "en");

        assertThat(fetchMetadataObjectHttpGet, allOf(
                requestUrl("http://jiveland.com/api/core/v3/metadata/objects/profile"),
                requestHeaders(contains(header("Accept-Language", "en")))));
    }

    @Test
    public void createContentWithFileBodies() throws IOException {
        ContentEntity entity = new ContentEntity();
        entity.content = new ContentBodyEntity();
        entity.content.text = "Some text";
        entity.content.type = "text/plain";
        entity.subject = "Some subject";
        entity.type = JiveCoreObjectType.comment;

        List<FileBody> files = new ArrayList<FileBody>();
        files.add(new FileBody(new File("test-data/test1.txt"), "text/plain"));
        files.add(new FileBody(new File("test-data/test2.txt"), "text/foo"));

        HttpPost createContentHttpPost = testObject.createContent("/url", entity, files);

        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntity.addPart("json", new JsonBody(
                // @formatter:off
                "{" +
                        "\"type\":\"comment\"," +
                        "\"content\":{" +
                            "\"type\":\"text/plain\"," +
                            "\"text\":\"Some text\"" +
                        "}," +
                        "\"subject\":\"Some subject\"" +
                 "}"
                // @formatter:on
        ));
        multipartEntity.addPart("file1", new FileBody(new File("test-data/test1.txt"), "text/plain"));
        multipartEntity.addPart("file2", new FileBody(new File("test-data/test2.txt"), "text/foo"));

        assertThat(createContentHttpPost, allOf(
                requestUrl("http://jiveland.com/url"),
                requestEntity(multipartEntity)));
    }

    @Test
    public void createContentWithoutFileBodies() throws IOException {
        ContentEntity entity = new ContentEntity();
        entity.content = new ContentBodyEntity();
        entity.content.text = "Some text";
        entity.content.type = "text/plain";
        entity.subject = "Some subject";
        entity.type = JiveCoreObjectType.comment;

        List<FileBody> fileBodies = new ArrayList<FileBody>();
        HttpPost createContentHttpPost = testObject.createContent("/url", entity, fileBodies);

        assertThat(createContentHttpPost, allOf(
                requestUrl("http://jiveland.com/url"),
                requestEntity(new JsonEntity(
                        // @formatter:off
                        "{" +
                                "\"type\":\"comment\"," +
                                "\"content\":{" +
                                    "\"type\":\"text/plain\"," +
                                    "\"text\":\"Some text\"" +
                                "}," +
                                "\"subject\":\"Some subject\"" +
                         "}"
                        // @formatter:on
                ))));
    }

    @Test
    public void executeBatchOperation() throws IOException {
        EndpointRequestEntity endpointRequestEntity1 = new EndpointRequestEntity();
        endpointRequestEntity1.method = (RequestMethod.GET);
        endpointRequestEntity1.endpoint = ("/url1");

        EndpointRequestEntity endpointRequestEntity2 = new EndpointRequestEntity();
        endpointRequestEntity2.method = (RequestMethod.POST);
        endpointRequestEntity2.endpoint = ("/url2");

        EndpointRequestEntity endpointRequestEntity3 = new EndpointRequestEntity();
        endpointRequestEntity3.method = (RequestMethod.GET);
        endpointRequestEntity3.endpoint = ("/url3");

        BatchRequestEntity batchRequestEntity1 = new BatchRequestEntity();
        batchRequestEntity1.key = ("entity1");
        batchRequestEntity1.request = (endpointRequestEntity1);

        BatchRequestEntity batchRequestEntity2 = new BatchRequestEntity();
        batchRequestEntity2.key = ("entity2");
        batchRequestEntity2.request = (endpointRequestEntity2);

        BatchRequestEntity batchRequestEntity3 = new BatchRequestEntity();
        batchRequestEntity3.key = ("entity3");
        batchRequestEntity3.request = (endpointRequestEntity3);

        HttpPost executeBatchOperationHttpPost = testObject.executeBatchOperation(new BatchRequestEntity[]{batchRequestEntity1, batchRequestEntity2, batchRequestEntity3});

        assertThat(executeBatchOperationHttpPost, allOf(
                requestUrl("http://jiveland.com/api/core/v3/executeBatch"),
                requestEntity(new JsonEntity(
                        // @formatter:off
                    "[" +
                        "{" +
                            "\"key\":\"entity1\"," +
                            "\"request\":{" +
                                "\"method\":\"GET\"," +
                                "\"endpoint\":\"/url1\"" +
                            "}" +
                        "}," +
                        "{" +
                            "\"key\":\"entity2\"," +
                            "\"request\":{" +
                                "\"method\":\"POST\"," +
                                "\"endpoint\":\"/url2\"" +
                            "}" +
                        "}," +
                        "{" +
                            "\"key\":\"entity3\"," +
                            "\"request\":{" +
                                "\"method\":\"GET\"," +
                                "\"endpoint\":\"/url3\"" +
                            "}" +
                        "}" +
                    "]"
                    // @formatter:on
                ))));
    }

    @Test
    public void uploadImage() throws IOException {
        String encoding = "image/png";
        File imageFile = new File("test-data/test-1px-red.png");
        FileBody fileBody = new FileBody(imageFile, encoding);

        HttpPost uploadImageHttpPost = testObject.uploadImage(fileBody);

        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntity.addPart("image", new FileBody(imageFile, encoding));

        assertThat(uploadImageHttpPost, allOf(
                requestUrl("http://jiveland.com/api/core/v3/images"),
                requestEntity(multipartEntity)));
    }

    @Test
    public void fetchMetadataProperties() {
        HttpGet fetchMetadataPropertiesHttpGet = testObject.fetchMetadataProperties();

        assertThat(fetchMetadataPropertiesHttpGet, allOf(
                requestUrl("http://jiveland.com/api/core/v3/metadata/properties"),
                HttpMatchers.requestMethod("GET")));
    }


    @Test
    public void fetchContents() {
        JiveCoreRequestOptions contentRequestOptions = new JiveCoreRequestOptions();
        contentRequestOptions.setCount(25);

        HttpGet fetchContentsHttpGet = testObject.fetchContents(contentRequestOptions);

        assertThat(fetchContentsHttpGet, requestUrl("http://jiveland.com/api/core/v3/contents?count=25"));
    }

    @Test
    public void searchContents() {
        JiveCoreRequestOptions searchContentRequestOptions = new JiveCoreRequestOptions();
        searchContentRequestOptions.setCount(25);

        HttpGet searchContentWithOptionsHttpGet = testObject.searchContents(searchContentRequestOptions);

        assertThat(searchContentWithOptionsHttpGet, requestUrl("http://jiveland.com/api/core/v3/search/contents?count=25"));
    }

    @Test
    public void createHttpGet() {
        HttpGet searchContentWithRequestPathAndQueryHttpGet = testObject.createHttpGet("/path?query=foo");

        assertThat(searchContentWithRequestPathAndQueryHttpGet,
                requestUrl("http://jiveland.com/path?query=foo"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void fetchMembersByPerson() {
        JiveCoreRequestOptions countRequestOptions = new JiveCoreRequestOptions();
        countRequestOptions.setCount(25);

        HttpGet fetchMembersByPersonHttpGet = testObject.fetchMembersByPerson("9999", countRequestOptions);

        assertThat(fetchMembersByPersonHttpGet, requestUrl("http://jiveland.com/api/core/v3/members/people/9999?count=25"));
    }

    @Test
    public void fetchContent() {
        JiveCoreRequestOptions options = new JiveCoreRequestOptions();
        options.setFields(Arrays.asList("type"));

        HttpGet fetchContentHttpGet = testObject.fetchContent("/url", options);

        assertThat(fetchContentHttpGet, requestUrl("http://jiveland.com/url?fields=type"));
    }

    @Test
    public void fetchReplies() {
        JiveCoreRequestOptions options = new JiveCoreRequestOptions();
        options.setStartIndex(1);

        HttpGet fetchRepliesHttpGet = testObject.fetchReplies("/url", options);

        assertThat(fetchRepliesHttpGet, requestUrl("http://jiveland.com/url?startIndex=1"));
    }

    @Test
    public void registerForPush() throws IOException {
        HttpPost registerForPushHttpPost = testObject.registerForMobile4Push("foo", "bar");

        ArrayList<BasicNameValuePair> bodyNameValuePairs = new ArrayList<BasicNameValuePair>();
        bodyNameValuePairs.add(new BasicNameValuePair("deviceID", "bar"));
        bodyNameValuePairs.add(new BasicNameValuePair("deviceToken", "foo"));
        bodyNameValuePairs.add(new BasicNameValuePair("deviceType", "6"));
        bodyNameValuePairs.add(new BasicNameValuePair("activated", "true"));
        assertThat(registerForPushHttpPost, allOf(
                requestUrl("http://jiveland.com/api/core/mobile/v1/pushNotification/register"),
                requestEntity(JiveEntityUtil.createForm(bodyNameValuePairs))));
    }

    @Test
    public void unregisterFromPush() throws IOException {
        HttpPost post = testObject.unregisterFromPush("foo", "bar");

        ArrayList<BasicNameValuePair> bodyNameValuePairs = new ArrayList<BasicNameValuePair>();
        bodyNameValuePairs.add(new BasicNameValuePair("deviceID", "bar"));
        bodyNameValuePairs.add(new BasicNameValuePair("deviceToken", "foo"));
        assertThat(post, allOf(
                requestUrl("http://jiveland.com/api/core/mobile/v1/pushNotification/unregister"),
                requestEntity(JiveEntityUtil.createForm(bodyNameValuePairs))));
    }

    @Test
    public void completeMission() throws IOException {
        HttpPost completeMissionHttpPost = testObject.completeMission("FOO");

        assertThat(completeMissionHttpPost, requestUrl("http://jiveland.com/api/core/mobile/v1/quest/FOO"));
    }

    @Test
    public void testWhenAuthorizeDeviceFromSessionThenRequestIsCreatedProperly() throws IOException {
        HttpPost post = testObject.authorizeDeviceFromSession();

        assertEquals(post.getURI(), URI.create("http://jiveland.com/oauth2/token"));
        assertEquals(post.getFirstHeader(JiveCoreHeaders.AUTHORIZATION).getValue(), "Basic oauthCredentials");
        assertEquals(JiveEntityUtil.toString(post.getEntity()), "grant_type=session");
    }

    @Test
    public void testWhenDeauthorizeDeviceThenRequestIsCreatedProperly() throws IOException {
        HttpPost post = testObject.deauthorizeDevice();

        assertEquals(post.getURI(), URI.create("http://jiveland.com/oauth2/revoke"));
    }

    @Test
    public void testWhenFetchImage() throws Exception {
        JiveCoreRequestOptions options = new JiveCoreRequestOptions().setWidth(123).setHeight(456).setPreserveAspectRatio(true);
        HttpGet get = testObject.fetchImage("/url", options);
        assertEquals(get.getURI(), URI.create("http://jiveland.com/url?width=123&height=456&preserveAspectRatio=true"));
    }
}
