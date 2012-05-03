package com.dumbster.pop.action;

import com.dumbster.pop.POPState;
import com.dumbster.pop.Response;
import com.dumbster.smtp.MailStore;

public class Password implements Action {
    @Override
    public String getCommand() {
        return "PASS";
    }

    @Override
    public Response response(POPState popState, MailStore mailStore) {
        return new Response(Response.OK, "mailbox ready", POPState.TRANSACTION);
    }
}
