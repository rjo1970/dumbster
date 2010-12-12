package com.dumbster.smtp;

import java.util.List;

public interface MailStore {
    public int getEmailCount();
    public void addMessage(MailMessage message);
    public MailMessage[] getMessages();
    public MailMessage getMessage(int index);
}
