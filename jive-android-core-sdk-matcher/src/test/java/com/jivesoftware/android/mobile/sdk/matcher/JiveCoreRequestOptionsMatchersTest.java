package com.jivesoftware.android.mobile.sdk.matcher;

import com.jivesoftware.android.mobile.sdk.core.JiveCoreRequestOptions;
import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import com.jivesoftware.android.mobile.sdk.entity.ResourceEntity;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreDirective;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreDirectiveValue;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreEntityDescriptor;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreEntryState;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCorePlaceType;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreValueFactory;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreVerb;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;

public class JiveCoreRequestOptionsMatchersTest {

    private JiveCoreRequestOptions options;

    @Before
    public void setUp() throws Exception {
        options = new JiveCoreRequestOptions();
    }

    @Test
    public void testEntityDescriptorFilters_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsEntityDescriptorFilters(contains("1,2"))));
    }

    @Test
    public void testEntityDescriptorFilters_single() throws Exception {
        options.setEntityDescriptorFilter(1, 2L);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsEntityDescriptorFilters(contains("1,2")));
    }

    @Test
    public void testRelationshipFilters() throws Exception {
        options.setEntityDescriptorFilter(new JiveCoreEntityDescriptor(1, 2L), new JiveCoreEntityDescriptor(3, 4L));
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsEntityDescriptorFilters(contains("1,2,3,4")));
    }

    @Test
    public void testSearchTermFilters_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsSearchTermFilters(contains("one", "two"))));
    }

    @Test
    public void testSearchTermFilters() throws Exception {
        options.setSearchTermFilter(Arrays.asList("one", "two"));
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsSearchTermFilters(contains("one", "two")));
    }

    @Test
    public void testTagFilters_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsTagFilters(contains("one", "two"))));
    }

    @Test
    public void testTagFilters() throws Exception {
        options.setTagFilter(Arrays.asList("one", "two"));
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsTagFilters(contains("one", "two")));
    }

    @Test
    public void testTypeFilters_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsTypeFilters(contains("one", "two"))));
    }

    @Test
    public void testTypeFilters() throws Exception {
        options.setTypeFilter(Arrays.asList(JiveCorePlaceType.space, JiveCorePlaceType.group));
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsTypeFilters(contains("space", "group")));
    }

    @Test
    public void testVerbFilters_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsVerbFilters(contains("jive:created", "jive:promoted"))));
    }

    @Test
    public void testVerbFilters() throws Exception {
        options.setVerbFilter(Arrays.asList(JiveCoreVerb.created, JiveCoreVerb.promoted));
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsVerbFilters(contains("jive:created", "jive:promoted")));
    }

    @Test
    public void testAuthorFilters_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsAuthorFilters(contains("one"))));
    }

    @Test
    public void testAuthorFilters() throws Exception {
        options.setAuthorFilter(Arrays.asList("one"));
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsAuthorFilters(contains("one")));
    }

    @Test
    public void testPlaceFilters_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsPlaceFilters(contains("one"))));
    }

    @Test
    public void testPlaceFilters() throws Exception {
        options.setPlaceFilter(Arrays.asList("one"));
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsPlaceFilters(contains("one")));
    }

    @Test
    public void testEntryStateFilters_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsEntryStateFilters(contains("hidden"))));
    }

    @Test
    public void testEntryStateFilters() throws Exception {
        options.setEntryStateFilter(Arrays.asList(JiveCoreEntryState.hidden));
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsEntryStateFilters(contains("hidden")));
    }

    @Test
    public void testUnreadFilter_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsUnreadFilter()));
    }

    @Test
    public void testUnreadFilter_explicitNegative() throws Exception {
        options.setUnreadFilter(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsUnreadFilter()));
    }

    @Test
    public void testUnreadFilter() throws Exception {
        options.setUnreadFilter(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsUnreadFilter());
    }

    @Test
    public void testNotificationsFilter_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsNotificationsFilter()));
    }

    @Test
    public void testNotificationsFilter_explicitNegative() throws Exception {
        options.setNotificationsFilter(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsNotificationsFilter()));
    }

    @Test
    public void testNotificationsFilter() throws Exception {
        options.setNotificationsFilter(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsNotificationsFilter());
    }

    @Test
    public void testDirectives_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsDirectives(contains("one"))));
    }

    @Test
    public void testDirectives() throws Exception {
        options.setDirectives(Arrays.asList(JiveCoreValueFactory.createDirectiveValue("one")));
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsDirectives(contains("one")));
    }

    @Test
    public void testCollapseSkipCollectionIdsDirective_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsCollapseSkipCollectionIdsDirective(contains("one", "two"))));
    }

    @Test
    public void testCollapseSkipCollectionIdsDirective() throws Exception {
        options.setCollapseSkipCollectionIdsDirective(Arrays.asList("one", "two"));
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsCollapseSkipCollectionIdsDirective(contains("one", "two")));
    }

    @Test
    public void testIncludeRtcDirective_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsIncludeRtcDirective()));
    }

    @Test
    public void testIncludeRtcDirective() throws Exception {
        options.setDirectives(Arrays.<JiveCoreDirectiveValue>asList(JiveCoreDirective.include_rtc));
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsIncludeRtcDirective());
    }

    @Test
    public void testCollapseDirective_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsCollapseDirective()));
    }

    @Test
    public void testCollapseDirective() throws Exception {
        options.setDirectives(Arrays.<JiveCoreDirectiveValue>asList(JiveCoreDirective.collapse));
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsCollapseDirective());
    }

    @Test
    public void testSort_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsDirectives(contains("one"))));
    }

    @Test
    public void testSort() throws Exception {
        options.setSort(JiveCoreValueFactory.createSortValue("one"));
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsSort("one"));
    }

    @Test
    public void testPublished_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsPublished("one")));
    }

    @Test
    public void testPublished() throws Exception {
        options.setPublished(createTestDate());
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsPublished("2014-10-13T00:00:00.000+0000"));
    }

    @Test
    public void testUpdated_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsUpdated("one")));
    }

    @Test
    public void testUpdated() throws Exception {
        options.setUpdated(createTestDate());
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsUpdated("2014-10-13T00:00:00.000+0000"));
    }

    @Test
    public void testBefore_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsBefore("one")));
    }

    @Test
    public void testBefore() throws Exception {
        options.setBefore(createTestDate());
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsBefore("2014-10-13T00:00:00.000+0000"));
    }

    @Test
    public void testAfter_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsAfter("one")));
    }

    @Test
    public void testAfter() throws Exception {
        options.setAfter(createTestDate());
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsAfter("2014-10-13T00:00:00.000+0000"));
    }

    @Test
    public void testAuthors_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsAuthors(contains("one"))));
    }

    @Test
    public void testAuthors() throws Exception {
        options.setAuthors(Arrays.asList(createPersonEntity("one"), createPersonEntity("two")));
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsAuthors(contains("one", "two")));
    }

    @Test
    public void testFields_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsFields(contains("one"))));
    }

    @Test
    public void testFields() throws Exception {
        options.setFields(Arrays.asList("one", "two"));
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsFields(contains("one", "two")));
    }

    @Test
    public void testAnchorUri_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsAnchorUri("one")));
    }

    @Test
    public void testAnchorUri() throws Exception {
        options.setAnchor("one");
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsAnchorUri("one"));
    }

    @Test
    public void testAutoCategorize_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsAutoCategorize()));
    }

    @Test
    public void testAutoCategorize_explicitNegative() throws Exception {
        options.setAutoCategorize(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsAutoCategorize()));
    }

    @Test
    public void testAutoCategorize() throws Exception {
        options.setAutoCategorize(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsAutoCategorize());
    }

    @Test
    public void testCollapse_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsCollapse()));
    }

    @Test
    public void testCollapse_explicitNegative() throws Exception {
        options.setCollapsed(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsCollapse()));
    }

    @Test
    public void testCollapse() throws Exception {
        options.setCollapsed(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsCollapse());
    }

    @Test
    public void testOldestUnread_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsOldestUnread()));
    }

    @Test
    public void testOldestUnread_explicitNegative() throws Exception {
        options.setOldestUnread(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsOldestUnread()));
    }

    @Test
    public void testOldestUnread() throws Exception {
        options.setOldestUnread(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsOldestUnread());
    }

    @Test
    public void testAbridged_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsAbridged()));
    }

    @Test
    public void testAbridged_explicitNegative() throws Exception {
        options.setAbridged(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsAbridged()));
    }

    @Test
    public void testAbridged() throws Exception {
        options.setAbridged(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsAbridged());
    }

    @Test
    public void testActiveOnly_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsActiveOnly()));
    }

    @Test
    public void testActiveOnly_explicitNegative() throws Exception {
        options.setActiveOnly(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsActiveOnly()));
    }

    @Test
    public void testActiveOnly() throws Exception {
        options.setActiveOnly(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsActiveOnly());
    }

    @Test
    public void testExclude_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsExclude()));
    }

    @Test
    public void testExclude_explicitNegative() throws Exception {
        options.setExclude(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsExclude()));
    }

    @Test
    public void testExclude() throws Exception {
        options.setExclude(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsExclude());
    }

    @Test
    public void testExcludeReplies_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsExcludeReplies()));
    }

    @Test
    public void testExcludeReplies_explicitNegative() throws Exception {
        options.setExcludeReplies(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsExcludeReplies()));
    }

    @Test
    public void testExcludeReplies() throws Exception {
        options.setExcludeReplies(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsExcludeReplies());
    }

    @Test
    public void testMinor_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsMinor()));
    }

    @Test
    public void testMinor_explicitNegative() throws Exception {
        options.setMinor(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsMinor()));
    }

    @Test
    public void testMinor() throws Exception {
        options.setMinor(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsMinor());
    }

    @Test
    public void testAuthorComment_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsAuthorComment()));
    }

    @Test
    public void testAuthorComment_explicitNegative() throws Exception {
        options.setAuthorComment(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsAuthorComment()));
    }

    @Test
    public void testAuthorComment() throws Exception {
        options.setAuthorComment(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsAuthorComment());
    }

    @Test
    public void testIsCreate_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsIsCreate()));
    }

    @Test
    public void testIsCreate_explicitNegative() throws Exception {
        options.setIsCreate(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsIsCreate()));
    }

    @Test
    public void testIsCreate() throws Exception {
        options.setIsCreate(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsIsCreate());
    }

    @Test
    public void testHierarchical_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsHierarchical()));
    }

    @Test
    public void testHierarchical_explicitNegative() throws Exception {
        options.setHierarchical(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsHierarchical()));
    }

    @Test
    public void testHierarchical() throws Exception {
        options.setHierarchical(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsHierarchical());
    }

    @Test
    public void testIncludeBlogs_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsIncludeBlogs()));
    }

    @Test
    public void testIncludeBlogs_explicitNegative() throws Exception {
        options.setIncludeBlogs(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsIncludeBlogs()));
    }

    @Test
    public void testIncludeBlogs() throws Exception {
        options.setIncludeBlogs(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsIncludeBlogs());
    }

    @Test
    public void testIncludeChildrenOutcomes_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsIncludeChildrenOutcomes()));
    }

    @Test
    public void testIncludeChildrenOutcomes_explicitNegative() throws Exception {
        options.setIncludeChildrenOutcomes(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsIncludeChildrenOutcomes()));
    }

    @Test
    public void testIncludeChildrenOutcomes() throws Exception {
        options.setIncludeChildrenOutcomes(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsIncludeChildrenOutcomes());
    }

    @Test
    public void testSendEmails_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsSendEmails()));
    }

    @Test
    public void testSendEmails_explicitNegative() throws Exception {
        options.setSendEmails(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsSendEmails()));
    }

    @Test
    public void testSendEmails() throws Exception {
        options.setSendEmails(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsSendEmails());
    }

    @Test
    public void testPreserveAspectRatio_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsPreserveAspectRatio()));
    }

    @Test
    public void testPreserveAspectRatio_explicitNegative() throws Exception {
        options.setPreserveAspectRatio(false);
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsPreserveAspectRatio()));
    }

    @Test
    public void testPreserveAspectRatio() throws Exception {
        options.setPreserveAspectRatio(true);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsPreserveAspectRatio());
    }

    @Test
    public void testCount_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsCount(1)));
    }

    @Test
    public void testCount() throws Exception {
        options.setCount(1);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsCount(1));
    }

    @Test
    public void testStartIndex_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsStartIndex(1)));
    }

    @Test
    public void testStartIndex() throws Exception {
        options.setStartIndex(1);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsStartIndex(1));
    }

    @Test
    public void testMax_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsMax(1)));
    }

    @Test
    public void testMax() throws Exception {
        options.setMax(1);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsMax(1));
    }

    @Test
    public void testWidth_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsWidth(1)));
    }

    @Test
    public void testWidth() throws Exception {
        options.setWidth(1);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsWidth(1));
    }

    @Test
    public void testHeight_negative() throws Exception {
        Assert.assertThat(options, not(JiveCoreRequestOptionsMatchers.optionsHeight(1)));
    }

    @Test
    public void testHeight() throws Exception {
        options.setHeight(1);
        Assert.assertThat(options, JiveCoreRequestOptionsMatchers.optionsHeight(1));
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