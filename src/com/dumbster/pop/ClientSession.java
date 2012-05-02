package com.dumbster.pop;

import java.io.IOException;

import com.dumbster.smtp.IOSource;
import com.dumbster.smtp.MailStore;
import com.dumbster.util.AbstractSession;

public class ClientSession extends AbstractSession {

    private Response _response;
    private POPState _popState = POPState.AUTHORIZATION;

    public ClientSession(IOSource socket, MailStore mailStore) {
        super(mailStore, socket);
        Request req = Request.initialRequest();
        _response = req.execute(mailStore);
    }

    @Override
    protected void prepareSessionLoop() throws IOException {
        prepareOutput();
        prepareInput();
        sendResponse();
        updatePOPState();
    }

    private void sendResponse() {
        StringBuilder sb = new StringBuilder(_response.getCode()).append(" ").append(_response.getMessage());
        String line = sb.toString();
        if (line.endsWith("\r\n")) {
            getOutput().print(sb.toString());
        } else {
            getOutput().print(sb.toString());
            getOutput().print("\r\n");
        }
        getOutput().flush();
    }

    private void updatePOPState() {
        _popState = _response.getNextState();
    }

    @Override
    protected void sessionLoop() throws IOException {
        String line;
        do {
            line = getInput().readLine();
            Request request = Request.createRequest(_popState, line);
            _response = request.execute(getMailStore());
            sendResponse();
            updatePOPState();
        } while (_popState != null && line != null);
    }
}
