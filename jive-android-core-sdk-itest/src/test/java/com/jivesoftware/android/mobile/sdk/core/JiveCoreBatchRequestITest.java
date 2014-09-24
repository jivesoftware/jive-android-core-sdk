package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.BatchRequestEntity;
import com.jivesoftware.android.mobile.sdk.entity.BatchResponseEntity;
import com.jivesoftware.android.mobile.sdk.entity.EndpointRequestEntity;
import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import com.jivesoftware.android.mobile.sdk.entity.RequestMethod;
import com.jivesoftware.android.mobile.sdk.gson.JiveGson;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class JiveCoreBatchRequestITest extends AbstractITest {
    @Test
    public void testBatchRequestForTwoPeople() throws Exception {

        EndpointRequestEntity adminPersonEndpointRequestEntity = new EndpointRequestEntity();
        adminPersonEndpointRequestEntity.method = RequestMethod.GET;
        adminPersonEndpointRequestEntity.endpoint = ADMIN.selfPathAndQuery;

        EndpointRequestEntity user2PersonEndpointRequestEntity = new EndpointRequestEntity();
        user2PersonEndpointRequestEntity.method = RequestMethod.GET;
        user2PersonEndpointRequestEntity.endpoint = USER2.selfPathAndQuery;

        BatchRequestEntity adminPersonBatchRequestEntity = new BatchRequestEntity();
        adminPersonBatchRequestEntity.key = "admin";
        adminPersonBatchRequestEntity.request = adminPersonEndpointRequestEntity;

        BatchRequestEntity user2BatchRequestEntity = new BatchRequestEntity();
        user2BatchRequestEntity.key = "user2";
        user2BatchRequestEntity.request = user2PersonEndpointRequestEntity;

        JiveCoreCallable<BatchResponseEntity[]> executeBatchOperationCallable = jiveCoreAdmin.executeBatchOperation(new BatchRequestEntity[]{adminPersonBatchRequestEntity, user2BatchRequestEntity});
        BatchResponseEntity[] batchResponseEntities = executeBatchOperationCallable.call();

        // IntelliJ says that the explicit <BatchResponseEntity> is not necessary, but it doesn't compile otherwise
        assertThat(Arrays.asList(batchResponseEntities), contains(
                batchResponseEntityDataAsPersonHasDisplayName(ADMIN.displayName),
                batchResponseEntityDataAsPersonHasDisplayName(USER2.displayName)));
    }

    private PropertyMatcher<String, BatchResponseEntity> batchResponseEntityDataAsPersonHasDisplayName(String displayName) {
        final JiveGson jiveGson = new JiveGson();
        return new PropertyMatcher<String, BatchResponseEntity>("displayName", displayName) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull BatchResponseEntity item) throws Exception {
                PersonEntity personEntity = jiveGson.fromJsonElement(item.data, PersonEntity.class);
                return personEntity.displayName;
            }
        };
    }
}
