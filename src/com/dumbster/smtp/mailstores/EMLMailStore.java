package com.dumbster.smtp.mailstores;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.MailStore;
import com.dumbster.smtp.eml.EMLMailMessage;

/**
 * Store messages as EML files.
 * <br/>This class makes no guarantees as to the order of the received messages.
 * The messages are stored in order but getMessages won't return messages in the same order they were received.
 */
public class EMLMailStore implements MailStore {

    private boolean initialized;
    private int count = 0;
    private File directory = new File("eml_store");
    private List<MailMessage> messages = new ArrayList<MailMessage>();

    /**
     * Checks if mail mailStore is initialized and initializes it if it's not.
     */
    private void checkInitialized() {
        if (!initialized) {
            if (!directory.exists()) {
                directory.mkdirs();
            } else {
                loadMessages();
            }
            initialized = true;
        }
    }

    /**
     * Load previous messages from directory.
     */
    private void loadMessages() {
        File[] files = loadMessageFiles();

        for (File file : files) {
            MailMessage message = new EMLMailMessage(file);
            messages.add(message);
        }
        count = files.length;
    }

    /**
     * Load message files from mailStore directory.
     * @return an array of {@code File}
     */
    private File[] loadMessageFiles() {
        File[] files = this.directory.listFiles(new EMLFilenameFilter());
        if (files == null) {
            System.err.println("Unable to load messages from eml mailStore directory: " + directory);
            return new File[0];
        }
        return files;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEmailCount() {
        checkInitialized();
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMessage(MailMessage message) {
        checkInitialized();
        count++;
        messages.add(message);

        System.out.println("Received message: " + count);

        try {
            if (!directory.exists()) {
                System.out.println("Directory created: " + directory);
                directory.mkdirs();
            }
            String filename = getFilename(message, count);
            File file = new File(directory, filename);
            FileWriter writer = new FileWriter(file);

            for (Iterator<String> i = message.getHeaderNames(); i.hasNext();) {
                String name = i.next();
                String[] values = message.getHeaderValues(name);
                for (String value : values) {
                    writer.append(name);
                    writer.append(": ");
                    writer.append(value);
                    writer.append('\n');
                }
            }
            writer.append('\n');
            writer.append(message.getBody());
            writer.append('\n');

            writer.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public String getFilename(MailMessage message, int count) {
        String filename = new StringBuilder().append(count).append("_")
                .append(message.getFirstHeaderValue("Subject"))
                .append(".eml").toString();
        filename = filename.replaceAll("[\\\\/<>\\?>\\*\"\\|]", "_");
        return filename;
    }

    /**
     * Return a list of messages stored by this mail mailStore.
     * @return a list of {@code EMLMailMessage}
     */
    @Override
    public MailMessage[] getMessages() {
        checkInitialized();

        return messages.toArray(new MailMessage[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MailMessage getMessage(int index) {
        return getMessages()[index];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearMessages() {
        for (File file : this.directory.listFiles(new EMLFilenameFilter())) {
            file.delete();
            count--;
        }
        messages.clear();
    }

    public void setDirectory(String directory) {
        setDirectory(new File(directory));
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    /**
     * Filter only files matching name of files saved by EMLMailStore.
     */
    public static class EMLFilenameFilter implements FilenameFilter {
        private final Pattern PATTERN = Pattern.compile("\\d+_.*\\.eml");
        private final Matcher MATCHER = PATTERN.matcher("");

        @Override
        public boolean accept(File dir, String name) {
            MATCHER.reset(name);
            return MATCHER.matches();
        }

    }
}
