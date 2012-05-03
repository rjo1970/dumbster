package com.dumbster.pop.action;

import com.dumbster.pop.POPState;
import com.dumbster.smtp.MailStore;
import com.dumbster.pop.Response;

public class Invalid implements Action {
    private String _message;

    public Invalid(String message) {
        _message = message;
    }

    @Override
    public String getCommand() {
        return "";
    }

    @Override
    public Response response(POPState popState, MailStore mailStore) {
        return new Response(Response.ERROR, _message, popState);
    }
}
