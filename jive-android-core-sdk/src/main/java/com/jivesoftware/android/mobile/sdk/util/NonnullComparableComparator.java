package com.jivesoftware.android.mobile.sdk.util;

import javax.annotation.Nonnull;

public class NonnullComparableComparator<T extends Comparable<T>> extends NonnullComparator<T> {
    @Override
    public int compareNonNull(@Nonnull T lhs, @Nonnull T rhs) {
        return lhs.compareTo(rhs);
    }
}
