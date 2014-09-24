package com.jivesoftware.android.mobile.sdk.core;

import com.google.common.collect.ImmutableList;
import com.jivesoftware.android.httpclient.util.JiveEntityUtil;
import com.jivesoftware.android.mobile.httpclient.matcher.HttpMatchers;
import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreContentRequestOptions;
import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreCountRequestOptions;
import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreInboxOptions;
import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreSearchContentRequestOptions;
import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreSearchPeopleRequestOptions;
import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreSearchPlacesRequestOptions;
import com.jivesoftware.android.mobile.sdk.entity.BatchRequestEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentBodyEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentEntity;
import com.jivesoftware.android.mobile.sdk.entity.EndpointRequestEntity;
import com.jivesoftware.android.mobile.sdk.entity.RequestMethod;
import com.jivesoftware.android.mobile.sdk.http.JsonBody;
import com.jivesoftware.android.mobile.sdk.http.JsonEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.jivesoftware.android.mobile.httpclient.matcher.HttpMatchers.header;
import static com.jivesoftware.android.mobile.httpclient.matcher.HttpMatchers.requestEntity;
import static com.jivesoftware.android.mobile.httpclient.matcher.HttpMatchers.requestHeaders;
import static com.jivesoftware.android.mobile.httpclient.matcher.HttpMatchers.requestUrl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by mark.schisler on 8/19/14.
 */
public class JiveCoreRequestFactoryTest {

    @Nonnull
    private final String baseUrl = "http://jiveland.com";

    private JiveCoreRequestFactory testObject;

    @Before
    public void setUp() throws MalformedURLException {
        testObject = new JiveCoreRequestFactory(new URL(baseUrl));
    }

    @Test
    public void fetchInbox() {
        JiveCoreInboxOptions options = new JiveCoreInboxOptions();
        options.setCount(1);
        options.setUnread(true);
        options.setAuthorPathAndQuery("/the/author");
        options.setTypes(ImmutableList.of("typeUUUU", "typeTTTT"));
        options.setDirectives(ImmutableList.of("prime(www)", "subprime(vvvv)"));
        options.setCollapseSkipCollectionIds(ImmutableList.of("zzz", "yyy"));
        options.setOldestUnread(true);

        HttpGet fetchInboxHttpGet = testObject.fetchInbox(options);

        assertThat(fetchInboxHttpGet, requestUrl(
                "http://jiveland.com/api/core/v3/inbox?" +
                        "count=1&" +
                        "filter=unread&" +
                        "filter=author%28%2Fthe%2Fauthor%29&" +
                        "filter=type%28typeUUUU%2CtypeTTTT%29&" +
                        "directive=prime%28www%29%2Csubprime%28vvvv%29%2CcollapseSkip%28zzz%2Cyyy%29&" +
                        "oldestUnread=true"));
    }

    @Test
    public void searchPeople() {
        JiveCoreSearchPeopleRequestOptions options = new JiveCoreSearchPeopleRequestOptions();
        options.setCount(25);
        options.setSearchTerms(Arrays.asList("heath", "borders"));

        HttpGet get = testObject.searchPeople(options);

        assertThat(get, requestUrl("http://jiveland.com/api/core/v3/search/people?count=25&filter=search%28heath%2Cborders%29"));
    }


    @Test
    public void searchPlaces() {
        JiveCoreSearchPlacesRequestOptions options = new JiveCoreSearchPlacesRequestOptions();
        options.setCount(25);
        options.setSearchTerms(Arrays.asList("sales", "marketing"));

        HttpGet get = testObject.searchPlaces(options);

        assertThat(get, requestUrl("http://jiveland.com/api/core/v3/search/places?count=25&filter=search%28sales%2Cmarketing%29"));
    }

