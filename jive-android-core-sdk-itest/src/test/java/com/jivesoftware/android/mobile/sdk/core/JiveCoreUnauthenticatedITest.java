package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.MetadataPropertyEntity;
import com.jivesoftware.android.mobile.sdk.entity.VersionEntity;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreRedirectedException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class JiveCoreUnauthenticatedITest extends AbstractITest {
    @Test
    public void testWhenFetchPublicMetadataPropertiesThenPropertiesAreCorrect() throws Exception {
        JiveCoreCallable<MetadataPropertyEntity[]> fetchPublicMetadataPropertiesJiveCoreCallable = jiveCoreUnauthenticatedAdmin.fetchPublicMetadataProperties();
        MetadataPropertyEntity[] publicMetadataPropertyEntities = fetchPublicMetadataPropertiesJiveCoreCallable.call();

        assertEquals(1, publicMetadataPropertyEntities.length);
        assertEquals("feature.mobile.nativeapp.allowed", publicMetadataPropertyEntities[0].name);
        assertEquals("boolean", publicMetadataPropertyEntities[0].type);
        assertEquals(true, publicMetadataPropertyEntities[0].value);
    }

    @Test
    public void testWhenVersionIsFetchedThenItIs9c1() throws Exception {
        JiveCoreCallable<VersionEntity> fetchVersionJiveCoreCallable = jiveCoreUnauthenticatedAdmin.fetchVersion();
        VersionEntity versionEntity = fetchVersionJiveCoreCallable.call();

        assertEquals(TEST_URL.toExternalForm(), versionEntity.instanceURL);
        assertEquals("9.0.0.0 9c1", versionEntity.jiveVersion);
        assertNull(versionEntity.ssoEnabled);
    }

    @Test
    public void testWhenVersionIsFetchedFromRedirectingUrlThenJiveCoreRedirectedExceptionIsThrown() throws Exception {
        JiveCoreCallable<VersionEntity> fetchVersionJiveCoreCallable = jiveCoreUnauthenticatedAdminRedirecting.fetchVersion();
        try {
            fetchVersionJiveCoreCallable.call();
            fail("Expected JiveCoreRedirectedException");
        } catch (JiveCoreRedirectedException e) {
            assertEquals(TEST_URL + "/api/version", e.location);
        }
    }
}
