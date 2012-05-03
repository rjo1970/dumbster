package com.dumbster.pop.action;

import com.dumbster.pop.POPState;
import com.dumbster.pop.Response;
import com.dumbster.smtp.MailStore;

/**
 * If this were a real POP3 server, this command would undelete any
 * messages marked for deletion. That's way too complicated. We're
 * just gonna lie and say, sure, they're undeleted.
 */
public class Reset implements Action {
    @Override
    public String getCommand() {
        return "RSET";
    }

    @Override
    public Response response(POPState popState, MailStore mailStore) {
        return new Response(Response.OK, "Dumbster doesn't really undelete", POPState.TRANSACTION);
    }
}
