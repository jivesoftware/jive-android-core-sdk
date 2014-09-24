package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import com.jivesoftware.android.mobile.sdk.entity.StreamListEntity;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertNotEquals;

public class JiveCoreStreamITest extends AbstractITest {
    @Test
    public void testWhenFetchStreamsThenStreamReferenceIsNotNull() throws Exception {
        JiveCoreCallable<PersonEntity> fetchMePersonJiveCoreCallable = jiveCoreAdmin.fetchMePerson();
        PersonEntity mePersonEntity = fetchMePersonJiveCoreCallable.call();

        String streamUrl = mePersonEntity.resources.get("streams").ref;
        JiveCoreCallable<StreamListEntity> streamListEntityJiveCoreCallable = jiveCoreAdmin.fetchStreams(streamUrl);
        StreamListEntity streamListEntity = streamListEntityJiveCoreCallable.call();

        assertNotEquals(streamListEntity.list, Collections.emptyList());
    }

    @Test
    public void testWhenUserStreamsAreRequestedThenTheyAreRetrieved() throws IOException {
        JiveCoreCallable<PersonEntity> fetchMePersonJiveCoreCallable = jiveCoreAdmin.fetchMePerson();
        PersonEntity mePersonEntity = fetchMePersonJiveCoreCallable.call();

        JiveCoreCallable<StreamListEntity> streamsCallable = jiveCoreAdmin.fetchStreams(mePersonEntity.resources.get("streams").ref);
        StreamListEntity entity = streamsCallable.call();

        assertNotEquals(entity.list, Collections.emptyList());
    }
}
