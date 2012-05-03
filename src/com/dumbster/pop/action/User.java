package com.dumbster.pop.action;

import com.dumbster.pop.POPState;
import com.dumbster.pop.Response;
import com.dumbster.smtp.MailStore;

/**
 * During the AUTHORIZATION phase, the client can offer a user ID.
 * In real POP3, we'd care what the user was. Since there's really only
 * one mailbox, Dumbster doesn't care.
 */
public class User implements Action {
    @Override
    public String getCommand() {
        return "USER";
    }

    @Override
    public Response response(POPState popState, MailStore mailStore) {
        if (popState == POPState.AUTHORIZATION) {
            return new Response(Response.OK, "", popState);
        }
        return (new Invalid("Not allowed in this state")).response(popState, mailStore);
    }
}
