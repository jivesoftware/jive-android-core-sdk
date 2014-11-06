package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import com.jivesoftware.android.mobile.sdk.entity.ResourceEntity;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreDirective;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreDirectiveValue;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreEntityDescriptor;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreEntryState;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCorePlaceType;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreRelationship;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreRelationshipValue;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreSort;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreValueFactory;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreVerb;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreVerbValue;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class JiveCoreRequestOptionsTest {

    private JiveCoreRequestOptions testObject;

    @Before
    public void setUp() throws Exception {
        testObject = new JiveCoreRequestOptions();
    }

    @Test
    public void overlay_requestOptions() {
        JiveCoreRequestOptions other = new JiveCoreRequestOptions();
        other.setEntityDescriptorFilter(3, 4L); // should overwrite
        other.setAbridged(true); // should be added

        testObject.setEntityDescriptorFilter(1, 2L); // gets overwritten
        testObject.setActiveOnly(true); // is retained
        testObject.overlay(other);

        String actual = queryParametersAsString();
        assertEquals("activeOnly=true;,abridged=true;,filter=entityDescriptor(3,4);", actual);
    }

    @Test
    public void overlay_immutableRequestOptions() {
        JiveCoreRequestOptions other = new JiveCoreRequestOptions();
        other.setEntityDescriptorFilter(3, 4L); // should overwrite
        other.setAbridged(true); // should be added

        testObject.setEntityDescriptorFilter(1, 2L); // gets overwritten
        testObject.setActiveOnly(true); // is retained
        testObject.overlay(other.createImmutableCopy());

        String actual = queryParametersAsString();
        assertEquals("activeOnly=true;,abridged=true;,filter=entityDescriptor(3,4);", actual);
    }

    @Test
    public void createImmutableCopy() {
        testObject.setEntityDescriptorFilter(1, 2L);
        testObject.setActiveOnly(true);
        JiveCoreImmutableRequestOptions copy = testObject.createImmutableCopy();
        testObject.setEntityDescriptorFilter(3, 4L);
        testObject.setActiveOnly(false);

        String actual = queryParametersAsString(copy.values);
        assertEquals("activeOnly=true;,filter=entityDescriptor(1,2);", actual);
    }

    @Test
    public void setEntityDescriptorFilter() throws Exception {
        testObject.setEntityDescriptorFilter(1, 2L);
        String actual = queryParametersAsString();
        assertEquals("filter=entityDescriptor(1,2);", actual);
    }

    @Test
    public void setEntityDescriptorFilter_object() throws Exception {
        testObject.setEntityDescriptorFilter(new JiveCoreEntityDescriptor(1, 2L));
        String actual = queryParametersAsString();
        assertEquals("filter=entityDescriptor(1,2);", actual);
    }

    @Test
    public void setEntityDescriptorFilter_multiple() throws Exception {
        testObject.setEntityDescriptorFilter(new JiveCoreEntityDescriptor(1, 2L), new JiveCoreEntityDescriptor(3, 4L));
        String actual = queryParametersAsString();
        assertEquals("filter=entityDescriptor(1,2,3,4);", actual);
    }

    @Test
    public void setRelationshipFilter() throws Exception {
        testObject.setRelationshipFilter(JiveCoreRelationship.following);
        String actual = queryParametersAsString();
        assertEquals("filter=relationship(following);", actual);
    }

    @Test
    public void setRelationshipFilter_escaped() throws Exception {
        testObject.setRelationshipFilter(new JiveCoreRelationshipValue() {
            @Override
            public String toString() {
                return "relationship,()\\";
            }
        });
        String actual = queryParametersAsString();
        assertEquals("filter=relationship(relationship\\,\\(\\)\\\\);", actual);
    }

    @Test
    public void setSearchTermFilter() throws Exception {
        testObject.setSearchTermFilter(Arrays.asList("foo"));
        String actual = queryParametersAsString();
        assertEquals("filter=search(foo);", actual);
    }

    @Test
    public void setSearchTermFilter_escaped() throws Exception {
        testObject.setSearchTermFilter(Arrays.asList("foo,()\\bar"));
        String actual = queryParametersAsString();
        assertEquals("filter=search(foo\\,\\(\\)\\\\bar);", actual);
    }

    @Test
    public void setSearchTermFilter_multiple() throws Exception {
        testObject.setSearchTermFilter(Arrays.asList("foo", "bar"));
        String actual = queryParametersAsString();
        assertEquals("filter=search(foo,bar);", actual);
    }

    @Test
    public void setTagFilter() throws Exception {
        testObject.setTagFilter(Arrays.asList("foo"));
        String actual = queryParametersAsString();
        assertEquals("filter=tag(foo);", actual);
    }

    @Test
    public void setTagFilter_multiple() throws Exception {
        testObject.setTagFilter(Arrays.asList("foo", "bar"));
        String actual = queryParametersAsString();
        assertEquals("filter=tag(foo,bar);", actual);
    }

    @Test
    public void setTagFilter_escaped() throws Exception {
        testObject.setTagFilter(Arrays.asList("foo,()\\"));
        String actual = queryParametersAsString();
        assertEquals("filter=tag(foo\\,\\(\\)\\\\);", actual);
    }

    @Test
    public void setTypeFilter() throws Exception {
        testObject.setTypeFilter(Arrays.asList(JiveCorePlaceType.blog));
        String actual = queryParametersAsString();
        assertEquals("filter=type(blog);", actual);
    }

    @Test
    public void setTypeFilter_multiple() throws Exception {
        testObject.setTypeFilter(Arrays.asList(JiveCorePlaceType.blog, JiveCorePlaceType.space));
        String actual = queryParametersAsString();
        assertEquals("filter=type(blog,space);", actual);
    }

    @Test
    public void setTypeFilter_escaped() throws Exception {
        testObject.setTypeFilter(Arrays.asList(JiveCoreValueFactory.createPlaceTypeValue("foo,()\\")));
        String actual = queryParametersAsString();
        assertEquals("filter=type(foo\\,\\(\\)\\\\);", actual);
    }

    @Test
    public void setVerbFilter() throws Exception {
        testObject.setVerbFilter(Arrays.asList(JiveCoreVerb.created));
        String actual = queryParametersAsString();
        assertEquals("filter=verb(jive:created);", actual);
    }

    @Test
    public void setVerbFilter_multiple() throws Exception {
        testObject.setVerbFilter(Arrays.asList(JiveCoreVerb.created, JiveCoreVerb.promoted));
        String actual = queryParametersAsString();
        assertEquals("filter=verb(jive:created,jive:promoted);", actual);
    }

    @Test
    public void setVerbFilter_escaped() throws Exception {
        testObject.setVerbFilter(Arrays.asList(new JiveCoreVerbValue() {
            @Override
            public String toString() {
                return "foo,()\\";
            }
        }));
        String actual = queryParametersAsString();
        assertEquals("filter=verb(foo\\,\\(\\)\\\\);", actual);
    }

    @Test
    public void setAuthorFilter() throws Exception {
        testObject.setAuthorFilter(Arrays.asList("selfUrl1"));
        String actual = queryParametersAsString();
        assertEquals("filter=author(selfUrl1);", actual);
    }

    @Test
    public void setAuthorFilter_multiple() throws Exception {
        testObject.setAuthorFilter(Arrays.asList("selfUrl1", "selfUrl2"));
        String actual = queryParametersAsString();
        assertEquals("filter=author(selfUrl1,selfUrl2);", actual);
    }

    @Test
    public void setPlaceFilter() throws Exception {
        testObject.setPlaceFilter(Arrays.asList("url1"));
        String actual = queryParametersAsString();
        assertEquals("filter=place(url1);", actual);
    }

    @Test
    public void setPlaceFilter_multiple() throws Exception {
        testObject.setPlaceFilter(Arrays.asList("url1", "url2"));
        String actual = queryParametersAsString();
        assertEquals("filter=place(url1,url2);", actual);
    }

    @Test
    public void setEntryStateFilter() throws Exception {
        testObject.setEntryStateFilter(Arrays.asList(JiveCoreEntryState.awaiting_action));
        String actual = queryParametersAsString();
        assertEquals("filter=entryState(awaiting_action);", actual);
    }

    @Test
    public void setEntryStateFilter_multiple() throws Exception {
        testObject.setEntryStateFilter(Arrays.asList(JiveCoreEntryState.awaiting_action, JiveCoreEntryState.hidden));
        String actual = queryParametersAsString();
        assertEquals("filter=entryState(awaiting_action,hidden);", actual);
    }

    @Test
    public void setUnreadFilter() throws Exception {
        testObject.setUnreadFilter(true);
        String actual = queryParametersAsString();
        assertEquals("filter=unread;", actual);
    }

    @Test
    public void setUnreadFilter_false() throws Exception {
        testObject.setUnreadFilter(true);
        testObject.setUnreadFilter(false);
        String actual = queryParametersAsString();
        assertEquals("", actual);
    }

    @Test
    public void setNotificationsFilter() throws Exception {
        testObject.setNotificationsFilter(true);
        String actual = queryParametersAsString();
        assertEquals("filter=notifications;", actual);
    }

    @Test
    public void setNotificationsFilter_false() throws Exception {
        testObject.setNotificationsFilter(true);
        testObject.setNotificationsFilter(false);
        String actual = queryParametersAsString();
        assertEquals("", actual);
    }

    @Test
    public void setDirectives() throws Exception {
        testObject.setDirectives(Arrays.<JiveCoreDirectiveValue>asList(JiveCoreDirective.collapse));
        String actual = queryParametersAsString();
        assertEquals("directive=collapse;", actual);
    }

    @Test
    public void setDirectives_multiple() throws Exception {
        testObject.setDirectives(Arrays.<JiveCoreDirectiveValue>asList(JiveCoreDirective.collapse, JiveCoreDirective.include_rtc));
        String actual = queryParametersAsString();
        assertEquals("directive=collapse,include_rtc;", actual);
    }

    @Test
    public void setCollapseSkipCollectionIdsDirective() throws Exception {
        testObject.setCollapseSkipCollectionIdsDirective(Arrays.asList("one", "two"));
        String actual = queryParametersAsString();
        assertEquals("directive=collapseSkip(one,two);", actual);
    }

    @Test
    public void setCollapseSkipCollectionIdsDirective_merged() throws Exception {
        testObject.setDirectives(Arrays.<JiveCoreDirectiveValue>asList(JiveCoreDirective.collapse));
        testObject.setCollapseSkipCollectionIdsDirective(Arrays.asList("one", "two"));
        String actual = queryParametersAsString();
        assertEquals("directive=collapse,collapseSkip(one,two);", actual);
    }

    @Test
    public void setSort() throws Exception {
        testObject.setSort(JiveCoreSort.relevanceDesc);
        String actual = queryParametersAsString();
        assertEquals("sort=relevanceDesc;", actual);
    }

    @Test
    public void setPublished() throws Exception {
        Date date = createTestDate();
        testObject.setPublished(date);
        String actual = queryParametersAsString();
        assertEquals("published=2014-10-13T00:00:00.000+0000;", actual);
    }

    @Test
    public void setUpdated() throws Exception {
        Date date = createTestDate();
        testObject.setUpdated(date);
        String actual = queryParametersAsString();
        assertEquals("updated=2014-10-13T00:00:00.000+0000;", actual);
    }

    @Test
    public void setBefore() throws Exception {
        Date date = createTestDate();
        testObject.setBefore(date);
        String actual = queryParametersAsString();
        assertEquals("before=2014-10-13T00:00:00.000+0000;", actual);
    }

    @Test
    public void setBefore_clearsAfter() throws Exception {
        Date date = createTestDate();
        testObject.setAfter(date);
        testObject.setBefore(date);
        String actual = queryParametersAsString();
        assertEquals("before=2014-10-13T00:00:00.000+0000;", actual);
    }

    @Test
    public void setAfter() throws Exception {
        Date date = createTestDate();
        testObject.setAfter(date);
        String actual = queryParametersAsString();
        assertEquals("after=2014-10-13T00:00:00.000+0000;", actual);
    }

    @Test
    public void setAfter_clearsBefore() throws Exception {
        Date date = createTestDate();
        testObject.setBefore(date);
        testObject.setAfter(date);
        String actual = queryParametersAsString();
        assertEquals("after=2014-10-13T00:00:00.000+0000;", actual);
    }

    @Test
    public void setAuthors() throws Exception {
        PersonEntity personEntity = createPersonEntity("person1");
        testObject.setAuthors(Arrays.asList(personEntity));
        String actual = queryParametersAsString();
        assertEquals("authors=person1;", actual);
    }

    @Test
    public void setAuthors_multiple() throws Exception {
        PersonEntity personEntity1 = createPersonEntity("person1");
        PersonEntity personEntity2 = createPersonEntity("person2");
        testObject.setAuthors(Arrays.asList(personEntity1, personEntity2));
        String actual = queryParametersAsString();
        assertEquals("authors=person1,person2;", actual);
    }

    @Test
    public void setFields() throws Exception {
        testObject.setFields(Arrays.asList("one"));
        String actual = queryParametersAsString();
        assertEquals("fields=one;", actual);
    }

    @Test
    public void setFields_multiple() throws Exception {
        testObject.setFields(Arrays.asList("one", "two", "three"));
        String actual = queryParametersAsString();
        assertEquals("fields=one,two,three;", actual);
    }

    @Test
    public void setFields_escaped() throws Exception {
        testObject.setFields(Arrays.asList("one,()\\"));
        String actual = queryParametersAsString();
        assertEquals("fields=one\\,\\(\\)\\\\;", actual);
    }

    @Test
    public void setAnchor() throws Exception {
        testObject.setAnchor("one");
        String actual = queryParametersAsString();
        assertEquals("anchor=one;", actual);
    }

    @Test
    public void setAutoCategorize() throws Exception {
        testObject.setAutoCategorize(true);
        String actual = queryParametersAsString();
        assertEquals("autoCategorize=true;", actual);
    }

    @Test
    public void setAutoCategorize_false() throws Exception {
        testObject.setAutoCategorize(false);
        String actual = queryParametersAsString();
        assertEquals("autoCategorize=false;", actual);
    }

    @Test
    public void setCollapsed() throws Exception {
        testObject.setCollapsed(true);
        String actual = queryParametersAsString();
        assertEquals("collapse=true;", actual);
    }

    @Test
    public void setCollapsed_false() throws Exception {
        testObject.setCollapsed(false);
        String actual = queryParametersAsString();
        assertEquals("collapse=false;", actual);
    }

    @Test
    public void setOldestUnread() throws Exception {
        testObject.setOldestUnread(true);
        String actual = queryParametersAsString();
        assertEquals("oldestUnread=true;", actual);
    }

    @Test
    public void setOldestUnread_false() throws Exception {
        testObject.setOldestUnread(false);
        String actual = queryParametersAsString();
        assertEquals("oldestUnread=false;", actual);
    }

    @Test
    public void setAbridged() throws Exception {
        testObject.setAbridged(true);
        String actual = queryParametersAsString();
        assertEquals("abridged=true;", actual);
    }

    @Test
    public void setAbridged_false() throws Exception {
        testObject.setAbridged(false);
        String actual = queryParametersAsString();
        assertEquals("abridged=false;", actual);
    }

    @Test
    public void setActiveOnly() throws Exception {
        testObject.setActiveOnly(true);
        String actual = queryParametersAsString();
        assertEquals("activeOnly=true;", actual);
    }

    @Test
    public void setActiveOnly_false() throws Exception {
        testObject.setActiveOnly(false);
        String actual = queryParametersAsString();
        assertEquals("activeOnly=false;", actual);
    }

    @Test
    public void setExclude() throws Exception {
        testObject.setExclude(true);
        String actual = queryParametersAsString();
        assertEquals("exclude=true;", actual);
    }

    @Test
    public void setExclude_false() throws Exception {
        testObject.setExclude(true);
        testObject.setExclude(false);
        String actual = queryParametersAsString();
        assertEquals("exclude=false;", actual);
    }

    @Test
    public void setExcludeReplies() throws Exception {
        testObject.setExcludeReplies(true);
        String actual = queryParametersAsString();
        assertEquals("excludeReplies=true;", actual);
    }

    @Test
    public void setExcludeReplies_false() throws Exception {
        testObject.setExcludeReplies(false);
        String actual = queryParametersAsString();
        assertEquals("excludeReplies=false;", actual);
    }

    @Test
    public void setMinor() throws Exception {
        testObject.setMinor(true);
        String actual = queryParametersAsString();
        assertEquals("minor=true;", actual);
    }

    @Test
    public void setMinor_false() throws Exception {
        testObject.setMinor(false);
        String actual = queryParametersAsString();
        assertEquals("minor=false;", actual);
    }

    @Test
    public void setAuthorComment() throws Exception {
        testObject.setAuthorComment(true);
        String actual = queryParametersAsString();
        assertEquals("author=true;", actual);
    }

    @Test
    public void setAuthorComment_false() throws Exception {
        testObject.setAuthorComment(false);
        String actual = queryParametersAsString();
        assertEquals("author=false;", actual);
    }

    @Test
    public void setIsCreate() throws Exception {
        testObject.setIsCreate(true);
        String actual = queryParametersAsString();
        assertEquals("isCreate=true;", actual);
    }

    @Test
    public void setIsCreate_false() throws Exception {
        testObject.setIsCreate(false);
        String actual = queryParametersAsString();
        assertEquals("isCreate=false;", actual);
    }

    @Test
    public void setHierarchical() throws Exception {
        testObject.setHierarchical(true);
        String actual = queryParametersAsString();
        assertEquals("hierarchical=true;", actual);
    }

    @Test
    public void setHierarchical_false() throws Exception {
        testObject.setHierarchical(false);
        String actual = queryParametersAsString();
        assertEquals("hierarchical=false;", actual);
    }

    @Test
    public void setIncludeBlogs() throws Exception {
        testObject.setIncludeBlogs(true);
        String actual = queryParametersAsString();
        assertEquals("includeBlogs=true;", actual);
    }

    @Test
    public void setIncludeBlogs_false() throws Exception {
        testObject.setIncludeBlogs(false);
        String actual = queryParametersAsString();
        assertEquals("includeBlogs=false;", actual);
    }

    @Test
    public void setIncludeChildrenOutcomes() throws Exception {
        testObject.setIncludeChildrenOutcomes(true);
        String actual = queryParametersAsString();
        assertEquals("includeChildrenOutcomes=true;", actual);
    }

    @Test
    public void setIncludeChildrenOutcomes_false() throws Exception {
        testObject.setIncludeChildrenOutcomes(false);
        String actual = queryParametersAsString();
        assertEquals("includeChildrenOutcomes=false;", actual);
    }

    @Test
    public void setSendEmails() throws Exception {
        testObject.setSendEmails(true);
        String actual = queryParametersAsString();
        assertEquals("sendEmails=true;", actual);
    }

    @Test
    public void setSendEmails_false() throws Exception {
        testObject.setSendEmails(false);
        String actual = queryParametersAsString();
        assertEquals("sendEmails=false;", actual);
    }

    @Test
    public void setPreserveAspectRatio() throws Exception {
        testObject.setPreserveAspectRatio(true);
        String actual = queryParametersAsString();
        assertEquals("preserveAspectRatio=true;", actual);
    }

    @Test
    public void setPreserveAspectRatio_false() throws Exception {
        testObject.setPreserveAspectRatio(false);
        String actual = queryParametersAsString();
        assertEquals("preserveAspectRatio=false;", actual);
    }

    @Test
    public void setCount() throws Exception {
        testObject.setCount(42);
        String actual = queryParametersAsString();
        assertEquals("count=42;", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCount_negative() throws Exception {
        testObject.setCount(-42);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCount_zero() throws Exception {
        testObject.setCount(0);
    }

    @Test
    public void setStartIndex() throws Exception {
        testObject.setStartIndex(42);
        String actual = queryParametersAsString();
        assertEquals("startIndex=42;", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setStartIndex_negative() throws Exception {
        testObject.setStartIndex(-42);
    }

    @Test
    public void setStartIndex_zero() throws Exception {
        testObject.setStartIndex(0);
        String actual = queryParametersAsString();
        assertEquals("startIndex=0;", actual);
    }

    @Test
    public void setMax() throws Exception {
        testObject.setMax(42);
        String actual = queryParametersAsString();
        assertEquals("max=42;", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMax_negative() throws Exception {
        testObject.setMax(-42);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMax_zero() throws Exception {
        testObject.setMax(0);
    }

    @Test
    public void setWidth() throws Exception {
        testObject.setWidth(42);
        String actual = queryParametersAsString();
        assertEquals("width=42;", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWidth_negative() throws Exception {
        testObject.setWidth(-42);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWidth_zero() throws Exception {
        testObject.setWidth(0);
    }

    @Test
    public void setHeight() throws Exception {
        testObject.setHeight(42);
        String actual = queryParametersAsString();
        assertEquals("height=42;", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setHeight_negative() throws Exception {
        testObject.setHeight(-42);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setHeight_zero() throws Exception {
        testObject.setHeight(0);
    }

    private String queryParametersAsString() {
        return queryParametersAsString(testObject.values);
    }

    private String queryParametersAsString(JiveCoreRequestOptionsValues values) {
        StringBuilder builder = new StringBuilder();
        Map<String, List<String>> stringListMap = values.provideQueryParameters();
        for (Map.Entry<String, List<String>> entry : stringListMap.entrySet()) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(entry.getKey())
                    .append("=")
                    .append(iterableToString(entry.getValue()))
                    .append(";");
        }
        return builder.toString();
    }

    private String iterableToString(Iterable<String> iterable) {
        StringBuilder builder = new StringBuilder();
        for (String item : iterable) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(item);
        }
        return builder.toString();
    }

    private PersonEntity createPersonEntity(String ref) {
        PersonEntity personEntity = new PersonEntity();
        personEntity.resources = new HashMap<String, ResourceEntity>();
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.ref = ref;
        personEntity.resources.put("self", resourceEntity);
        return personEntity;
    }

    private Date createTestDate() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, 2014);
        calendar.set(Calendar.MONTH, Calendar.OCTOBER);
        calendar.set(Calendar.DAY_OF_MONTH, 13);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        return calendar.getTime();
    }

}