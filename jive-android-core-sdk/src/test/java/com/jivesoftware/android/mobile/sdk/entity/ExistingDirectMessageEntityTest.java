package com.jivesoftware.android.mobile.sdk.entity;

import com.jivesoftware.android.mobile.sdk.json.JiveJson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;


/**
 * Created by mark.schisler on 3/3/14.
 */
public class ExistingDirectMessageEntityTest {

    private ContentEntity testObject;

    @Before
    public void setUp() throws Exception {
        JiveJson jiveJson = new JiveJson();
        testObject = jiveJson.fromJson(new FileInputStream("test-data/direct-message.json"), ContentEntity.class);
    }

    @Test
    public void testWhenGsonIsParsedThenItIsSuccessful() {
        Calendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));

        assertEquals(testObject.author.name.givenName, "Heath");
        assertEquals(testObject.author.name.familyName, "Borders");
        assertEquals(testObject.author.name.formatted, "Heath Borders");
        assertEquals(testObject.author.displayName, "Heath Borders");
        assertEquals(testObject.author.id, "2012");

        assertEquals(testObject.content.type, "text/html");
        assertEquals(testObject.content.text, "<body><!-- [DocumentBodyStart:718eacbe-9a31-43ac-bbaf-d5c749aaabd5] --><div class=\"jive-rendered-content\"><span>This is a real DM</span></div><!-- [DocumentBodyEnd:718eacbe-9a31-43ac-bbaf-d5c749aaabd5] --></body>");

        assertEquals(testObject.contentID, "1188");

        assertEquals(testObject.followerCount, Integer.valueOf(1));

        assertEquals(testObject.highlightBody, "This is a <em class=\"jive-hilite\">real</em> DM");
        assertEquals(testObject.highlightSubject, "This is a <em class=\"jive-hilite\">real</em> DM");

        assertEquals(testObject.parent,"http://apresian-z800.jiveland.com:8080/api/core/v3/people/2012");

        assertEquals(testObject.parentPlace.id,"2012");
        assertEquals(testObject.parentPlace.type.toString(), "person");
        assertEquals(testObject.parentPlace.name,"Heath Borders");
        assertEquals(testObject.parentPlace.uri,"http://apresian-z800.jiveland.com:8080/api/core/v3/people/2012");
        assertEquals(testObject.parentPlace.html,"http://apresian-z800.jiveland.com:8080/people/heath");

        PersonEntity[] participants = (PersonEntity[]) testObject.participants;

        assertEquals(participants[0].id,"2003");
        assertEquals(participants[0].displayName,"Dmitry Apresian");
        assertEquals(participants[0].name.formatted, "Dmitry Apresian");

        assertEquals(participants[1].id,"2015");
        assertEquals(participants[1].displayName,"Ben Oberkfell");
        assertEquals(participants[1].name.formatted, "Ben Oberkfell");

        cal.setTime(testObject.published);
        assertEquals(cal.get(Calendar.MONTH),2);
        assertEquals(cal.get(Calendar.DATE), 13);
        assertEquals(cal.get(Calendar.YEAR), 2014);
        assertEquals(cal.get(Calendar.HOUR_OF_DAY), 15);
        assertEquals(cal.get(Calendar.MINUTE), 8);
        assertEquals(cal.get(Calendar.SECOND), 0);

        assertEquals(testObject.replyCount,Integer.valueOf(1));

        assertEquals(testObject.status, "published");
        assertEquals(testObject.subject,"This is a real DM");

        assertEquals(testObject.tags.size(),0);

        assertEquals(testObject.id, "1093");

        cal.setTime(testObject.updated);
        assertEquals(cal.get(Calendar.MONTH),2);
        assertEquals(cal.get(Calendar.DATE), 13);
        assertEquals(cal.get(Calendar.YEAR), 2014);
        assertEquals(cal.get(Calendar.HOUR_OF_DAY), 15);
        assertEquals(cal.get(Calendar.MINUTE), 8);
        assertEquals(cal.get(Calendar.SECOND), 0);

        assertEquals(testObject.viewCount, Integer.valueOf(1));

        assertEquals(testObject.visibleToExternalContributors, Boolean.valueOf(false));

        assertEquals(testObject.resources.get("self").ref,"http://apresian-z800.jiveland.com:8080/api/core/v3/dms/1093");
        assertEquals(testObject.resources.get("self").allowedMethods, Sets.newSet(RequestMethod.DELETE.toString(), RequestMethod.GET.toString()));

        assertEquals(testObject.resources.get("html").ref, "http://apresian-z800.jiveland.com:8080/collaboration/1093");
        assertEquals(testObject.resources.get("html").allowedMethods, Sets.newSet(RequestMethod.GET.toString()));

        assertEquals(testObject.resources.get("comments").ref,"http://apresian-z800.jiveland.com:8080/api/core/v3/dms/1093/comments");
        assertEquals(testObject.resources.get("comments").allowedMethods, Sets.newSet(RequestMethod.POST.toString(), RequestMethod.GET.toString()));

    }

}
