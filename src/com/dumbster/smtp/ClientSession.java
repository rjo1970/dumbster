package com.dumbster.smtp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientSession implements Runnable {

    private IOSource socket;
    private volatile MailStore mailStore;
    private MailMessage msg;
    private Response smtpResponse;
    private PrintWriter out;
    private BufferedReader input;
    private SmtpState smtpState;
    private String line;

    public ClientSession(IOSource socket, MailStore mailStore) {
        this.socket = socket;
        this.mailStore = mailStore;
        this.msg = new MailMessage();
        Request request = Request.initialRequest();
        smtpResponse = request.execute(this.mailStore, msg);
    }

    public void run() {
        try {
            prepareSessionLoop();
            sessionLoop();
        } catch (Exception ignored) {
        } finally {
            try {
                socket.close();
            } catch (Exception ignored) {
            }
        }
    }

    private void prepareSessionLoop() throws IOException {
        prepareOutput();
        prepareInput();
        sendResponse();
        updateSmtpState();
        readLine();
    }

    private void prepareOutput() throws IOException {
        out = socket.getOutputStream();
        out.flush();
    }

    private void prepareInput() throws IOException {
        input = socket.getInputStream();
    }

    private void sendResponse() {
        if (smtpResponse.getCode() > 0) {
            int code = smtpResponse.getCode();
            String message = smtpResponse.getMessage();
            out.print(code + " " + message + "\r\n");
            out.flush();
        }
    }

    private void updateSmtpState() {
        smtpState = smtpResponse.getNextState();
    }

    private void readLine() throws IOException {
        line = input.readLine();
    }

    private void sessionLoop() throws IOException {
        while (smtpState != SmtpState.CONNECT && (line != null)) {
            Request request = Request.createRequest(smtpState, line);
            smtpResponse = request.execute(mailStore, msg);
            storeInputInMessage(request);
            sendResponse();
            updateSmtpState();
            saveAndRefreshMessageIfComplete();
            readLine();
        }
    }

    private void saveAndRefreshMessageIfComplete() {
        if (smtpState == SmtpState.QUIT) {
            mailStore.addMessage(msg);
            msg = new MailMessage();
        }
    }

    private void storeInputInMessage(Request request) {
        String params = request.getParams();
        if (null == params)
            return;

        if (SmtpState.DATA_HDR.equals(smtpResponse.getNextState())) {
            addDataHeader(params);
            return;
        }

        if (SmtpState.DATA_BODY == smtpResponse.getNextState()) {
            msg.appendBody(params);
        }
    }

    private void addDataHeader(String params) {
        int headerNameEnd = params.indexOf(':');
        if (headerNameEnd > 0) {
            String name = params.substring(0, headerNameEnd).trim();
            String value = params.substring(headerNameEnd + 1).trim();
            msg.addHeader(name, value);
        }
    }

}
