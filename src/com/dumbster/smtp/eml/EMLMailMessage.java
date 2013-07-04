package com.dumbster.smtp.eml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.SmtpState;

/**
 * An implementation of MailMessage to support lazy load of messages stored in EML files.
 * <br/><br/>
 * Each message is attached to a file but won't load the file until data is requested.<br/>
 * This object is detached from the original file, so changes made to this object won't reflect to the file automatically.
 */
public class EMLMailMessage implements MailMessage {

    private static final Pattern PATTERN = Pattern.compile("(.*?): (.*)");

    private File file;
    private Map<String, List<String>> headers = new HashMap<String, List<String>>();
    private StringBuilder body = new StringBuilder();

    private boolean initialized = false;

    public EMLMailMessage(File file) {
        this.file = file;
    }

    private void checkLoaded() {
        if (!initialized) {
            loadFile();
            initialized = true;
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<String> getHeaderNames() {
        checkLoaded();
        return headers.keySet().iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getHeaderValues(String name) {
        checkLoaded();
        List<String> values = headers.get(name);
        if (values != null) {
            return values.toArray(new String[0]);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFirstHeaderValue(String name) {
        checkLoaded();
        if (headers.containsKey(name)) {
            return headers.get(name).get(0);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBody() {
        checkLoaded();
        return body.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addHeader(String name, String value) {
        List<String> values = headers.get(name);
        if (values == null) {
            values = new ArrayList<String>();
            headers.put(name, values);
        }
        values.add(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void appendHeader(String name, String value) {
        List<String> values = headers.get(name);
        if (values == null) {
            addHeader(name, value);
        } else {
            String oldValue = values.get(values.size()-1);
            values.remove(oldValue);
            values.add(oldValue + value);
            headers.put(name, values);
        }
    }

    /**
     * Adds the given text to message body
     */
    @Override
    public void appendBody(String line) {
        if (shouldPrependNewline(line)) {
            body.append("\n");
        }
        body.append(line);
    }

    private boolean shouldPrependNewline(String line) {
        return body.length() > 0 && line.length() > 0 && !"\n".equals(line);
    }

    private void loadFile() {
        try {
            Scanner scanner = new Scanner(file);
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

        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
