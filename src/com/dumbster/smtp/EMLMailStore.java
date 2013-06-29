package com.dumbster.smtp;

/**
 * Store messages in a directory.
 * Each message will be stored in an EML file.
 * 
 * @author daniel
 */
public class EMLMailStore implements MailStore {

    private int count = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEmailCount() {
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMessage(MailMessage message) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MailMessage[] getMessages() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MailMessage getMessage(int index) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearMessages() {
        // TODO Auto-generated method stub

    }

}
