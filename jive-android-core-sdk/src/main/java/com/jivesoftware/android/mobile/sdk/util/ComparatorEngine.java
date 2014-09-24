package com.jivesoftware.android.mobile.sdk.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;

public class ComparatorEngine {
    private int comparison;
    public <T> int compare(@Nonnull Comparator<T> comparator, @Nullable T lhs, @Nullable T rhs) {
        if (comparison == 0) {
            comparison = comparator.compare(lhs, rhs);
        }
        return comparison;
    }

    public <T> ComparatorEngine compareAnd(@Nonnull Comparator<T> comparator, @Nullable T lhs, @Nullable T rhs) {
        compare(comparator, lhs, rhs);
        return this;
    }
}
