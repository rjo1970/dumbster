package com.dumbster.pop.action;

import com.dumbster.pop.POPState;
import com.dumbster.pop.Response;
import com.dumbster.smtp.MailStore;

public class CapabilityList implements Action {
    @Override
    public String getCommand() {
        return "CAPA";
    }

    @Override
    public Response response(POPState popState, MailStore mailStore) {
        StringBuilder sb = new StringBuilder("List of capabilities follows\r\n");
        sb.append("USER\r\n");
        sb.append("UIDL\r\n");
        sb.append("IMPLEMENTATION Dumbster POP3 v0.01\r\n.\r\n");
        return new Response(Response.OK, sb.toString(), popState);
    }
}
