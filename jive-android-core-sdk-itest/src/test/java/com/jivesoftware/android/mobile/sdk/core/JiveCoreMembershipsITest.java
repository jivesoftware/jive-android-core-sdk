package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreContentRequestOptions;
import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreCountRequestOptions;
import com.jivesoftware.android.mobile.sdk.entity.MemberEntity;
import com.jivesoftware.android.mobile.sdk.entity.MemberListEntity;
import com.jivesoftware.android.mobile.sdk.entity.NewMemberEntity;
import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import com.jivesoftware.android.mobile.sdk.entity.PlaceEntity;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

import static com.jivesoftware.android.mobile.sdk.entity.EntityUtils.getSelfResourceRef;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.JiveObjectEntityMatchers.objectSelfURL;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ListEntityMatchers.listEntities;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.MemberEntityMatchers.memberGroup;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.MemberEntityMatchers.memberPerson;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class JiveCoreMembershipsITest extends AbstractITest {
    private PersonEntity user3PersonEntity;
    private PlaceEntity createdPlaceEntity;

    @Before
    public void setup() throws Exception {
        super.setup();

        user3PersonEntity = jiveCoreUser3.fetchMePerson().call();
        MemberListEntity user3MemberListEntity = jiveCoreUser3.fetchMembersByPerson(user3PersonEntity.id, new JiveCoreCountRequestOptions()).call();
        for (MemberEntity memberEntity : user3MemberListEntity.list) {
            jiveCoreUser3.deleteMember(getSelfResourceRef(memberEntity)).call();
        }


        String uuid = UUID.randomUUID().toString();

        PlaceEntity creatingPlaceEntity = new PlaceEntity();
        creatingPlaceEntity.displayName = uuid;
        creatingPlaceEntity.name = uuid;
        creatingPlaceEntity.type = "group";
        creatingPlaceEntity.groupType = "OPEN";

        createdPlaceEntity = jiveCoreUser2.createPlace(creatingPlaceEntity).call();
    }

    @After
    public void tearDown() throws Exception {
        if (createdPlaceEntity != null) {
            jiveCoreUser2.deletePlace(getSelfResourceRef(createdPlaceEntity)).call();
            createdPlaceEntity = null;
        }

        super.tearDown();
    }

    @Test
    public void testCreateReadDeleteMembership() throws Exception {

        NewMemberEntity newMemberEntity = new NewMemberEntity();
        newMemberEntity.person = USER3.selfPathAndQuery;
        newMemberEntity.state = "member";

        MemberEntity createdMemberEntity = jiveCoreUser3.createMember(createdPlaceEntity.placeID, newMemberEntity).call();
        assertThat(createdMemberEntity, allOf(
                memberPerson(objectSelfURL(USER3.selfURL)),
                memberGroup(objectSelfURL(getSelfResourceRef(createdPlaceEntity)))));

        MemberListEntity user3MemberListEntity = jiveCoreUser3.fetchMembersByPerson(user3PersonEntity.id, new JiveCoreCountRequestOptions()).call();
        assertThat(user3MemberListEntity, listEntities(Matchers.<MemberEntity>contains(
                allOf(
                        memberPerson(objectSelfURL(USER3.selfURL)),
                        memberGroup(objectSelfURL(getSelfResourceRef(createdPlaceEntity)))))));

        MemberListEntity placeMemberListEntity = jiveCoreUser3.fetchMembersByPlace(createdPlaceEntity.placeID, new JiveCoreCountRequestOptions()).call();
        assertThat(placeMemberListEntity, listEntities(Matchers.<MemberEntity>containsInAnyOrder(
                allOf(
                        memberPerson(objectSelfURL(USER2.selfURL)),
                        memberGroup(objectSelfURL(getSelfResourceRef(createdPlaceEntity)))),

                allOf(
                        memberPerson(objectSelfURL(USER3.selfURL)),
                        memberGroup(objectSelfURL(getSelfResourceRef(createdPlaceEntity)))))));

        {
            JiveCoreContentRequestOptions options = new JiveCoreContentRequestOptions();
            options.setCount(1);

            MemberListEntity placeMemberListEntity1 = jiveCoreUser3.fetchMembersByPlace(createdPlaceEntity.placeID, options).call();
            MemberListEntity placeMemberListEntity2 = jiveCoreUser3.fetchMembers(placeMemberListEntity1.links.next).call();

            assertThat(Arrays.asList(placeMemberListEntity1, placeMemberListEntity2), containsInAnyOrder(
                    listEntities(Matchers.<MemberEntity>contains(allOf(
                            memberPerson(objectSelfURL(USER2.selfURL)),
                            memberGroup(objectSelfURL(getSelfResourceRef(createdPlaceEntity)))))),
                    listEntities(Matchers.<MemberEntity>contains(allOf(
                            memberPerson(objectSelfURL(USER3.selfURL)),
                            memberGroup(objectSelfURL(getSelfResourceRef(createdPlaceEntity))))))));
        }

        jiveCoreUser3.deleteMember(getSelfResourceRef(createdMemberEntity)).call();

        MemberListEntity postDeleteUser3MemberListEntity = jiveCoreUser3.fetchMembersByPerson(user3PersonEntity.id, new JiveCoreCountRequestOptions()).call();
        assertThat(postDeleteUser3MemberListEntity, listEntities(Matchers.<MemberEntity>emptyIterable()));

        MemberListEntity postDeletePlaceMemberListEntity = jiveCoreUser3.fetchMembersByPlace(createdPlaceEntity.placeID, new JiveCoreCountRequestOptions()).call();
        assertThat(postDeletePlaceMemberListEntity, listEntities(Matchers.<MemberEntity>containsInAnyOrder(
                allOf(
                        memberPerson(objectSelfURL(USER2.selfURL)),
                        memberGroup(objectSelfURL(getSelfResourceRef(createdPlaceEntity)))))));
    }
}
