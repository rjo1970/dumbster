package com.dumbster.smtp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RollingMailStore implements MailStore {

    private List<MailMessage> receivedMail;

    public RollingMailStore() {
        receivedMail = Collections.synchronizedList(new ArrayList<MailMessage>());
    }

    public int getEmailCount() {
        return receivedMail.size();
    }

    public void addMessage(MailMessage message) {
        receivedMail.add(message);
        if (getEmailCount() > 100) {
            receivedMail.remove(0);
        }
    }

    public MailMessage[] getMessages() {
        return receivedMail.toArray(new MailMessage[receivedMail.size()]);
    }

    public MailMessage getMessage(int index) {
        return receivedMail.get(index);
    }

    @Override
    public void clearMessages() {
        this.receivedMail.clear();
    }

    @Override
    public void deleteMessage(int index) {
        try {
            receivedMail.remove(index);
        } catch (IndexOutOfBoundsException iob) {
            // oh, well
        } catch (UnsupportedOperationException uo) {
            // that's kind of surprising
            uo.printStackTrace(System.err);
        }
    }
}
