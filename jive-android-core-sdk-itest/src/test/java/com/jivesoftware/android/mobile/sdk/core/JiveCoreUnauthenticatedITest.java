package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.MetadataPropertyEntity;
import com.jivesoftware.android.mobile.sdk.entity.VersionEntity;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    public void testWhenVersionIsFetchedThenItIs8c4() throws Exception {
        JiveCoreCallable<VersionEntity> fetchVersionJiveCoreCallable = jiveCoreUnauthenticatedAdmin.fetchVersion();
        VersionEntity versionEntity = fetchVersionJiveCoreCallable.call();

        assertEquals(TEST_URL.toExternalForm(), versionEntity.instanceURL);
        assertEquals("8.0.0.0 8c4", versionEntity.jiveVersion);
        assertNull(versionEntity.ssoEnabled);
    }
}
