package com.dumbster.pop.action;

import com.dumbster.pop.POPState;
import com.dumbster.pop.Response;
import com.dumbster.smtp.MailStore;

public class Connect implements Action {
    @Override
    public String getCommand() {
        return "";
    }

    @Override
    public Response response(POPState popState, MailStore mailStore) {
        if (popState == POPState.AUTHORIZATION) {
            return new Response(Response.OK, "Dumbster POP3 server ready\r\n", popState);
        }
        return new Invalid("").response(popState, mailStore);
    }
}
