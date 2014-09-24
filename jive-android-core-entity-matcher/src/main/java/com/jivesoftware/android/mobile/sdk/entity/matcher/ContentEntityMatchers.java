package com.jivesoftware.android.mobile.sdk.entity.matcher;

import com.jivesoftware.android.mobile.matcher.ArrayPropertyMatcher;
import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.AttachmentEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentBodyEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentEntity;
import com.jivesoftware.android.mobile.sdk.entity.EntityUtils;
import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;

@ParametersAreNonnullByDefault
public class ContentEntityMatchers {
    @Nonnull
    public static Matcher<ContentEntity> contentSubject(@Nullable String subject) {
        return new PropertyMatcher<String, ContentEntity>("subject", subject) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull ContentEntity item) throws Exception {
                return item.subject;
            }
        };
    }

    @Nonnull
    public static Matcher<ContentEntity> contentContentBody(Matcher<ContentBodyEntity> contentBodyMatcher) {
        return new PropertyMatcher<ContentBodyEntity, ContentEntity>(contentBodyMatcher, "contentBody") {
            @Nullable
            @Override
            protected ContentBodyEntity getPropertyValue(@Nonnull ContentEntity item) throws Exception {
                return item.content;
            }
        };
    }

    @Nonnull
    public static Matcher<ContentEntity> contentVisibility(String visibility) {
        return new PropertyMatcher<String, ContentEntity>("visibility", visibility) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull ContentEntity item) throws Exception {
                return item.visibility;
            }
        };
    }

    @Nonnull
    public static Matcher<ContentEntity> contentUsers(Matcher<? super Iterable<? extends PersonEntity>> usersMatcher) {
        return new ArrayPropertyMatcher<PersonEntity, ContentEntity>(usersMatcher, "users") {
            @Nullable
            @Override
            protected PersonEntity[] getArrayPropertyValue(@Nonnull ContentEntity item) throws Exception {
                if (item.users instanceof PersonEntity[]) {
                    return (PersonEntity[]) item.users;
                } else if (item.users instanceof String[]) {
                    String[] personEntitySelfUrls = (String[]) item.users;
                    PersonEntity[] personEntities = new PersonEntity[item.users.length];
                    for (int i=0; i < personEntitySelfUrls.length; i++) {
                        PersonEntity personEntity = new PersonEntity();
                        personEntity.resources = EntityUtils.createSelfResources(personEntitySelfUrls[i]);
                        personEntity.type = "person";
                        personEntities[i] = personEntity;
                    }
                    return personEntities;
                } else {
                    return null;
                }
            }
        };
    }

    @Nonnull
    public static Matcher<ContentEntity> contentAttachments(Matcher<? super Iterable<? extends AttachmentEntity>> attachmentsMatcher) {
        return new PropertyMatcher<List<AttachmentEntity>, ContentEntity>(attachmentsMatcher, "attachments") {
            @Nullable
            @Override
            protected List<AttachmentEntity> getPropertyValue(@Nonnull ContentEntity item) throws Exception {
                return item.attachments;
            }
        };
    }

    @Nonnull
    public static Matcher<ContentEntity> contentQuestion(Boolean question) {
        return new PropertyMatcher<Boolean, ContentEntity>("question", question) {
            @Nullable
            @Override
            protected Boolean getPropertyValue(@Nonnull ContentEntity item) throws Exception {
                return item.question;
            }
        };
    }

    @Nonnull
    public static Matcher<ContentEntity> contentParent(@Nullable String parent) {
        return new PropertyMatcher<String, ContentEntity>("parent", parent) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull ContentEntity item) throws Exception {
                return item.parent;
            }
        };
    }

    @Nonnull
    public static Matcher<ContentEntity> contentLikeCount(int expected) {
        return new PropertyMatcher<Integer, ContentEntity>("likeCount", expected) {
            @Nullable
            @Override
            protected Integer getPropertyValue(@Nonnull ContentEntity item) throws Exception {
                return item.likeCount;
            }
        };
    }

    @Nonnull
    public static Matcher<ContentEntity> contentUsers(String... personSelfUrls) {
        return new PropertyMatcher<List<String>, ContentEntity>("users", Arrays.asList(personSelfUrls)) {
            @Nullable
            @Override
            protected List<String> getPropertyValue(@Nonnull ContentEntity item) throws Exception {
                Object[] users = item.users;
                if (users instanceof String[]) {
                    String[] personSelfUrls = (String[]) users;
                    return Arrays.asList(personSelfUrls);
                } else if (users instanceof PersonEntity[]) {
                    PersonEntity[] personEntities = (PersonEntity[]) users;
                    String[] personSelfUrls = new String[personEntities.length];
                    for (int i=0, length = personEntities.length; i < length; i++) {
                        PersonEntity personEntity = personEntities[i];
                        String personSelfUrl = EntityUtils.getSelfResourceRef(personEntity);
                        personSelfUrls[i] = personSelfUrl;
                    }
                    return Arrays.asList(personSelfUrls);
                } else if (users == null) {
                    return null;
                } else {
                    throw new IllegalStateException("Unexpected users: " + users);
                }
            }
        };
    }
}
