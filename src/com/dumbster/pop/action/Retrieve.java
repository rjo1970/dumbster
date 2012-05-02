package com.dumbster.pop.action;


import com.dumbster.pop.POPState;
import com.dumbster.pop.Response;
import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.MailStore;

/**
 * The RFC says we're supposed to "byte stuff" the lines in a multi-line message.
 */
public class Retrieve implements Action {
    private final int _index;
    
    public Retrieve(String argument) {
        int ix;
        try {
            ix = Integer.parseInt(argument.trim());
        } catch (Exception ex) {
            ix = -1;
        }
        _index = ix;
    }

    @Override
    public String getCommand() {
        return "RETR";
    }

    @Override
    public Response response(POPState popState, MailStore mailStore) {
        MailMessage msg = mailStore.getMessage(_index);
        if (msg != null) {
            return new Response(Response.OK, msg.byteStuff(), POPState.TRANSACTION);
        } else {
            return new Response(Response.ERROR, "no message at index "+_index, POPState.TRANSACTION);
        }
    }

}
