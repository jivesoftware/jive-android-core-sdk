package com.jivesoftware.android.mobile.sdk.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;

public abstract class NonnullComparator<T> implements Comparator<T> {
    @Override
    public int compare(@Nullable T lhs, @Nullable T rhs) {
        if (lhs == null) {
            if (rhs == null) {
                return 0;
            } else {
                return -1;
            }
        } else if (rhs == null) {
            return 1;
        } else {
            int comparison = compareNonNull(lhs, rhs);
            return comparison;
        }
    }

    public abstract int compareNonNull(@Nonnull T lhs, @Nonnull T rhs);
}
