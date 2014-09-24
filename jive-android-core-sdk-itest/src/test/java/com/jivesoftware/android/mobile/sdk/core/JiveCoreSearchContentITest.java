package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreSearchContentRequestOptions;
import com.jivesoftware.android.mobile.sdk.entity.ContentBodyEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentListEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.Callable;

import static com.jayway.awaitility.Awaitility.await;

public class JiveCoreSearchContentITest extends AbstractDelayedRestITest {

    @Test
    public void testSearchForNewlyCreatedDocument() throws Exception {
        final String uuid = UUID.randomUUID().toString();
        ContentEntity newDocumentEntity = createNewDocumentEntity(uuid);

        JiveCoreCallable<ContentEntity> contentCallable = jiveCoreAdmin.createContent(newDocumentEntity, Collections.<FileBody>emptyList());
        ContentEntity contentEntity = contentCallable.call();
        final String contentEntitySelfUrl = contentEntity.resources.get("self").ref;

        await().until(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                JiveCoreSearchContentRequestOptions searchContentRequestOptions = new JiveCoreSearchContentRequestOptions();
                searchContentRequestOptions.setSearchTerms(Arrays.asList(uuid));
                JiveCoreCallable<ContentListEntity> searchContentCallable = jiveCoreUser2.searchContents(searchContentRequestOptions);
                ContentListEntity contentListEntity = searchContentCallable.call();
                for (ContentEntity contentEntity : contentListEntity.list) {
                    if (contentEntity.resources.get("self").ref.equals(contentEntitySelfUrl)) {
                        return true;
                    }
                }

                return false;
            }
        });
    }

    @Test
    public void testSearchForTwoNewlyCreatedDocumentsByPaging() throws Exception {
        final String uuid = UUID.randomUUID().toString();
        ContentEntity newDocumentEntity1 = createNewDocumentEntity(uuid + " 1");
        ContentEntity newDocumentEntity2 = createNewDocumentEntity(uuid + " 2");

        JiveCoreCallable<ContentEntity> createContent1Callable = jiveCoreAdmin.createContent(newDocumentEntity1, Collections.<FileBody>emptyList());
        ContentEntity contentEntity1 = createContent1Callable.call();

        JiveCoreCallable<ContentEntity> createContent2Callable = jiveCoreAdmin.createContent(newDocumentEntity2, Collections.<FileBody>emptyList());
        ContentEntity contentEntity2 = createContent2Callable.call();

        final String contentEntity1SelfUrl = contentEntity1.resources.get("self").ref;
        final String contentEntity2SelfUrl = contentEntity2.resources.get("self").ref;

        await().until(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                JiveCoreSearchContentRequestOptions searchContentRequestOptions = new JiveCoreSearchContentRequestOptions();
                searchContentRequestOptions.setCount(1);
                searchContentRequestOptions.setSearchTerms(Arrays.asList(uuid));
                JiveCoreCallable<ContentListEntity> searchContent1Callable = jiveCoreUser2.searchContents(searchContentRequestOptions);
                ContentListEntity contentListEntity = searchContent1Callable.call();
                for (ContentEntity contentEntity : contentListEntity.list) {
                    String contentEntitySelfUrl = contentEntity.resources.get("self").ref;
                    String nextContentEntitySelfUrl;
                    if (contentEntitySelfUrl.equals(contentEntity1SelfUrl)) {
                        nextContentEntitySelfUrl = contentEntity2SelfUrl;
                    } else if ( contentEntitySelfUrl.equals(contentEntity2SelfUrl)) {
                        nextContentEntitySelfUrl = contentEntity1SelfUrl;
                    } else {
                        nextContentEntitySelfUrl = null;
                    }
                    if (nextContentEntitySelfUrl != null) {
                        JiveCoreCallable<ContentListEntity> searchNextContentCallable = jiveCoreUser2.searchContents(contentListEntity.links.next);
                        ContentListEntity nextContentListEntity = searchNextContentCallable.call();
                        for (ContentEntity nextContentEntity : nextContentListEntity.list) {
                            if (nextContentEntity.resources.get("self").ref.equals(nextContentEntitySelfUrl)) {
                                return true;
                            }
                        }
                    }
                }

                return false;
            }
        });
    }

    private static ContentEntity createNewDocumentEntity(String uuid) {
        String content = "Hello world!";
        String subject = "testWhenDocumentContentIsCreatedThenItIsRetrieved " + uuid;
        String contentType = "text/html";

        ContentEntity newDocumentEntity = new ContentEntity();
        newDocumentEntity.type = "document";
        newDocumentEntity.subject = subject;
        newDocumentEntity.content = new ContentBodyEntity();
        newDocumentEntity.content.text = content;
        newDocumentEntity.content.type = contentType;

        return newDocumentEntity;
    }
}
