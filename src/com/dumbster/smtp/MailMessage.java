package com.dumbster.smtp;

import java.util.Iterator;

public interface MailMessage {

    public abstract Iterator<String> getHeaderNames();

    public abstract String[] getHeaderValues(String name);

    public abstract String getFirstHeaderValue(String name);

    public abstract String getBody();

    public abstract void addHeader(String name, String value);

    public abstract void appendHeader(String name, String value);

    public abstract void appendBody(String line);

    /**
     * Not used for the SMTP protocol but by POP3
     *
     * @return a unique identifier consisting of one to 70 characters in the range 0x21
     * to 0x7E
     */
    public abstract String getUID();

    /**
     * Used by the POP3 protocol. POP3 requires that messages be "byte stuffed" upon retrieval.
     *
     * @return the entire message, ready for POP3
     */
    public String byteStuff();

}