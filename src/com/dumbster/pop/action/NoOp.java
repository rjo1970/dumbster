package com.dumbster.pop.action;

import com.dumbster.pop.POPState;
import com.dumbster.pop.Response;
import com.dumbster.smtp.MailStore;

public class NoOp implements Action {
    @Override
    public String getCommand() {
        return "NOOP";
    }

    @Override
    public Response response(POPState popState, MailStore mailStore) {
        return new Response(Response.OK, "", POPState.TRANSACTION);
    }
}
