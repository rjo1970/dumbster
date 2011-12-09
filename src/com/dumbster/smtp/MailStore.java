package com.dumbster.smtp;

public interface MailStore {
    public int getEmailCount();

    public void addMessage(MailMessage message);

    public MailMessage[] getMessages();

    public MailMessage getMessage(int index);

    public void clearMessages();
}
