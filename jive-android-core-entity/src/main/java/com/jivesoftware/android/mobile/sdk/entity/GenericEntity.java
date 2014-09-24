package com.jivesoftware.android.mobile.sdk.entity;

import com.google.gson.annotations.SerializedName;

public class GenericEntity<V> {
    public V value;

    @SerializedName("jive_label")
    public String label;

    @SerializedName("jive_displayOrder")
    public Integer displayOrder;

    @SerializedName("jive_summaryDisplayOrder")
    public Integer summaryDisplayOrder;

    @SerializedName("jive_showSummaryLabel")
    public Boolean showSummaryLabel;
}
