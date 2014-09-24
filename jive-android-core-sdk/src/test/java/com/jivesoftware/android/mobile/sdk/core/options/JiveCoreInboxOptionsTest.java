package com.jivesoftware.android.mobile.sdk.core.options;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JiveCoreInboxOptionsTest {
    private JiveCoreInboxOptions testObject;

    @Before
    public void setup() {
        testObject = new JiveCoreInboxOptions();
    }

    @Test
    public void testInitiallyNotUnreadNullAuthorNullTypesNullDirectivesNotOldestUnreadNullCollapseSkipCollectionIds() {
        testObject.setCollapse(true);

        assertEquals((Map<String, ? extends List<String>>) ImmutableMap.of(
                        "collapse", ImmutableList.of("true")),
                testObject.copyQueryParameters());
    }

    @Test
    public void testUnread() {
        testObject.setCollapse(true);
        testObject.setUnread(true);

        assertEquals((Map<String, ? extends List<String>>) ImmutableMap.of(
                        "collapse", ImmutableList.of("true"),
                        "filter", ImmutableList.of("unread")),
                testObject.copyQueryParameters());
    }

    @Test
    public void testAuthor() {
        testObject.setCollapse(true);
        testObject.setAuthorPathAndQuery("/foo/bar");

        assertEquals((Map<String, ? extends List<String>>) ImmutableMap.of(
                        "collapse", ImmutableList.of("true"),
                        "filter", ImmutableList.of("author(/foo/bar)")),
                testObject.copyQueryParameters());
    }

    @Test
    public void testTypes() {
        testObject.setCollapse(true);
        testObject.setTypes(ImmutableList.of("foo", "bar"));

        assertEquals((Map<String, ? extends List<String>>) ImmutableMap.of(
                        "collapse", ImmutableList.of("true"),
                        "filter", ImmutableList.of("type(foo,bar)")),
                testObject.copyQueryParameters());
    }

    @Test
    public void testDirectives() {
        testObject.setCollapse(true);
        testObject.setDirectives(ImmutableList.of("foo(bar)", "baz(buzz)"));

        assertEquals((Map<String, ? extends List<String>>) ImmutableMap.of(
                        "collapse", ImmutableList.of("true"),
                        "directive", ImmutableList.of("foo(bar),baz(buzz)")),
                testObject.copyQueryParameters());
    }

    @Test
    public void testOldestUnread() {
        testObject.setCollapse(true);
        testObject.setOldestUnread(true);

        assertEquals((Map<String, ? extends List<String>>) ImmutableMap.of(
                        "collapse", ImmutableList.of("true"),
                        "oldestUnread", ImmutableList.of("true")),
                testObject.copyQueryParameters());
    }

    @Test
    public void testCollapseSkipCollectionIds() {
        testObject.setCollapse(true);
        testObject.setCollapseSkipCollectionIds(ImmutableList.of("1", "2", "3"));

        assertEquals((Map<String, ? extends List<String>>) ImmutableMap.of(
                        "collapse", ImmutableList.of("true"),
                        "directive", ImmutableList.of("collapseSkip(1,2,3)")),
                testObject.copyQueryParameters());
    }

    @Test
    public void testUnreadAndAuthorAndTypesAndDirectivesAndOldestUnreadAndCollapseSkipCollectionIds() {
        testObject.setCollapse(true);
        testObject.setUnread(true);
        testObject.setAuthorPathAndQuery("/theAuthor");
        testObject.setTypes(ImmutableList.of("type1", "type2"));
        testObject.setDirectives(ImmutableList.of("foo(bar)", "baz(buzz)"));
        testObject.setOldestUnread(true);
        testObject.setCollapseSkipCollectionIds(ImmutableList.of("1", "2", "3"));

        assertEquals((Map<String, ? extends List<String>>) ImmutableMap.of(
                        "collapse", ImmutableList.of("true"),
                        "filter", ImmutableList.of(
                                "unread",
                                "author(/theAuthor)",
                                "type(type1,type2)"),
                        "directive", ImmutableList.of("foo(bar),baz(buzz),collapseSkip(1,2,3)"),
                        "oldestUnread", ImmutableList.of("true")),
                testObject.copyQueryParameters());
    }
}
