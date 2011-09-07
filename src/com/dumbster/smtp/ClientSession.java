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
    private String lastHeaderName = null;


    public ClientSession(IOSource socket, MailStore mailStore) {
        this.socket = socket;
        this.mailStore = mailStore;
        this.msg = new MailMessageImpl();
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

    private void sessionLoop() throws IOException {
        while (smtpState != SmtpState.CONNECT && readNextLineReady()) {
            Request request = Request.createRequest(smtpState, line);
            smtpResponse = request.execute(mailStore, msg);
            storeInputInMessage(request);
            sendResponse();
            updateSmtpState();
            saveAndRefreshMessageIfComplete();
        }
    }

    private boolean readNextLineReady() throws IOException {
        readLine();
        return line != null;
    }

    private void readLine() throws IOException {
        line = input.readLine();
    }

    private void saveAndRefreshMessageIfComplete() {
        if (smtpState == SmtpState.QUIT) {
            mailStore.addMessage(msg);
            msg = new MailMessageImpl();
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
            return;
        }
    }

    private void addDataHeader(String params) {
        int headerNameEnd = params.indexOf(':');
        if (headerNameEnd > 0 && !whiteSpacedLineStart(params)) {
            lastHeaderName = params.substring(0, headerNameEnd).trim();
            String value = params.substring(headerNameEnd + 1).trim();
            msg.addHeader(lastHeaderName, value);
        } else if (whiteSpacedLineStart(params) && lastHeaderName != null) {
            msg.appendHeader(lastHeaderName, params);
        }
    }

    private boolean whiteSpacedLineStart(String s)  {
        if (s == null || "".equals(s))
          return false;
        char c = s.charAt(0);
        return c == 32 || c == 0x0b || c == '\n' ||
                c == '\r' || c == '\t' || c == '\f';
    }

}
