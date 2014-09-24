package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.MetadataPropertyEntity;
import org.junit.Test;

import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class JiveCoreMetadataPropertiesITest extends AbstractITest {
    @Test
    public void fetchMetadataProperties() throws Exception {
        MetadataPropertyEntity[] metadataPropertyEntities = jiveCoreAdmin.fetchMetadataProperties().call();
        assertThat(metadataPropertyEntities, not(emptyArray()));
    }
}
