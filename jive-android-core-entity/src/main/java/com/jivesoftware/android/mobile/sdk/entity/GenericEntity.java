package com.jivesoftware.android.mobile.sdk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@JsonSerialize(include= NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericEntity<V> {
    public V value;

    @JsonProperty("jive_label")
    public String label;

    @JsonProperty("jive_displayOrder")
    public Integer displayOrder;

    @JsonProperty("jive_summaryDisplayOrder")
    public Integer summaryDisplayOrder;

    @JsonProperty("jive_showSummaryLabel")
    public Boolean showSummaryLabel;
}
