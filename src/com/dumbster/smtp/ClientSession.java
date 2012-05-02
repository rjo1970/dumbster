package com.dumbster.smtp;

import java.io.IOException;

import com.dumbster.util.AbstractSession;

public class ClientSession extends AbstractSession {

    private MailMessage msg;
    private Response smtpResponse;
    private SmtpState smtpState;
    private String line;
    private String lastHeaderName = null;


    public ClientSession(IOSource socket, MailStore mailStore) {
        super(mailStore, socket);
        this.msg = new MailMessageImpl();
        Request request = Request.initialRequest();
        smtpResponse = request.execute(getMailStore(), msg);
    }

    @Override
    protected void prepareSessionLoop() throws IOException {
        prepareOutput();
        prepareInput();
        sendResponse();
        updateSmtpState();
    }

    private void sendResponse() {
        if (smtpResponse.getCode() > 0) {
            int code = smtpResponse.getCode();
            String message = smtpResponse.getMessage();
            getOutput().print(code + " " + message + "\r\n");
            getOutput().flush();
        }
    }

    private void updateSmtpState() {
        smtpState = smtpResponse.getNextState();
    }

    @Override
    protected void sessionLoop() throws IOException {
        while (smtpState != SmtpState.CONNECT && readNextLineReady()) {
            Request request = Request.createRequest(smtpState, line);
            smtpResponse = request.execute(getMailStore(), msg);
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
        line = getInput().readLine();
    }

    private void saveAndRefreshMessageIfComplete() {
        if (smtpState == SmtpState.QUIT) {
            getMailStore().addMessage(msg);
            msg = new MailMessageImpl();
        }
    }

    private void storeInputInMessage(Request request) {
        final String params = request.getParams();
        if (params != null) {
            switch (smtpResponse.getNextState()) {
                case DATA_HDR:
                    addDataHeader(params);
                    break;
                case DATA_BODY:
                    msg.appendBody(params);
                    break;
            }
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
