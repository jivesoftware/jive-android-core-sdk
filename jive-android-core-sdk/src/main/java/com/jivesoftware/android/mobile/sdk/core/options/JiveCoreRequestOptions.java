package com.jivesoftware.android.mobile.sdk.core.options;

import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import com.jivesoftware.android.mobile.sdk.util.DateFormatUtil;
import com.jivesoftware.android.mobile.sdk.util.Joiner;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * General purpose options specification.  Not all capabilities are available on a given API call.  Please consult
 * the Jive platform API documentation to identify what parameters should be used.
 */
@ParametersAreNonnullByDefault
public final class JiveCoreRequestOptions implements JiveCoreQueryParameterProvider {

    private static final Pattern ESCAPE_PATTERN = Pattern.compile("[,\\(\\)\\\\]");

    private final Map<String, List<String>> filters = new LinkedHashMap<String, List<String>>();
    private final Map<String, List<String>> directives = new LinkedHashMap<String, List<String>>();
    private final Map<String, List<String>> queryParameters = new LinkedHashMap<String, List<String>>();

    @Override
    public Map<String, List<String>> provideQueryParameters() {
        updateAllDerived();
        return Collections.unmodifiableMap(queryParameters);
    }

    @Nonnull
    public JiveCoreRequestOptions setEntityDescriptorFilter(int objectType, long objectId) {
        JiveCoreEntityDescriptor entityDescriptor = new JiveCoreEntityDescriptor(objectType, objectId);
        filters.put("entityDescriptor", Collections.singletonList(entityDescriptor.toString()));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setEntityDescriptorFilter(JiveCoreEntityDescriptor ... entityDescriptors) {
        filters.put("entityDescriptor", toUnescapedList(Arrays.asList(entityDescriptors)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setRelationshipFilter(JiveCoreRelationshipValue relationship) {
        filters.put("relationship", toEscapedList(Collections.singletonList(relationship.toString())));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setSearchTermFilter(Iterable<String> searchTerms) {
        filters.put("search", toEscapedList(searchTerms));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setTagFilter(Iterable<String> tags) {
        filters.put("tag", toEscapedList(tags));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setTypeFilter(Iterable<? extends JiveCoreTypeValue> types) {
        filters.put("type", toEscapedList(types));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setVerbFilter(Iterable<? extends JiveCoreVerbValue> verbs) {
        filters.put("verb", toEscapedList(verbs));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setAuthorFilter(Iterable<String> authors) {
        filters.put("author", toEscapedList(authors));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setPlaceFilter(Iterable<String> placeUris) {
        filters.put("place", toEscapedList(placeUris));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setEntryStateFilter(Iterable<? extends JiveCoreEntryStateValue> values) {
        filters.put("entryState", toEscapedList(values));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setUnreadFilter(boolean flag) {
        updateFlagFilter("unread", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setNotificationsFilter(boolean flag) {
        updateFlagFilter("notifications", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setDirectives(Iterable<JiveCoreDirectiveValue> directiveList) {
        for (JiveCoreDirectiveValue directive : directiveList) {
            directives.put(directive.toString(), Collections.<String>emptyList());
        }
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setCollapseSkipCollectionIdsDirective(Iterable<String> ids) {
        directives.put("collapseSkip", toEscapedList(ids));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setSort(JiveCoreSortValue sortOrder) {
        queryParameters.put("sort", Collections.singletonList(sortOrder.toString()));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setPublished(Date date) {
        queryParameters.put("published", Collections.singletonList(toFormattedDate(date)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setUpdated(Date date) {
        queryParameters.put("updated", Collections.singletonList(toFormattedDate(date)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setAfter(Date date) {
        queryParameters.remove("before");
        queryParameters.put("after", Collections.singletonList(toFormattedDate(date)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setBefore(Date date) {
        queryParameters.put("before", Collections.singletonList(toFormattedDate(date)));
        queryParameters.remove("after");
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setAuthors(Iterable<PersonEntity> authors) {
        ArrayList<String> transformed = new ArrayList<String>();
        for (PersonEntity personEntity : authors) {
            transformed.add(personEntity.resources.get("self").ref);
        }
        queryParameters.put("authors", transformed);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setFields(Iterable<String> fieldNames) {
        queryParameters.put("fields", Collections.singletonList(toJoinedString(fieldNames)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setAnchor(String uri) {
        queryParameters.put("anchor", Collections.singletonList(uri));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setAutoCategorize(boolean flag) {
        updateFlagQueryParameter("autoCategorize", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setCollapsed(boolean flag) {
        updateFlagQueryParameter("collapse", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setOldestUnread(boolean flag) {
        updateFlagQueryParameter("oldestUnread", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setAbridged(boolean flag) {
        updateFlagQueryParameter("abridged", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setActiveOnly(boolean flag) {
        updateFlagQueryParameter("activeOnly", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setExclude(boolean flag) {
        updateFlagQueryParameter("exclude", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setExcludeReplies(boolean flag) {
        updateFlagQueryParameter("excludeReplies", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setMinor(boolean flag) {
        updateFlagQueryParameter("minor", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setAuthorComment(boolean flag) {
        updateFlagQueryParameter("author", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setIsCreate(boolean flag) {
        updateFlagQueryParameter("isCreate", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setHierarchical(boolean flag) {
        updateFlagQueryParameter("hierarchical", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setIncludeBlogs(boolean flag) {
        updateFlagQueryParameter("includeBlogs", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setIncludeChildrenOutcomes(boolean flag) {
        updateFlagQueryParameter("includeChildrenOutcomes", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setSendEmails(boolean flag) {
        updateFlagQueryParameter("sendEmails", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setPreserveAspectRatio(boolean flag) {
        updateFlagQueryParameter("preserveAspectRatio", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setCount(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Invalid count: " + count);
        }
        queryParameters.put("count", Collections.singletonList(Integer.toString(count)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setStartIndex(int startIndex) {
        if (startIndex < 0) {
            throw new IllegalArgumentException("Invalid startIndex: " + startIndex);
        }
        queryParameters.put("startIndex", Collections.singletonList(Integer.toString(startIndex)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setMax(int max) {
        if (max <= 0) {
            throw new IllegalArgumentException("Invalid max: " + max);
        }
        queryParameters.put("max", Collections.singletonList(Integer.toString(max)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setWidth(int width) {
        if (width <= 0) {
            throw new IllegalArgumentException("Invalid width: " + width);
        }
        queryParameters.put("width", Collections.singletonList(Integer.toString(width)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setHeight(int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Invalid height: " + height);
        }
        queryParameters.put("height", Collections.singletonList(Integer.toString(height)));
        return this;
    }

    @Override
    @Nonnull
    public String toString() {
        updateAllDerived();
        StringBuilder builder = new StringBuilder(getClass().getSimpleName()).append("{");
        boolean entryComma = false;
        for (Map.Entry<String, List<String>> entry : queryParameters.entrySet()) {
            if (entryComma) {
                builder.append(",");
            } else {
                entryComma = true;
            }
            builder.append(entry.getKey())
                    .append("=[");
            boolean valueComma = false;
            for (String value : entry.getValue()) {
                if (valueComma) {
                    builder.append(",");
                } else {
                    valueComma = true;
                }
                builder.append(value);
            }
            builder.append("]");
        }
        builder.append("}");
        return builder.toString();
    }

    private void updateFlagFilter(String filter, boolean value) {
        if (value) {
            filters.put(filter, Collections.<String>emptyList());
        } else {
            filters.remove(filter);
        }
    }

    private void updateFlagQueryParameter(String queryParameter, boolean value) {
        queryParameters.put(queryParameter, Collections.singletonList(Boolean.toString(value)));
    }

    private void updateAllDerived() {
        updateDerived("filter", filters);
        updateDerived("directive", directives);
    }

    private void updateDerived(String queryParameter, Map<String, List<String>> map) {
        if (map.isEmpty()) {
            queryParameters.remove(queryParameter);
        } else {
            queryParameters.put(queryParameter, buildValueList(map));
        }
    }

    @Nonnull
    private static List<String> buildValueList(Map<String, List<String>> map) {
        ArrayList<String> result = new ArrayList<String>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if (value.isEmpty()) {
                result.add(key);
            } else {
                String joined = Joiner.on(",").join(value);
                result.add(key + "(" + joined + ")");
            }
        }
        return result;
    }

    @Nonnull
    private static List<String> toUnescapedList(Iterable<?> values) {
        ArrayList<String> result = new ArrayList<String>();
        for (Object item : values) {
            result.add(item.toString());
        }
        return result;
    }

    @Nonnull
    private static List<String> toEscapedList(Iterable<?> values) {
        List<String> result = new ArrayList<String>();
        for (Object value : values) {
            String escaped = escape(value);
            result.add(escaped);
        }
        return result;
    }

    @Nonnull
    private static String toJoinedString(Iterable<?> values) {
        StringBuilder builder = new StringBuilder();
        for (Object value : values) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            String escaped = escape(value);
            builder.append(escaped);
        }
        return builder.toString();
    }

    @Nonnull
    private static String escape(Object value) {
        String unescaped = value.toString();
        Matcher matcher = ESCAPE_PATTERN.matcher(unescaped);
        return matcher.replaceAll("\\\\$0");
    }

    @Nonnull
    private static String toFormattedDate(Date date) {
        DateFormat gmtIso8601DateFormat = DateFormatUtil.getGmtIso8601DateFormat();
        return gmtIso8601DateFormat.format(date);
    }

}
