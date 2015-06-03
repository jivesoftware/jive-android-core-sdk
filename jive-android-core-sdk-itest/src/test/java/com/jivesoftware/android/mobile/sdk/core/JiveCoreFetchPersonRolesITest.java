package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import com.jivesoftware.android.mobile.sdk.entity.PersonRolesEntity;
import com.jivesoftware.android.mobile.sdk.entity.ResourceEntity;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JiveCoreFetchPersonRolesITest extends AbstractITest {
    @Test
    public void testWhenFetchPersonRoles() throws Exception {
        JiveCoreCallable<PersonEntity> fetchMePersonJiveCoreCallable = jiveCoreAdmin.fetchMePerson();
        PersonEntity mePersonEntity = fetchMePersonJiveCoreCallable.call();

        ResourceEntity roles = mePersonEntity.resources.get("roles");
        JiveCoreCallable<PersonRolesEntity> callable = jiveCoreAdmin.fetchPersonRoles(roles.ref);
        PersonRolesEntity personRolesEntity = callable.call();
        assertEquals(true, personRolesEntity.fullAccess);
        assertNotNull(personRolesEntity.manageCommunity);
        assertNotNull(personRolesEntity.manageGroups);
        assertNotNull(personRolesEntity.manageNewsStreams);
        assertNotNull(personRolesEntity.manageSystem);
        assertNotNull(personRolesEntity.manageUsers);
        assertNotNull(personRolesEntity.moderate);
    }
}
