package com.dumbster.smtp;

/**
 * Do-nothing implementation of MailStore
 */
public class NullMailStore implements MailStore {
    @Override
    public void addMessage(MailMessage message) {
    }

    @Override
    public int getEmailCount() {
        return 0;
    }

    @Override
    public MailMessage[] getMessages() {
        return new MailMessage[0];
    }

    @Override
    public MailMessage getMessage(int index) {
        return null;
    }

    @Override
    public void clearMessages() {
    }

    @Override
    public void deleteMessage(int index) {
    }
}
