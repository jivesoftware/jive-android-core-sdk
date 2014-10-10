package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreRequestOptions;
import com.jivesoftware.android.mobile.sdk.entity.MemberEntity;
import com.jivesoftware.android.mobile.sdk.entity.MemberListEntity;
import com.jivesoftware.android.mobile.sdk.entity.PlaceEntity;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreAPIException;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.UUID;

import static com.jivesoftware.android.mobile.sdk.entity.EntityUtils.getSelfResourceRef;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.JiveObjectEntityMatchers.objectSelfURL;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ListEntityMatchers.listEntities;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.MemberEntityMatchers.memberGroup;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.MemberEntityMatchers.memberPerson;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.PlaceEntityMatchers.placeName;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class JiveCorePlaceITest extends AbstractITest {
    @Test
    public void testCreateAndFetchAndDeletePlace() throws Exception {
        String uuid = UUID.randomUUID().toString();

        PlaceEntity creatingPlaceEntity = new PlaceEntity();
        creatingPlaceEntity.displayName = uuid;
        creatingPlaceEntity.name = uuid;
        creatingPlaceEntity.type = "group";
        creatingPlaceEntity.groupType = "OPEN";

        PlaceEntity createdPlaceEntity = jiveCoreUser2.createPlace(creatingPlaceEntity).call();

        MemberListEntity createdPlaceMemberListEntity = jiveCoreUser2.fetchMembersByPlace(createdPlaceEntity.placeID, new JiveCoreRequestOptions()).call();
        //IntelliJ wrongly things <MemberEntity> is redundant
        assertThat(createdPlaceMemberListEntity, listEntities(Matchers.<MemberEntity>contains(
                allOf(
                        memberPerson(objectSelfURL(USER2.selfURL)),
                        memberGroup(objectSelfURL(getSelfResourceRef(createdPlaceEntity)))))));

        PlaceEntity fetchedPlaceEntity = jiveCoreAdmin.fetchPlace(getSelfResourceRef(createdPlaceEntity)).call();
        assertThat(fetchedPlaceEntity, placeName(uuid));

        jiveCoreUser2.deletePlace(getSelfResourceRef(createdPlaceEntity)).call();

        try {
            jiveCoreAdmin.fetchPlace(getSelfResourceRef(createdPlaceEntity)).call();
            fail("Expected 404");
        } catch (JiveCoreAPIException e) {
            assertEquals(404, e.statusCode);
        }
    }
}
