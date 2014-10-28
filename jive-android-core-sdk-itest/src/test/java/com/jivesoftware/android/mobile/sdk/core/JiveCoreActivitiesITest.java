package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.ActivityListEntity;
import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JiveCoreActivitiesITest extends AbstractITest {

    @Test
    public void fetchActivitiesByPerson() throws Exception {
        // Get a user's self url
        PersonEntity user2PersonEntity = jiveCoreAdmin.fetchPerson(USER2.selfPathAndQuery).call();
        String personUrl = user2PersonEntity.resources.get("self").ref;
        // Request the activity for that user
        ActivityListEntity activityListEntity = jiveCoreAdmin.fetchActivitiesByPerson(
                personUrl, new JiveCoreRequestOptions()).call();
        // Make sure all the actors are our user
        assertTrue(activityListEntity.list.size() > 0);
        for (int i = 0; i < activityListEntity.list.size(); i++) {
            String actual = activityListEntity.list.get(i).actor.id;
            assertEquals("Activity at index " + i + " had the wrong actor", personUrl, actual);
        }
    }

    @Test
    public void fetchActivities() throws Exception {
        // Get a user's self url
        PersonEntity user2PersonEntity = jiveCoreAdmin.fetchPerson(USER2.selfPathAndQuery).call();
        String personUrl = user2PersonEntity.resources.get("self").ref;
        // Request the activity for that user
        ActivityListEntity activityListEntity = jiveCoreAdmin.fetchActivitiesByPerson(personUrl, new JiveCoreRequestOptions()).call();
        // Get the next page of activity's URL
        String nextUrl = activityListEntity.links.next;
        assertNotNull(nextUrl);
        // Get the next page using the URL:
        activityListEntity = jiveCoreAdmin.fetchActivities(nextUrl).call();
        // Make sure all the actors are our user
        assertTrue(activityListEntity.list.size() > 0);
        for (int i = 0; i < activityListEntity.list.size(); i++) {
            String actual = activityListEntity.list.get(i).actor.id;
            assertEquals("Activity at index " + i + " had the wrong actor", personUrl, actual);
        }
    }

}