    @Test
    public void fetchMetadataObject() {
        HttpGet get = testObject.fetchMetadataObject("profile", "en");

        assertThat(get, allOf(
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
        entity.type = "comment";

        List<FileBody> files = new ArrayList<FileBody>();
        files.add(new FileBody(new File("test-data/test1.txt"), "text/plain"));
        files.add(new FileBody(new File("test-data/test2.txt"), "text/foo"));

        HttpPost post = testObject.createContent("/url", entity, files);

        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntity.addPart("json", new JsonBody(
                // @formatter:off
                "{" +
                        "\"content\":{" +
                            "\"type\":\"text/plain\"," +
                            "\"text\":\"Some text\"" +
                        "}," +
                        "\"subject\":\"Some subject\"," +
                        "\"type\":\"comment\"" +
                 "}"
                // @formatter:on
        ));
        multipartEntity.addPart("file1", new FileBody(new File("test-data/test1.txt"), "text/plain"));
        multipartEntity.addPart("file2", new FileBody(new File("test-data/test2.txt"), "text/foo"));

        assertThat(post, allOf(
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
        entity.type = "comment";

        List<FileBody> fileBodies = new ArrayList<FileBody>();
        HttpPost post = testObject.createContent("/url", entity, fileBodies);

        assertThat(post, allOf(
                requestUrl("http://jiveland.com/url"),
                requestEntity(new JsonEntity(
                        // @formatter:off
                        "{" +
                                "\"content\":{" +
                                    "\"type\":\"text/plain\"," +
                                    "\"text\":\"Some text\"" +
                                "}," +
                                "\"subject\":\"Some subject\"," +
                                "\"type\":\"comment\"" +
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

        HttpPost post = testObject.executeBatchOperation(new BatchRequestEntity[]{batchRequestEntity1, batchRequestEntity2, batchRequestEntity3});

        assertThat(post, allOf(
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

        HttpPost post = testObject.uploadImage(fileBody);

        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntity.addPart("image", new FileBody(imageFile, encoding));

        assertThat(post, allOf(
                requestUrl("http://jiveland.com/api/core/v3/images"),
                requestEntity(multipartEntity)));
    }

    @Test
    public void fetchMetadataProperties() {
        HttpGet get = testObject.fetchMetadataProperties();

        assertThat(get, allOf(
                requestUrl(baseUrl + JiveCoreConstants.CORE_API_V3_PREFIX + "/metadata/properties"),
                HttpMatchers.requestMethod("GET")));
    }


    @Test
    public void fetchContents() {
        JiveCoreContentRequestOptions contentRequestOptions = new JiveCoreContentRequestOptions();
        contentRequestOptions.setCount(25);

        HttpGet get = testObject.fetchContents(contentRequestOptions);

        assertThat(get, requestUrl("http://jiveland.com/api/core/v3/contents?count=25"));
    }

    @Test
    public void searchContents() {
        JiveCoreSearchContentRequestOptions searchContentRequestOptions = new JiveCoreSearchContentRequestOptions();
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

    @Test
    public void fetchMembersByPerson() {
        JiveCoreCountRequestOptions countRequestOptions = new JiveCoreCountRequestOptions();
        countRequestOptions.setCount(25);

        HttpGet get = testObject.fetchMembersByPerson("9999", countRequestOptions);

        assertThat(get, allOf(requestUrl("http://jiveland.com/api/core/v3/members/people/9999?count=25")));
    }

    @Test
    public void fetchContent() {
        JiveCoreContentRequestOptions options = new JiveCoreContentRequestOptions();
        options.setFields(Arrays.asList("type"));

        HttpGet get = testObject.fetchContent("/url", options);

        assertThat(get, allOf(requestUrl("http://jiveland.com/url?fields=type")));
    }

    @Test
    public void fetchReplies() {
        JiveCoreContentRequestOptions options = new JiveCoreContentRequestOptions();
        options.setStartIndex(1);

        HttpGet get = testObject.fetchReplies("/url", options);

        assertThat(get, allOf(requestUrl("http://jiveland.com/url?startIndex=1")));
    }

    @Test
    public void registerForPush() throws IOException {
        HttpPost post = testObject.registerForPush("foo", "bar");

        ArrayList<BasicNameValuePair> bodyNameValuePairs = new ArrayList<BasicNameValuePair>();
        bodyNameValuePairs.add(new BasicNameValuePair("deviceID", "bar"));
        bodyNameValuePairs.add(new BasicNameValuePair("deviceToken", "foo"));
        bodyNameValuePairs.add(new BasicNameValuePair("deviceType", "4"));
        bodyNameValuePairs.add(new BasicNameValuePair("activated", "true"));
        assertThat(post, allOf(
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
}