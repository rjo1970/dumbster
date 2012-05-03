package com.dumbster.smtp;

import java.util.ArrayList;
import java.util.List;

/**
 * A mail store with a fixed maximum number of messages it can hold and
 * a FIFO expiration policy. This implementation is intended to be safe
 * for multithreaded access while still being reasonably fast.
 */
public class FixedSizeMailStore implements MailStore {

    private final List<MailMessage> _messages = new ArrayList<MailMessage>();
    private final int _maxSize;

    public FixedSizeMailStore(int size) {
        _maxSize = size;
    }

    @Override
    public int getEmailCount() {
        synchronized (_messages) {
            return _messages.size();
        }
    }

    @Override
    public void addMessage(MailMessage message) {
        // We have two operations, here. First, add the new message to the list; second, remove the other end of the list iff the list is too long
        // Because there might be multiple threads accessing this store, we need to synchronize *all* access to the list. *sigh*
        synchronized (_messages) {
            _messages.add(message);
            final int size = _messages.size();
            if (size > _maxSize) {
                _messages.subList(0, size-_maxSize).clear();
            }
        }
    }

    @Override
    public MailMessage[] getMessages() {
        synchronized (_messages) {
            return _messages.toArray(new MailMessage[_messages.size()]);
        }
    }

    @Override
    public MailMessage getMessage(int index) {
        MailMessage[] messages = getMessages();
        if (index > -1 && index < messages.length) {
            return messages[index];
        }
        return null;
    }

    @Override
    public void clearMessages() {
        synchronized (_messages) {
            _messages.clear();
        }
    }

    @Override
    public void deleteMessage(int index) {
        synchronized (_messages) {
            if (index > -1 && index < _messages.size()) {
                _messages.remove(index);
            }
        }
    }
}
