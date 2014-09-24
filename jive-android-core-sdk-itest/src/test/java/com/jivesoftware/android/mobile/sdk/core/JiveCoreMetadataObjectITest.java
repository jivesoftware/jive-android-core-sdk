package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.MetadataObjectEntity;
import org.junit.Test;

import static com.jivesoftware.android.mobile.sdk.entity.matcher.MetadataObjectEntityMatchers.metadataObjectName;
import static org.junit.Assert.assertThat;

public class JiveCoreMetadataObjectITest extends AbstractITest {
    @Test
    public void testGetProfileMetadataObjectInUSEnglish() throws Exception {
        MetadataObjectEntity metadataObjectEntity = jiveCoreAdmin.fetchMetadataObject("profile", "en_US").call();
        assertThat(metadataObjectEntity, metadataObjectName("profile"));
    }
}
