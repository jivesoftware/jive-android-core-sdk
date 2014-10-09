package com.jivesoftware.android.mobile.sdk.core.options;

import com.jivesoftware.android.mobile.sdk.util.DateFormatUtil;
import com.jivesoftware.android.mobile.sdk.util.Joiner;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JiveCoreRequestOptionsImpl implements JiveCoreRequestOptions {

    private static final Pattern ESCAPE_PATTERN = Pattern.compile("[,\\(\\)\\\\]");

    private final Map<String, List<String>> filters = new HashMap<String, List<String>>();
    private final Map<String, List<String>> queryParameters = new HashMap<String, List<String>>();

    @Override
    public Map<String, List<String>> copyQueryParameters() {
        if (filters.isEmpty()) {
            queryParameters.remove("filter");
        } else {
            queryParameters.put("filter", buildFilterValues());
        }
        return Collections.unmodifiableMap(queryParameters);
    }

    public JiveCoreRequestOptionsImpl setEntityDescriptorFilter(int objectType, long objectId) {
        JiveCoreEntityDescriptor entityDescriptor = new JiveCoreEntityDescriptor(objectType, objectId);
        filters.put("entityDescriptor", Collections.singletonList(entityDescriptor.toString()));
        return this;
    }

    public JiveCoreRequestOptionsImpl setEntityDescriptorFilter(JiveCoreEntityDescriptor ... entityDescriptors) {
        ArrayList<String> list = new ArrayList<String>(entityDescriptors.length);
        for (JiveCoreEntityDescriptor entityDescriptor : entityDescriptors) {
            list.add(entityDescriptor.toString());
        }
        filters.put("entityDescriptor", list);
        return this;
    }

    public JiveCoreRequestOptionsImpl setRelationshipFilter(JiveCoreRelationshipValue relationship) {
        filters.put("relationship", Collections.singletonList(relationship.name()));
        return this;
    }

    public JiveCoreRequestOptionsImpl setSearchTerms(List<String> searchTerms) {
        filters.put("search", toEscapedList(searchTerms));
        return this;
    }

    public JiveCoreRequestOptionsImpl setTagFilter(List<String> tags) {
        filters.put("tag", toEscapedList(tags));
        return this;
    }

    public JiveCoreRequestOptionsImpl setTypeFilter(Iterable<? extends JiveCorePlaceTypeValue> types) {
        filters.put("type", toEscapedList(types));
        return this;
    }

    public JiveCoreRequestOptionsImpl setOrder(JiveCoreSortValue sortOrder) {
        queryParameters.put("sort", Collections.singletonList(sortOrder.name()));
        return this;
    }

    public JiveCoreRequestOptionsImpl setPublished(Date date) {
        queryParameters.put("published", Collections.singletonList(toFormattedDate(date)));
        return this;
    }

    public JiveCoreRequestOptionsImpl setUpdated(Date date) {
        queryParameters.put("updated", Collections.singletonList(toFormattedDate(date)));
        return this;
    }

    public JiveCoreRequestOptionsImpl setBefore(Date date) {
        queryParameters.put("before", Collections.singletonList(toFormattedDate(date)));
        queryParameters.remove("after");
        return this;
    }

    public JiveCoreRequestOptionsImpl setAfter(Date date) {
        queryParameters.remove("before");
        queryParameters.put("after", Collections.singletonList(toFormattedDate(date)));
        return this;
    }

    public JiveCoreRequestOptionsImpl setReturnedFields(Iterable<String> fieldNames) {
        queryParameters.put("fields", toEscapedList(fieldNames));
        return this;
    }

    public JiveCoreRequestOptionsImpl setAutoCategorize(boolean flag) {
        queryParameters.put("autoCategorize", Collections.singletonList(Boolean.toString(flag)));
        return this;
    }

    public JiveCoreRequestOptionsImpl setCollapse(boolean flag) {
        queryParameters.put("collapse", Collections.singletonList(Boolean.toString(flag)));
        return this;
    }

    public JiveCoreRequestOptionsImpl setOldestUnread(boolean flag) {
        queryParameters.put("oldestUnread", Collections.singletonList(Boolean.toString(flag)));
        return this;
    }

    public JiveCoreRequestOptionsImpl setAbridged(boolean flag) {
        queryParameters.put("abridged", Collections.singletonList(Boolean.toString(flag)));
        return this;
    }

    public JiveCoreRequestOptionsImpl setActiveOnly(boolean flag) {
        queryParameters.put("activeOnly", Collections.singletonList(Boolean.toString(flag)));
        return this;
    }

    public JiveCoreRequestOptionsImpl setCount(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Invalid count: " + count);
        }
        queryParameters.put("count", Collections.singletonList(Integer.toString(count)));
        return this;
    }

    public JiveCoreRequestOptionsImpl setStartIndex(int value) {
        queryParameters.put("startIndex", Collections.singletonList(Integer.toString(value)));
        return this;
    }

    private List<String> buildFilterValues() {
        ArrayList<String> result = new ArrayList<String>();
        for (Map.Entry<String, List<String>> entry : filters.entrySet()) {
            String key = entry.getKey();
            String value = Joiner.on(",").join(entry.getValue());
            result.add(key + "(" + value + ")");
        }
        return result;
    }

    private List<String> toEscapedList(Iterable<?> values) {
        List<String> result = new ArrayList<String>();
        for (Object object : values) {
            String unescaped = object.toString();
            Matcher matcher = ESCAPE_PATTERN.matcher(unescaped);
            String escaped = matcher.replaceAll("\\\\1");
            result.add(escaped);
        }
        return result;
    }

    private String toFormattedDate(Date date) {
        DateFormat gmtIso8601DateFormat = DateFormatUtil.getGmtIso8601DateFormat();
        return gmtIso8601DateFormat.format(date);
    }

}
