package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JiveCoreFetchPersonITest extends AbstractITest {
    @Test
    public void testFetchPerson() throws Exception {
        PersonEntity user2PersonEntity = jiveCoreAdmin.fetchPerson(USER2.selfPathAndQuery).call();
        assertEquals(USER2.displayName, user2PersonEntity.displayName);
    }
}
