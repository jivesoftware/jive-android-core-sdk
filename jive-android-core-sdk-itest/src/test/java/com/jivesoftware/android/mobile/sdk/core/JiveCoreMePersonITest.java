package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JiveCoreMePersonITest extends AbstractITest {
    @Test
    public void testWhenFetchMePersonThenNameIsCorrect() throws Exception {
        JiveCoreCallable<PersonEntity> fetchMePersonJiveCoreCallable = jiveCoreAdmin.fetchMePerson();
        PersonEntity mePersonEntity = fetchMePersonJiveCoreCallable.call();

        assertEquals(ADMIN.displayName, mePersonEntity.name.formatted);
    }
}
