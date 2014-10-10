package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreRequestOptions;
import com.jivesoftware.android.mobile.sdk.entity.PlaceEntity;
import com.jivesoftware.android.mobile.sdk.entity.PlaceListEntity;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import static com.jayway.awaitility.Awaitility.await;
import static com.jivesoftware.android.mobile.sdk.entity.EntityUtils.getSelfResourceRef;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ListEntityMatchers.listEntities;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.PlaceEntityMatchers.placeName;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class JiveCoreSearchPlacesITest extends AbstractDelayedRestITest {
    private String uuid;
    private PlaceEntity createdPlaceEntity1;
    private PlaceEntity createdPlaceEntity2;

    @Before
    public void setup() throws Exception {
        super.setup();

        uuid = UUID.randomUUID().toString();

        PlaceEntity creatingPlaceEntity1 = new PlaceEntity();
        creatingPlaceEntity1.displayName = uuid + "-1";
        creatingPlaceEntity1.name = uuid + "-1";
        creatingPlaceEntity1.type = "group";
        creatingPlaceEntity1.groupType = "OPEN";

        createdPlaceEntity1 = jiveCoreUser2.createPlace(creatingPlaceEntity1).call();

        PlaceEntity creatingPlaceEntity2 = new PlaceEntity();
        creatingPlaceEntity2.displayName = uuid + "-2";
        creatingPlaceEntity2.name = uuid + "-2";
        creatingPlaceEntity2.type = "group";
        creatingPlaceEntity2.groupType = "OPEN";

        createdPlaceEntity2 = jiveCoreUser2.createPlace(creatingPlaceEntity2).call();
    }

    @After
    public void tearDown() throws Exception {
        if (createdPlaceEntity1 != null) {
            jiveCoreAdmin.deletePlace(getSelfResourceRef(createdPlaceEntity1)).call();
            createdPlaceEntity1 = null;
        }
        if (createdPlaceEntity2 != null) {
            jiveCoreAdmin.deletePlace(getSelfResourceRef(createdPlaceEntity2)).call();
            createdPlaceEntity2 = null;

        }

        super.tearDown();
    }

    @Test
    public void testSearchPlaces() throws Exception {
        await().until(new Callable<PlaceListEntity>() {
                          @Override
                          public PlaceListEntity call() throws Exception {
                              JiveCoreRequestOptions options = new JiveCoreRequestOptions();
                              options.setSearchTermFilter(Arrays.asList(uuid));

                              PlaceListEntity placeListEntity = jiveCoreAdmin.searchPlaces(options).call();
                              return placeListEntity;
                          }
                      },

                // IntelliJ thinks the below <PlaceEntity> is redundant, but javac disagrees
                listEntities(Matchers.<PlaceEntity>containsInAnyOrder(
                        placeName(createdPlaceEntity1.name),
                        placeName(createdPlaceEntity2.name))));
    }

    @Test
    public void testSearchPlacesWithNextLink() throws Exception {
        await().until(new Callable<List<PlaceListEntity>>() {
                          @Override
                          public List<PlaceListEntity> call() throws Exception {
                              JiveCoreRequestOptions options = new JiveCoreRequestOptions();
                              options.setCount(1);
                              options.setSearchTermFilter(Arrays.asList(uuid));

                              PlaceListEntity placeListEntity1 = jiveCoreAdmin.searchPlaces(options).call();
                              if ((placeListEntity1.links != null) && (placeListEntity1.links.next != null)) {
                                  PlaceListEntity placeListEntity2 = jiveCoreAdmin.searchPlaces(placeListEntity1.links.next).call();
                                  return Arrays.asList(placeListEntity1, placeListEntity2);
                              } else {
                                  return Collections.singletonList(placeListEntity1);
                              }
                          }
                      },

                // IntelliJ thinks the below <PlaceEntity> is redundant, but javac disagrees
                containsInAnyOrder(
                        listEntities(Matchers.<PlaceEntity>contains(
                                placeName(createdPlaceEntity1.name))),
                        listEntities(Matchers.<PlaceEntity>contains(
                                placeName(createdPlaceEntity2.name)))));
    }
}
