package com.dumbster.smtp.eml;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;

public class EMLMailMessageTest {

    private EMLMailMessage message;
    /*
        Example From http://en.wikipedia.org/wiki/Simple_Mail_Transfer_Protocol
    */
    private String text =
            "From: \"Bob Example\" <bob@example.org>\n" +
                    "To: \"Alice Example\" <alice@example.com>\n" +
                    "Cc: theboss@example.com\n" +
                    "Date: Tue, 15 January 2008 16:02:43 -0500\n" +
                    "Subject: Test message\n" +
                    "\n" +
                    "Hello Alice.\n" +
                    "This is a test message with 5 header fields and 4 lines in the message body.\n" +
                    "Your friend,\n" +
                    "Bob\n";
    @Test
    public void testReadHeaders() {
        message = new EMLMailMessage(new ByteArrayInputStream(text.getBytes()));
        String[] from = message.getHeaderValues("From");
        assertEquals(1, from.length);
        assertEquals("\"Bob Example\" <bob@example.org>", from[0]);
        assertEquals(1, message.getHeaderValues("To").length);
    }

    @Test
    public void testGetBody() {
        message = new EMLMailMessage(new ByteArrayInputStream(text.getBytes()));
        assertEquals("Hello Alice.\n" +
                "This is a test message with 5 header fields and 4 lines in the message body.\n" +
                "Your friend,\n" +
                "Bob", message.getBody());
    }
}
