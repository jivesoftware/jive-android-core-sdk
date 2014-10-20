package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.ContentBodyEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentEntity;
import com.jivesoftware.android.mobile.sdk.entity.StreamEntity;
import com.jivesoftware.android.mobile.sdk.entity.StreamListEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import static com.jivesoftware.android.mobile.sdk.entity.matcher.ListEntityMatchers.listEntities;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.StreamEntityMatchers.streamName;
import static org.junit.Assert.assertThat;

public class JiveCoreFollowITest extends AbstractITest {

    @Test
    public void testWhenDocumentCreatedThenUnfollowWorksCorrectly() throws Exception {
        String content = "Hello world!";
        String subject = "testWhenDocumentCreatedThenUnfollowWorksCorrectly " + UUID.randomUUID().toString();
        String contentType = "text/html";

        ContentEntity documentEntity = new ContentEntity();
        documentEntity.type = "document";
        documentEntity.subject = subject;
        documentEntity.content = new ContentBodyEntity();
        documentEntity.content.text = content ;
        documentEntity.content.type = contentType;

        ContentEntity contentEntity = jiveCoreAdmin.createContent(documentEntity, Collections.<FileBody>emptyList()).call();

        StreamListEntity newActivityListEntity = jiveCoreAdmin.updateFollowingInEntitiesForObject(contentEntity, Collections.<StreamEntity>emptyList()).call();

        assertThat(newActivityListEntity, listEntities(Matchers.<StreamEntity>emptyIterable()));
    }

    @Test
    public void testWhenDocumentCreatedThenFollowWorksCorrectly() throws Exception {
        String content = "Hello world!";
        String subject = "testWhenDocumentCreatedThenFollowWorksCorrectly " + UUID.randomUUID().toString();
        String contentType = "text/html";

        ContentEntity documentEntity = new ContentEntity();
        documentEntity.type = "document";
        documentEntity.subject = subject;
        documentEntity.content = new ContentBodyEntity();
        documentEntity.content.text = content ;
        documentEntity.content.type = contentType;

        ContentEntity contentEntity = jiveCoreAdmin.createContent(documentEntity, Collections.<FileBody>emptyList()).call();

        StreamListEntity initialStreamListEntity = jiveCoreAdmin.fetchStreams(contentEntity.resources.get("followingIn").ref).call();

        jiveCoreAdmin.updateFollowingInEntitiesForObject(contentEntity, Collections.<StreamEntity>emptyList());
        StreamListEntity updatedStreamListEntity = jiveCoreAdmin.updateFollowingInEntitiesForObject(contentEntity, initialStreamListEntity.list).call();

        assertThat(updatedStreamListEntity, listEntities(Matchers.<StreamEntity>contains(streamName(initialStreamListEntity.list.get(0).name))));
    }
}
