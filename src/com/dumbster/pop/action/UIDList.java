package com.dumbster.pop.action;
/**
 * File copyright 4/25/12 by sbeitzel
 */

import com.dumbster.pop.POPState;
import com.dumbster.pop.Response;
import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.MailStore;

/**
 * With an argument (a message index), respond with OK, message index, and message uuid.
 * With no argument, respond with OK, crlf, then message index and message uuid and crlf for each message.
 * If the argument isn't a valid message index, respond with ERR.
 */
public class UIDList implements Action {
    private final String _argument;
    
    public UIDList(String argument) {
        if (argument.toUpperCase().startsWith(getCommand())) {
            _argument = argument.substring(getCommand().length()).trim();
        } else {
            _argument = argument.trim();
        }
    }
    
    @Override
    public String getCommand() {
        return "UIDL";
    }

    @Override
    public Response response(POPState popState, MailStore mailStore) {
        StringBuilder sb = new StringBuilder();
        if (!_argument.isEmpty()) {
            // we have an argument
            try {
                int ix = Integer.parseInt(_argument);
                MailMessage msg = mailStore.getMessage(ix);
                if (msg != null) {
                    sb.append(ix).append(" ").append(msg.getUID()).append("\r\n.\r\n");
                    return new Response(Response.OK, sb.toString(), POPState.TRANSACTION);
                }
            } catch (Exception e) {
                return new Response(Response.ERROR, "not a valid message id", POPState.TRANSACTION);
            }
            return new Response(Response.ERROR, "no such message", POPState.TRANSACTION);
        } else {
            // we dump the whole mailstore
            sb.append("\r\n");
            MailMessage [] allMessages = mailStore.getMessages();
            for (int ix = 0; ix < allMessages.length; ix++) {
                sb.append(ix).append(" ").append(allMessages[ix].getUID()).append("\r\n");
            }
            sb.append(".\r\n");
            return new Response(Response.OK, sb.toString(), POPState.TRANSACTION);
        }
    }
}
