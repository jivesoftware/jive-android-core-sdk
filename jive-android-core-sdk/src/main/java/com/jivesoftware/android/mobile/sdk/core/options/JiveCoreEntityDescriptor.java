package com.jivesoftware.android.mobile.sdk.core.options;

import javax.annotation.Nonnull;

public class JiveCoreEntityDescriptor {

    @Nonnull
    private final String representation;

    public JiveCoreEntityDescriptor(int objectType, long objectId) {
        representation = objectType + "," + objectId;
    }

    @Override
    public String toString() {
        return representation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JiveCoreEntityDescriptor that = (JiveCoreEntityDescriptor) o;
        return representation.equals(that.representation);

    }

    @Override
    public int hashCode() {
        return representation.hashCode();
    }

}
