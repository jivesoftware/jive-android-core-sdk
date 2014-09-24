package com.jivesoftware.android.mobile.sdk.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@ParametersAreNonnullByDefault
public class EntityUtils {
    @Nullable
    public static String getResourceRef(@Nullable JiveObjectEntity entity, String name) {
        if (entity == null) {
            return null;
        } else if (entity.resources == null) {
            return null;
        } else {
            ResourceEntity resource = entity.resources.get(name);
            if (resource == null) {
                return null;
            } else {
                return resource.ref;
            }
        }
    }

    @Nonnull
    public static String getSelfResourceRef(@Nullable JiveObjectEntity entity) {
        String selfResourceRef = getResourceRef(entity, "self");
        if (selfResourceRef == null) {
            throw new NullPointerException();
        }
        return selfResourceRef;
    }

    @Nonnull
    public static Map<String, ResourceEntity> createResources(String name, String ref) {
        HashSet<String> allowedMethods = new HashSet<String>();
        allowedMethods.add("GET");

        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.allowedMethods = allowedMethods;
        resourceEntity.ref = ref;

        HashMap<String, ResourceEntity> resources = new HashMap<String, ResourceEntity>();
        resources.put(name, resourceEntity);

        return resources;
    }

    @Nonnull
    public static Map<String, ResourceEntity> createSelfResources(String ref) {
        return createResources("self", ref);
    }
}
