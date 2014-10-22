package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.regex.Pattern;

/**
 * An entity descriptor tuple.
 */
@ParametersAreNonnullByDefault
public class JiveCoreEntityDescriptor {

    private static final Pattern FORM = Pattern.compile("\\d+,\\d+");

    @Nonnull
    private final String representation;

    @JsonCreator
    public JiveCoreEntityDescriptor(String stringForm) {
        if (!FORM.matcher(stringForm).matches()) {
            throw(new IllegalArgumentException(
                    "String supplied '" + stringForm + "' does not satisfy pattern " + FORM.toString()));
        }
        representation = stringForm;
    }

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
