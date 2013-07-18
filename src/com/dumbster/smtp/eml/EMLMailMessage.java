package com.dumbster.smtp.eml;

import java.io.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.MailMessageImpl;
import com.dumbster.smtp.SmtpState;

/**
 * An implementation of MailMessage to support lazy load of messages stored in EML files.
 * <br/><br/>
 * Each message is attached to a file but won't load the file until data is requested.<br/>
 * This object is detached from the original file, so changes made to this object won't reflect to the file automatically.
 */
public class EMLMailMessage implements MailMessage {

    private static final Pattern PATTERN = Pattern.compile("(.*?): (.*)");

    private InputStream stream;
    private MailMessage delegate = new MailMessageImpl();

    private boolean isLoaded = false;

    public EMLMailMessage(InputStream file) {
        this.stream = file;
    }

    public EMLMailMessage(File file) {
        try {
            this.stream = new FileInputStream(file);
        } catch (FileNotFoundException fnf) {
            throw new RuntimeException(fnf);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<String> getHeaderNames() {
        checkLoaded();
        return delegate.getHeaderNames();
    }

    private void checkLoaded() {
        if (!isLoaded) {
            loadFile();
            isLoaded = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getHeaderValues(String name) {
        checkLoaded();
        return delegate.getHeaderValues(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFirstHeaderValue(String name) {
        checkLoaded();
        return delegate.getFirstHeaderValue(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBody() {
        checkLoaded();
        return delegate.getBody();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addHeader(String name, String value) {
        delegate.addHeader(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void appendHeader(String name, String value) {
        delegate.appendHeader(name, value);
    }

    /**
     * Adds the given text to message body
     */
    @Override
    public void appendBody(String line) {
        delegate.appendBody(line);
    }

    private void loadFile() {
        Scanner scanner = new Scanner(stream);
        SmtpState state = SmtpState.DATA_HDR;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (state == SmtpState.DATA_HDR) {
                if (line.isEmpty()) {
                    state = SmtpState.DATA_BODY;
                    continue;
                }

                Matcher matcher = PATTERN.matcher(line);
                if (matcher.matches()) {
                    String headerName = matcher.group(1);
                    String headerValue = matcher.group(2);
                    addHeader(headerName, headerValue);
                }
            } else {
                appendBody(line);
            }
        }
        scanner.close();
    }

}
