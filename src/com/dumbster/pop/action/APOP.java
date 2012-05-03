package com.dumbster.pop.action;

import com.dumbster.pop.POPState;
import com.dumbster.pop.Response;
import com.dumbster.smtp.MailStore;

/**
 * This is an optional command to provide a slightly more secure authentication
 * method than plaintext user/password. A real POP3 server should be serious about
 * security, but Dumbster isn't intended for production.
 */
public class APOP implements Action {
    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public Response response(POPState popState, MailStore mailStore) {
        return new Response(Response.OK, "Oy yeah, you're cool.", POPState.TRANSACTION);
    }
}
