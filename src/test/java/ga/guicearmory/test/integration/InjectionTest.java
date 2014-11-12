package ga.guicearmory.test.integration;

import ga.guicearmory.gini.annotations.Property;
import ga.guicearmory.gini.util.Gini;
import ga.guicearmory.test.module.InjectionTestModule;
import ga.guicearmory.test.runner.GuiceJUnitRunner;
import ga.guicearmory.test.runner.GuiceModules;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ InjectionTestModule.class })
public class InjectionTest {

    @Inject @Property("test.key.1") int anInt;
    @Inject @Property("test.key.1") Integer boxed;
    @Inject @Property("test.key.1") long aLong;

    @Inject @Property("test.key.2") String aString;

    @Inject @Property("test.key.3") String gini;

    @Inject @Property("test.key.date") Date date;
    @Inject @Property("test.key.date") DateTime dateTime;

    @Inject @Property("test.key.date2") Date date2;
    @Inject @Property("test.key.date2") DateTime dateTime2;

    @Inject @Property("test.key.uri") URI aUri;

    @Test
    public void injectPrimitiveTest(){
        assertEquals(3, anInt);
        assertEquals(3L, aLong);
    }

    @Test
    public void injectPrimitiveWithAutoboxingTest(){
        assertEquals(new Integer(3),boxed);
    }

    @Test
    public void injectStringTest(){
        assertEquals("three",aString);
        assertEquals("gini",gini);
    }

    @Test
    public void injectDateTest() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date testDate = sdf.parse("01/01/2015");
        assertEquals(testDate,date);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date testDate2 = sdf2.parse("2014-12-30T12:08:56.111-0700");
        assertEquals(testDate2,date2);
    }

    @Test
    public void injectJodaDateTest() {
        DateTime date = new DateTime(2015,01,01,00,00);
        assertEquals(dateTime,date);

        DateTime date2 = DateTime.parse("2014-12-30T12:08:56.111-0700", DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        assertEquals(dateTime2,date2);
    }

    @Test
    public void staticGiniInjectionTest(){
        assertEquals("gini", Gini.getProperty(String.class, "test." + "key." + "3"));
    }

    @Test
    public void injectURITest() throws URISyntaxException {
        URI uri = new URI("http://stacktrace.tk");

        assertEquals(uri, aUri);
        assertNotNull(aUri.getHost());
        assertNotNull(aUri.getRawPath());
    }
}
