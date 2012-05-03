package com.dumbster.pop.action;

import com.dumbster.pop.POPState;
import com.dumbster.pop.Response;
import com.dumbster.smtp.MailStore;

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
