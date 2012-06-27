package com.dumbster.smtp;

import org.junit.*;

import com.dumbster.smtp.MailMessage;

import static org.junit.Assert.*;

public class MailMessageTest {

    private MailMessage message;

    @Before
    public void setup() {
        this.message = new MailMessageImpl();
    }

    @Test
    public void testConstructor() {
        assertEquals("", message.getBody());
        assertFalse(message.getHeaderNames().hasNext());
        assertEquals("\n\n", message.toString());
    }

    @Test
    public void testAddHeader() {
        message.addHeader("foo", "bar1");
        assertEquals("bar1", message.getFirstHeaderValue("foo"));
        assertEquals("foo", message.getHeaderNames().next());
        assertEquals("foo: bar1\n\n\n", message.toString());
    }

    @Test
    public void testAppendHeader() {
        message.addHeader("foo", "bar1");
        message.appendHeader("foo", " baz2");
        assertEquals("bar1 baz2", message.getFirstHeaderValue("foo"));
    }

    @Test
    public void testLongSubjectHeader() {
        String longSubject = StringUtil.longString(500);
        message.addHeader("Subject", longSubject);
        assertEquals("Subject: "+longSubject+"\n\n\n", message.toString());
    }

    @Test
    public void testEmptyHeaderValue() {
        String[] values = message.getHeaderValues("NOT PRESENT");
        assertEquals(0, values.length);
    }

    @Test
    public void testEmptyFirstHeaderValue() {
        String value = message.getFirstHeaderValue("NOT PRESENT");
        assertEquals(null, value);
    }

    @Test
    public void testAddTwoSameHeaders() {
        message.addHeader("foo", "bar1");
        message.addHeader("foo", "bar2");
        assertEquals("bar1", message.getFirstHeaderValue("foo"));
        assertEquals("bar2", message.getHeaderValues("foo")[1]);
        assertEquals("foo: bar1\nfoo: bar2\n\n\n", message.toString());
    }

    @Test
    public void testGetHeaders() {
        message.addHeader("foo", "bar1");
        message.addHeader("foo", "bar2");
        message.addHeader("baz", "bar3");
        assertEquals("bar1", message.getFirstHeaderValue("foo"));
        assertEquals("bar2", message.getHeaderValues("foo")[1]);
        assertEquals("bar3", message.getFirstHeaderValue("baz"));
        assertEquals(1, message.getHeaderValues("baz").length);
    }

    @Test
    public void testAppendBody() {
        message.appendBody("Should I have shut the server down before disconnecting the power?");
        assertEquals(
                "\nShould I have shut the server down before disconnecting the power?\n",
                message.toString());
    }

    @Test
    public void testAppendBodyKeepsNewlines() {
        message.appendBody("First line of text.\n");
        message.appendBody("Now what should happen?\nShould this still work?\n");
        message.appendBody("\n");
        message.appendBody("");
        assertEquals("\nFirst line of text.\n\nNow what should happen?\nShould this still work?\n\n\n", message.toString());
    }

    @Test
    public void headersAndBody() {
        message.addHeader("foo", "bar1");
        message.addHeader("foo", "bar2");
        message.appendBody("Should I have shut the server down before disconnecting the power?");
        assertEquals(
                "foo: bar1\nfoo: bar2\n\nShould I have shut the server down before disconnecting the power?\n",
                message.toString());
    }

}
