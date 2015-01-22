package com.jivesoftware.android.mobile.sdk.core;

import com.google.common.io.ByteSink;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.jivesoftware.android.mobile.sdk.entity.AttachmentEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentBodyEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentListEntity;
import com.jivesoftware.android.mobile.sdk.entity.matcher.ListEntityMatchers;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreContentType;
import com.jivesoftware.android.mobile.sdk.parser.HttpResponseParser;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreException;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreExceptionFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.mime.content.FileBody;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.jayway.awaitility.Awaitility.await;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.AttachmentEntityMatchers.attachmentContentType;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.AttachmentEntityMatchers.attachmentName;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.AttachmentEntityMatchers.attachmentSize;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.AttachmentEntityMatchers.attachmentUrl;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ContentBodyEntityMatchers.contentBodyText;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ContentBodyEntityMatchers.contentBodyType;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ContentEntityMatchers.contentAttachments;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ContentEntityMatchers.contentContentBody;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ContentEntityMatchers.contentSubject;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ContentEntityMatchers.contentUsers;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ContentEntityMatchers.contentVisibility;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.JiveObjectEntityMatchers.objectSelfURL;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class JiveCoreContentITest extends AbstractDelayedRestITest {

    @SuppressWarnings("RedundantTypeArguments")
    @Test
    public void testWhenDocumentContentIsCreatedThenItIsRetrieved() throws Exception {
        String content = "Hello world!";
        String subject = "testWhenDocumentContentIsCreatedThenItIsRetrieved " + UUID.randomUUID().toString();
        String contentType = "text/html";

        ContentEntity documentEntity = new ContentEntity();
        documentEntity.type = JiveCoreContentType.document;
        documentEntity.subject = subject;
        documentEntity.content = new ContentBodyEntity();
        documentEntity.content.text = content;
        documentEntity.content.type = contentType;

        ContentEntity entity = jiveCoreAdmin.createContent(documentEntity, Collections.<FileBody>emptyList()).call();

        assertThat(entity, allOf(
                contentSubject(subject),
                // InteliJ wrongly things <ContentBodyEntity> is redundant
                contentContentBody(Matchers.<ContentBodyEntity>allOf(
                        contentBodyText(containsString(content)),
                        contentBodyType(contentType)))));
    }

    @Test
    public void testFetchContents() throws Exception {
        String content = "Hello world!";
        final String uuid = UUID.randomUUID().toString();
        String subject = "testWhenDocumentContentIsCreatedThenItIsRetrieved " + uuid;
        String contentType = "text/html";

        ContentEntity documentEntity1 = new ContentEntity();
        documentEntity1.type = JiveCoreContentType.document;
        documentEntity1.subject = subject + " 1";
        documentEntity1.content = new ContentBodyEntity();
        documentEntity1.content.text = content;
        documentEntity1.content.type = contentType;

        ContentEntity documentEntity2 = new ContentEntity();
        documentEntity2.type = JiveCoreContentType.document;
        documentEntity2.subject = subject + " 2";
        documentEntity2.content = new ContentBodyEntity();
        documentEntity2.content.text = content;
        documentEntity2.content.type = contentType;

        ContentEntity contentEntity1 = jiveCoreAdmin.createContent(documentEntity1, Collections.<FileBody>emptyList()).call();
        ContentEntity contentEntity2 = jiveCoreAdmin.createContent(documentEntity2, Collections.<FileBody>emptyList()).call();

        String contentEntity1SelfUrl = contentEntity1.resources.get("self").ref;
        String contentEntity2SelfUrl = contentEntity2.resources.get("self").ref;
        await().until(new Callable<List<List<String>>>() {
                          @Override
                          public List<List<String>> call() throws Exception {
                              JiveCoreRequestOptions options = new JiveCoreRequestOptions();
                              options.setSearchTermFilter(Arrays.asList(uuid));
                              ContentListEntity contentListEntity = jiveCoreAdmin.fetchContents(options).call();

                              List<List<String>> contentEntitySelfUrlResults = new ArrayList<List<String>>();

                              List<String> contentEntitySelfUrls = new ArrayList<String>();
                              for (ContentEntity contentEntity : contentListEntity.list) {
                                  contentEntitySelfUrls.add(contentEntity.resources.get("self").ref);
                              }
                              contentEntitySelfUrlResults.add(contentEntitySelfUrls);

                              return contentEntitySelfUrlResults;
                          }
                      },
                // IntelliJ things <List<String>> is redundant, but javac disagrees.
                Matchers.<List<String>>contains(containsInAnyOrder(
                        contentEntity1SelfUrl,
                        contentEntity2SelfUrl)));
    }

    @SuppressWarnings({"unchecked", "RedundantTypeArguments"})
    @Test
    public void testFetchContentWithNextLink() throws Exception {
        String content = "Hello world!";
        final String uuid = UUID.randomUUID().toString();
        String subject = "testWhenDocumentContentIsCreatedThenItIsRetrieved " + uuid;
        String contentType = "text/html";

        ContentEntity documentEntity1 = new ContentEntity();
        documentEntity1.type = JiveCoreContentType.document;
        documentEntity1.subject = subject + " 1";
        documentEntity1.content = new ContentBodyEntity();
        documentEntity1.content.text = content;
        documentEntity1.content.type = contentType;

        ContentEntity documentEntity2 = new ContentEntity();
        documentEntity2.type = JiveCoreContentType.document;
        documentEntity2.subject = subject + " 2";
        documentEntity2.content = new ContentBodyEntity();
        documentEntity2.content.text = content;
        documentEntity2.content.type = contentType;

        ContentEntity contentEntity1 = jiveCoreAdmin.createContent(documentEntity1, Collections.<FileBody>emptyList()).call();
        ContentEntity contentEntity2 = jiveCoreAdmin.createContent(documentEntity2, Collections.<FileBody>emptyList()).call();

        String contentEntity1SelfUrl = contentEntity1.resources.get("self").ref;
        String contentEntity2SelfUrl = contentEntity2.resources.get("self").ref;

        await().until(new Callable<List<List<String>>>() {
                          @Override
                          public List<List<String>> call() throws Exception {
                              JiveCoreRequestOptions options = new JiveCoreRequestOptions();
                              options.setCount(1);
                              options.setSearchTermFilter(Arrays.asList(uuid));
                              ContentListEntity contentListEntity = jiveCoreAdmin.fetchContents(options).call();

                              List<List<String>> contentEntitySelfUrlResults = new ArrayList<List<String>>();

                              List<String> contentEntitySelfUrls = new ArrayList<String>();
                              for (ContentEntity contentEntity : contentListEntity.list) {
                                  contentEntitySelfUrls.add(contentEntity.resources.get("self").ref);
                              }
                              contentEntitySelfUrlResults.add(contentEntitySelfUrls);

                              if ((contentListEntity.links != null) && (contentListEntity.links.next != null)) {
                                  ContentListEntity nextContentListEntity = jiveCoreAdmin.fetchContents(contentListEntity.links.next).call();
                                  List<String> nextContentEntitySelfUrls = new ArrayList<String>();
                                  for (ContentEntity nextContentEntity : nextContentListEntity.list) {
                                      nextContentEntitySelfUrls.add(nextContentEntity.resources.get("self").ref);
                                  }
                                  contentEntitySelfUrlResults.add(nextContentEntitySelfUrls);
                              }

                              return contentEntitySelfUrlResults;
                          }
                      },
                // IntelliJ things <List<String>> is redundant, but javac disagrees.
                Matchers.<List<String>>containsInAnyOrder(
                        Collections.singletonList(contentEntity1SelfUrl),
                        Collections.singletonList(contentEntity2SelfUrl)));
    }

    @SuppressWarnings("RedundantTypeArguments")
    @Test
    public void testUpdateContent() throws Exception {
        String uuid = UUID.randomUUID().toString();

        ContentEntity newDocumentEntity = new ContentEntity();
        newDocumentEntity.type = JiveCoreContentType.document;
        newDocumentEntity.subject = uuid;
        newDocumentEntity.content = new ContentBodyEntity();
        newDocumentEntity.content.text = "Hello world!";
        newDocumentEntity.content.type = "text/html";

        ContentEntity updatingDocumentEntity = jiveCoreAdmin.createContent(newDocumentEntity, Collections.<FileBody>emptyList()).call();
        updatingDocumentEntity.content.text = "el barto";
        updatingDocumentEntity.visibility = "people";

        updatingDocumentEntity.users = new String[]{USER2.selfURL.toExternalForm()};

        ContentEntity updatedDocumentEntity = jiveCoreAdmin.updateContent(updatingDocumentEntity, Collections.<FileBody>emptyList()).call();

        assertThat(updatedDocumentEntity, allOf(
                contentSubject(uuid),
                // InteliJ wrongly things <ContentBodyEntity> is redundant
                contentContentBody(Matchers.<ContentBodyEntity>allOf(
                        contentBodyText(containsString("el barto")),
                        contentBodyType("text/html"))),
                contentVisibility("people"),
                contentUsers(contains(objectSelfURL(USER2.selfURL)))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateContentWithTextAttachments() throws Exception {
        String uuid = UUID.randomUUID().toString();

        ContentEntity newDocumentEntity = new ContentEntity();
        newDocumentEntity.type = JiveCoreContentType.document;
        newDocumentEntity.subject = uuid;
        newDocumentEntity.content = new ContentBodyEntity();
        newDocumentEntity.content.text = "Hello world!";
        newDocumentEntity.content.type = "text/html";

        final File attachment1File = File.createTempFile("attachment1", "txt");
        final File attachment2File = File.createTempFile("attachment2", "txt");

        attachment1File.deleteOnExit();
        attachment2File.deleteOnExit();

        ByteSink byteSink1 = new ByteSink() {
            @Override
            public OutputStream openStream() throws IOException {
                return new FileOutputStream(attachment1File);
            }
        };
        byteSink1.asCharSink(Charset.forName("UTF-8")).write("el barto");

        ByteSink byteSink2 = new ByteSink() {
            @Override
            public OutputStream openStream() throws IOException {
                return new FileOutputStream(attachment2File);
            }
        };
        byteSink2.asCharSink(Charset.forName("UTF-8")).write("Monty Burns");

        FileBody fileBody1 = new FileBody(attachment1File, "attachment1.txt", "text/plain", "UTF-8");
        FileBody fileBody2 = new FileBody(attachment2File, "attachment2.txt", "text/plain", "UTF-8");

        ContentEntity createdContentEntity = jiveCoreAdmin.createContent(newDocumentEntity, Arrays.asList(fileBody1, fileBody2)).call();

        assertThat(createdContentEntity, contentAttachments(containsInAnyOrder(
                allOf(
                        attachmentName("attachment1.txt.zip"),
                        attachmentContentType("application/zip"),
                        attachmentSize(greaterThan(0L)),
                        attachmentUrl(notNullValue(String.class))),
                allOf(
                        attachmentName("attachment2.txt.zip"),
                        attachmentContentType("application/zip"),
                        attachmentSize(greaterThan(0L)),
                        attachmentUrl(notNullValue(String.class)))
        )));

        class AttachmentDownloader {
            @Nonnull
            private final String url;
            @Nonnull
            private final String name;

            AttachmentDownloader(@Nonnull String url, @Nonnull String name) {
                this.url = url;
                this.name = name;
            }

            public String download() throws Exception {
                HttpGet httpGet = new HttpGet(url);

                JiveCoreCallable<HttpResponse> attachmentHttpResponseCallable;
                attachmentHttpResponseCallable = jiveCoreAdmin.createCallable(httpGet, new HttpResponseParserFactory<HttpResponse>() {
                    @Nonnull
                    @Override
                    public HttpResponseParser<HttpResponse> createHttpResponseParser(@Nonnull JiveCoreExceptionFactory jiveCoreExceptionFactory) {
                        return new HttpResponseParser<HttpResponse>(jiveCoreExceptionFactory) {
                            @Nullable
                            @Override
                            protected HttpResponse parseValidResponse(
                                    @Nonnull HttpResponse httpResponse,
                                    int statusCode,
                                    @Nullable HttpEntity httpEntity) throws IOException, JiveCoreException {
                                return httpResponse;
                            }
                        };
                    }
                });
                HttpResponse attachmentHttpResponse = attachmentHttpResponseCallable.call();
                HttpEntity entity = attachmentHttpResponse.getEntity();

                ZipInputStream zipInputStream = new ZipInputStream(entity.getContent());
                {
                    ZipEntry zipEntry = zipInputStream.getNextEntry();
                    assertNotNull(zipEntry);
                    assertEquals(name, zipEntry.getName());
                }

                InputStreamReader inputStreamReader = new InputStreamReader(zipInputStream, "UTF-8");

                String contents = CharStreams.toString(inputStreamReader);

                inputStreamReader.close();
                zipInputStream.close();

                // JIVE-48390
                int garbageStart = contents.indexOf('\u0000');
                String ungarbagedContents;
                if (garbageStart == -1) {
                    ungarbagedContents = contents;
                } else {
                    ungarbagedContents = contents.substring(0, garbageStart);
                }

                return ungarbagedContents;
            }
        }

        AttachmentEntity attachment1Entity = createdContentEntity.attachments.get(0);
        AttachmentEntity attachment2Entity = createdContentEntity.attachments.get(1);

        // Quick and dirty way to make this order-independent.
        String attachment1Name = attachment1Entity.name.equals("attachment1.txt.zip") ? "attachment1.txt" : "attachment2.txt";
        String attachment2Name = attachment2Entity.name.equals("attachment2.txt.zip") ? "attachment2.txt" : "attachment1.txt";

        String attachment1Contents = new AttachmentDownloader(attachment1Entity.url, attachment1Name).download();
        String attachment2Contents = new AttachmentDownloader(attachment2Entity.url, attachment2Name).download();

        List<String> actual = Arrays.asList(attachment1Contents, attachment2Contents);
        assertThat(actual, containsInAnyOrder("el barto", "Monty Burns"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateContentWithImageAttachments() throws Exception {
        // Tested images separately because of JIVE-48402

        String uuid = UUID.randomUUID().toString();

        ContentEntity newDocumentEntity = new ContentEntity();
        newDocumentEntity.type = JiveCoreContentType.document;
        newDocumentEntity.subject = uuid;
        newDocumentEntity.content = new ContentBodyEntity();
        newDocumentEntity.content.text = "Hello world!";
        newDocumentEntity.content.type = "text/html";


        FileBody fileBody1 = new FileBody(new File("test-data/el-barto.jpg"), "image/jpeg");
        FileBody fileBody2 = new FileBody(new File("test-data/mr-burns.gif"), "image/gif");

        ContentEntity createdContentEntity = jiveCoreAdmin.createContent(newDocumentEntity, Arrays.asList(fileBody1, fileBody2)).call();

        assertThat(createdContentEntity, contentAttachments(containsInAnyOrder(
                allOf(
                        attachmentName("el-barto.jpg"),
                        attachmentContentType("image/jpeg"),
                        attachmentSize(greaterThan(0L)),
                        attachmentUrl(notNullValue(String.class))),
                allOf(
                        attachmentName("mr-burns.gif"),
                        attachmentContentType("image/gif"),
                        attachmentSize(greaterThan(0L)),
                        attachmentUrl(notNullValue(String.class)))
        )));
    }

    @SuppressWarnings({"unchecked", "RedundantTypeArguments"})
    @Test
    public void testCreateAndFetchComments() throws Exception {
        String uuid = UUID.randomUUID().toString();

        ContentEntity newDocumentEntity = new ContentEntity();
        newDocumentEntity.type = JiveCoreContentType.document;
        newDocumentEntity.subject = uuid;
        newDocumentEntity.content = new ContentBodyEntity();
        newDocumentEntity.content.text = "Hello world!";
        newDocumentEntity.content.type = "text/html";

        ContentEntity createdDocumentEntity = jiveCoreAdmin.createContent(newDocumentEntity, Collections.<FileBody>emptyList()).call();

        ContentEntity newCommentEntity1 = new ContentEntity();
        newCommentEntity1.type = JiveCoreContentType.comment;
        newCommentEntity1.content = new ContentBodyEntity();
        newCommentEntity1.content.text = "Comment1";
        newCommentEntity1.content.type = "text/html";

        jiveCoreAdmin.createContent(createdDocumentEntity.resources.get("comments").ref, newCommentEntity1, Collections.<FileBody>emptyList()).call();

        ContentEntity newCommentEntity2 = new ContentEntity();
        newCommentEntity2.type = JiveCoreContentType.comment;
        newCommentEntity2.content = new ContentBodyEntity();
        newCommentEntity2.content.text = "Comment2";
        newCommentEntity2.content.type = "text/html";

        jiveCoreAdmin.createContent(createdDocumentEntity.resources.get("comments").ref, newCommentEntity2, Collections.<FileBody>emptyList()).call();

        ContentListEntity repliesContentListEntity = jiveCoreAdmin.fetchReplies(createdDocumentEntity.resources.get("comments").ref, new JiveCoreRequestOptions()).call();

        assertThat(repliesContentListEntity, ListEntityMatchers.<ContentEntity, ContentListEntity>listEntities(
                Matchers.<ContentEntity>contains(
                        contentContentBody(Matchers.<ContentBodyEntity>allOf(
                                contentBodyText(containsString("Comment1")),
                                contentBodyType("text/html"))),
                        contentContentBody(Matchers.<ContentBodyEntity>allOf(
                                contentBodyText(containsString("Comment2")),
                                contentBodyType("text/html"))))));
    }
}
