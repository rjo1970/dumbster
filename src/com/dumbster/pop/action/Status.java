package com.dumbster.pop.action;

import com.dumbster.pop.POPState;
import com.dumbster.pop.Response;
import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.MailStore;

public class Status implements Action {
    @Override
    public String getCommand() {
        return "STAT";
    }

    @Override
    public Response response(POPState popState, MailStore mailStore) {
        MailMessage [] maildrop = mailStore.getMessages();
        long bytes = 0;
        for (MailMessage msg : maildrop) {
            bytes += msg.toString().getBytes().length;
        }
        return new Response(Response.OK, maildrop.length + " " + bytes, POPState.TRANSACTION);
    }
}
