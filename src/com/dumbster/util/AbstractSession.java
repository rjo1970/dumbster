package com.dumbster.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.dumbster.smtp.IOSource;
import com.dumbster.smtp.MailStore;

public abstract class AbstractSession implements Runnable {
    private IOSource _socket;
    private volatile MailStore _mailStore;
    private PrintWriter _out;
    private BufferedReader _input;

    public AbstractSession(MailStore mailStore, IOSource socket) {
        _mailStore = mailStore;
        _socket = socket;
    }

    protected void prepareOutput() throws IOException {
        _out = _socket.getOutputStream();
        _out.flush();
    }

    protected void prepareInput() throws IOException {
        _input = _socket.getInputStream();
    }

    protected MailStore getMailStore() { return _mailStore; }
    protected PrintWriter getOutput() { return _out; }
    protected BufferedReader getInput() { return  _input; }
    protected IOSource getSocket() { return _socket; }

    protected abstract void prepareSessionLoop() throws IOException;
    protected abstract void sessionLoop() throws IOException;

    @Override
    public void run() {
        try {
            prepareSessionLoop();
            sessionLoop();
        } catch (Exception ignored) {
        } finally {
            try {
                getSocket().close();
            } catch (Exception ignored) {
            }
        }
    }
}
