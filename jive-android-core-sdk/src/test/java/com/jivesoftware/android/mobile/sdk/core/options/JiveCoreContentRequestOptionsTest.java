package com.jivesoftware.android.mobile.sdk.core.options;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import com.jivesoftware.android.mobile.sdk.entity.ResourceEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by ben.oberkfell on 6/23/14.
 */
public class JiveCoreContentRequestOptionsTest {

    private PersonEntity testPerson1;
    private PersonEntity testPerson2;

    private final JiveCoreContentRequestOptions testObject = new JiveCoreContentRequestOptions();

    @Before
    public void setUp() {
        testPerson1 = new PersonEntity();
        ResourceEntity testPerson1SelfResource = new ResourceEntity();
        testPerson1SelfResource.ref = "/api/core/v3/people/1";
        testPerson1.resources = new HashMap<String, ResourceEntity>();
        testPerson1.resources.put("self",  testPerson1SelfResource);

        testPerson2 = new PersonEntity();
        ResourceEntity testPerson2SelfResource = new ResourceEntity();
        testPerson2SelfResource.ref = "/api/core/v3/people/2";
        testPerson2.resources = new HashMap<String, ResourceEntity>();
        testPerson2.resources.put("self", testPerson2SelfResource);
    }

    @Test
    public void testInitiallyNoAuthors() {
        assertNull(testObject.getAuthors());
    }

    @Test
    public void testParametersWithOnePerson() {
        testObject.setAuthors(ImmutableList.of(testPerson1));
        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();

        Map<String, List<String>> expectedQueryParameters = Maps.newHashMap();
        expectedQueryParameters.put("filter", ImmutableList.of("author(/api/core/v3/people/1)"));
        assertEquals(expectedQueryParameters, actualQueryParameters);
    }

    @Test
    public void testParametersWithMultiplePeople() {
        testObject.setAuthors(ImmutableList.of(testPerson1,testPerson2));
        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();

        Map<String, List<String>> expectedQueryParameters = Maps.newHashMap();
        expectedQueryParameters.put("filter", ImmutableList.of("author(/api/core/v3/people/1,/api/core/v3/people/2)"));
        assertEquals(expectedQueryParameters, actualQueryParameters);
    }

    @Test
    public void testParametersWithTypeFilterAndPeopleFilter() {
        testObject.setTypes(ImmutableList.of("document", "discussion"));
        testObject.setAuthors(ImmutableList.of(testPerson1,testPerson2));
        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();

        Map<String, List<String>> expectedQueryParameters = Maps.newHashMap();
        expectedQueryParameters.put("filter", ImmutableList.of("type(document,discussion)","author(/api/core/v3/people/1,/api/core/v3/people/2)"));
        assertEquals(expectedQueryParameters, actualQueryParameters);
    }
}
