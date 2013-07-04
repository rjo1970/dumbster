package com.dumbster.smtp;

import java.util.Iterator;

public interface MailMessage {

    /**
     * Gets an iterator over header names.
     * @return {@code Iterator}
     */
    Iterator<String> getHeaderNames();

    /**
     * Gets the values of a given header.
     * 
     * @param name
     *            the name of the header.
     * @return the list of values associated with this header.
     */
    String[] getHeaderValues(String name);

    /**
     * A shortcut to get only the first value of a header
     *
     * @param name
     *            is name of the header
     * @return the first value of the header.
     */
    String getFirstHeaderValue(String name);

    /**
     * Returns the body of the message.
     * @return the body of the message.
     */
    String getBody();

    /**
     * Adds a header to the message
     *
     * @param name
     *            is the name of the header.
     * @param value
     *            is the value to add to the header.
     */
    void addHeader(String name, String value);

    /**
     * Append some text to the last existing value of a header.
     * It differs from {@code addHeader} method because it doesn't add a new header entry,
     * instead it appends the given value to an existing header entry. 
     * If the given header name doesn't exist this method will add a new header.
     *
     * @param name
     *            is the name of the header
     * @param value
     *            is the value to append to the header.
     */
    void appendHeader(String name, String value);

    /**
     * Appends the given text to the body.
     * The text will be added in a new line.
     *
     * @param line
     *            is the text to append.
     */
    void appendBody(String line);

}