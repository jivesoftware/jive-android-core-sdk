package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.ActivityEntity;
import com.jivesoftware.android.mobile.sdk.entity.ActivityListEntity;
import com.jivesoftware.android.mobile.sdk.entity.ActivityObjectEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentBodyEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentEntity;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreVerb;
import org.apache.http.entity.mime.content.FileBody;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import static com.jayway.awaitility.Awaitility.await;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ActivityEntityMatchers.activityObjectUrl;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ActivityEntityMatchers.activityRead;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ActivityEntityMatchers.activityVerb;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.AllOf.allOf;

public class JiveCoreInboxITest extends AbstractDelayedRestITest {

    @Test
    public void markInboxEntryAsUnread() throws Exception {
        String content = "Hello world!";
        String subject = "markInboxEntryAsRead " + UUID.randomUUID().toString();
        String contentType = "text/html";

        ContentEntity documentEntity = new ContentEntity();
        documentEntity.type = "document";
        documentEntity.subject = subject;
        documentEntity.content = new ContentBodyEntity();
        documentEntity.content.text = content;
        documentEntity.content.type = contentType;

        JiveCoreCallable<ContentEntity> contentCallable = jiveCoreAdmin.createContent(documentEntity, Collections.<FileBody>emptyList());
        ContentEntity contentEntity = contentCallable.call();
        final String contentEntitySelfUrl = contentEntity.resources.get("self").ref;

        ActivityEntity inboxActivityEntity = await().until(
                fetchInboxActivityEntity(jiveCoreAdmin, contentEntitySelfUrl),
                activityRead(true));

        jiveCoreAdmin.markInboxEntryAsUnread(inboxActivityEntity.jiveExtension.update).call();

        await().until(
                fetchInboxActivityEntity(jiveCoreAdmin, contentEntitySelfUrl),
                activityRead(false));
    }

    @Test
    public void markInboxEntryAsRead() throws Exception {
        String content = "Hello world!";
        String subject = "markInboxEntryAsRead " + UUID.randomUUID().toString();
        String contentType = "text/html";

        ContentEntity documentEntity = new ContentEntity();
        documentEntity.type = "document";
        documentEntity.subject = subject;
        documentEntity.content = new ContentBodyEntity();
        documentEntity.content.text = content;
        documentEntity.content.type = contentType;
        documentEntity.users = new String[]{ADMIN.selfPathAndQuery};
        documentEntity.visibility = "people";

        JiveCoreCallable<ContentEntity> contentCallable = jiveCoreUser2.createContent(documentEntity, Collections.<FileBody>emptyList());
        ContentEntity contentEntity = contentCallable.call();
        final String contentEntitySelfUrl = contentEntity.resources.get("self").ref;

        ActivityEntity inboxActivityEntity = await().until(
                fetchInboxActivityEntity(jiveCoreAdmin, contentEntitySelfUrl),
                activityRead(false));

        jiveCoreAdmin.markInboxEntryAsRead(inboxActivityEntity.jiveExtension.update).call();

        await().until(
                fetchInboxActivityEntity(jiveCoreAdmin, contentEntitySelfUrl),
                activityRead(true));
    }

    @Test
    public void fetchInboxOnlyUnreadAndOneCount() throws Exception {
        String content = "Hello world!";
        String subject = "markInboxEntryAsRead " + UUID.randomUUID().toString();
        String contentType = "text/html";

        ContentEntity documentEntity1 = new ContentEntity();
        documentEntity1.type = "document";
        documentEntity1.subject = subject + " 1";
        documentEntity1.content = new ContentBodyEntity();
        documentEntity1.content.text = content;
        documentEntity1.content.type = contentType;
        documentEntity1.users = new String[]{ADMIN.selfPathAndQuery};
        documentEntity1.visibility = "people";

        ContentEntity contentEntity1 = jiveCoreUser2.createContent(documentEntity1, Collections.<FileBody>emptyList()).call();
        String contentEntity1SelfUrl = contentEntity1.resources.get("self").ref;

        ContentEntity documentEntity2 = new ContentEntity();
        documentEntity2.type = "document";
        documentEntity2.subject = subject + " 2";
        documentEntity2.content = new ContentBodyEntity();
        documentEntity2.content.text = content;
        documentEntity2.content.type = contentType;
        documentEntity2.users = new String[]{ADMIN.selfPathAndQuery};
        documentEntity2.visibility = "people";

        ContentEntity contentEntity2 = jiveCoreUser2.createContent(documentEntity2, Collections.<FileBody>emptyList()).call();
        String contentEntity2SelfUrl = contentEntity2.resources.get("self").ref;

        await().until(new Callable<List<List<ActivityEntity>>>() {
                          @Override
                          public List<List<ActivityEntity>> call() throws Exception {
                              JiveCoreRequestOptions options = new JiveCoreRequestOptions();
                              options.setCount(1);
                              options.setUnreadFilter(true);
                              // don't try collapsing because the collapse option doesn't propagate: JIVE-48153

                              ActivityListEntity unreadInboxActivityListEntity1 = jiveCoreAdmin.fetchInbox(options).call();
                              ActivityListEntity unreadInboxActivityListEntity2 = jiveCoreAdmin.fetchActivities(unreadInboxActivityListEntity1.links.next).call();

                              return Arrays.asList(unreadInboxActivityListEntity1.list, unreadInboxActivityListEntity2.list);
                          }
                      },
                containsInAnyOrder(
                        contains(
                                allOf(
                                        activityObjectUrl(contentEntity1SelfUrl),
                                        activityRead(false),
                                        activityVerb(JiveCoreVerb.notification)),
                                allOf(
                                        activityObjectUrl(contentEntity1SelfUrl),
                                        activityRead(false),
                                        activityVerb(JiveCoreVerb.created))),
                        contains(
                                allOf(
                                        activityObjectUrl(contentEntity2SelfUrl),
                                        activityRead(false),
                                        activityVerb(JiveCoreVerb.notification)),
                                allOf(
                                        activityObjectUrl(contentEntity2SelfUrl),
                                        activityRead(false),
                                        activityVerb(JiveCoreVerb.created)))));
    }

    private Callable<ActivityEntity> fetchInboxActivityEntity(@Nonnull final JiveCore jiveCore, @Nonnull final String objectSelfUrl) {
        return new Callable<ActivityEntity>() {
            @Override
            public ActivityEntity call() throws IOException {
                JiveCoreCallable<ActivityListEntity> fetchInboxCallable = jiveCore.fetchInbox(new JiveCoreRequestOptions());
                ActivityListEntity inboxActivityListEntity = fetchInboxCallable.call();
                for (ActivityEntity inboxActivityEntity : inboxActivityListEntity.list) {
                    ActivityObjectEntity inboxActivityObjectEntity = inboxActivityEntity.object;
                    if (inboxActivityObjectEntity != null) {
                        String inboxActivityObjectId = inboxActivityObjectEntity.id;
                        if (objectSelfUrl.equals(inboxActivityObjectId)) {
                            return inboxActivityEntity;
                        }
                    }
                }
                return null;
            }
        };
    }
}
