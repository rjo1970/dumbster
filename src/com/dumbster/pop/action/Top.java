package com.dumbster.pop.action;

import com.dumbster.pop.POPState;
import com.dumbster.pop.Response;
import com.dumbster.smtp.MailStore;

/**
 * This is optional and also requires figuring out byte stuffing.
 * Once {@link Retrieve} works, this should be a piece of cake.
 */
public class Top implements Action {
    @Override
    public String getCommand() {
        return "TOP";
    }

    @Override
    public Response response(POPState popState, MailStore mailStore) {
        return new Quit().response(popState, mailStore);
    }
}
