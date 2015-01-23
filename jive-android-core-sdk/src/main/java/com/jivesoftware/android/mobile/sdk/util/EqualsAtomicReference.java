package com.jivesoftware.android.mobile.sdk.util;

import org.apache.http.util.LangUtils;

import javax.annotation.Nullable;
import java.util.Comparator;

public class EqualsAtomicReference<V> {
    private final Object monitor = new Object();
    @Nullable
    private final Comparator<V> comparator;
    @Nullable
    private V value;

    @SuppressWarnings("UnusedDeclaration")
    public EqualsAtomicReference() {
        this(null, null);
    }

    public EqualsAtomicReference(@Nullable V value, @Nullable Comparator<V> comparator) {
        this.comparator = comparator;
        synchronized (monitor) {
            this.value = value;
        }
    }

    @Nullable
    public V get() {
        synchronized (monitor) {
            return value;
        }
    }

    public void set(@Nullable V newValue) {
        synchronized (monitor) {
            value = newValue;
        }
    }

    public boolean compareAndSet(@Nullable V expectedValue, @Nullable V newValue) {
        synchronized (monitor) {
            boolean equals;
            if (comparator == null) {
                equals = LangUtils.equals(value, expectedValue);
            } else {
                int comparison = comparator.compare(value, expectedValue);
                equals = (comparison == 0);
            }
            if (equals) {
                value = newValue;
                return true;
            } else {
                return false;
            }
        }
    }

    @Nullable
    public V getAndSet(@Nullable V newValue) {
        V oldValue;
        synchronized (monitor) {
            oldValue = value;
            value = newValue;
        }
        return oldValue;
    }
}
