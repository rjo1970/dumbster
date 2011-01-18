package com.dumbster.smtp;

import java.util.Iterator;

public interface IMailMessage {

    public abstract Iterator<String> getHeaderNames();

    public abstract String[] getHeaderValues(String name);

    public abstract String getFirstHeaderValue(String name);

    public abstract String getBody();

    public abstract void addHeader(String name, String value);

    public abstract void appendBody(String line);

}