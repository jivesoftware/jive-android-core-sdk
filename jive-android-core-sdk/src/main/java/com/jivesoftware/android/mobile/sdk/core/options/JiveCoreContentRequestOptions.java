package com.jivesoftware.android.mobile.sdk.core.options;

import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import com.jivesoftware.android.mobile.sdk.util.Joiner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben.oberkfell on 4/22/14.
 */
public class JiveCoreContentRequestOptions extends JiveCoreSearchTypesRequestOptions {
    public enum ContentRelationship {
        RELATIONSHIP_FOLLOWING("following"),
        RELATIONSHIP_PARTICIPATED("participated"),
        RELATIONSHIP_RECENTLY_VIEWED("recentlyviewed");


        private String relationship;

        public String toString() {
            return relationship;
        }

        ContentRelationship(String relationship) {
            this.relationship = relationship;
        }
    };

    private ContentRelationship relationship;
    private List<PersonEntity> authors;

    public ContentRelationship getContentRelationship() {
        return relationship;
    }

    public void setContentRelationship(ContentRelationship relationship) {
        this.relationship = relationship;
    }

    public List<PersonEntity> getAuthors() { return authors; }

    public void setAuthors(List<PersonEntity> authors) {
        this.authors = authors;
    }

    @Override
    protected ArrayList<String> copyFilters() {
        ArrayList<String> filters = super.copyFilters();

        if (relationship != null) {
            filters.add("relationship("+relationship.toString()+")");
        }

        if ((authors != null) && !authors.isEmpty()) {
            List<String> authorUris = new ArrayList<String>();
            for ( PersonEntity person : authors) {
               authorUris.add(person.resources.get("self").ref);
            }

            StringBuilder joinedAuthorUris = new StringBuilder("author(");
            Joiner.on(",").appendTo(joinedAuthorUris, authorUris);
            joinedAuthorUris.append(')');
            filters.add(joinedAuthorUris.toString());
        }

        return filters;
    }

}
