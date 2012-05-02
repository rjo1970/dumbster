package com.dumbster.pop.action;

import com.dumbster.pop.POPState;
import com.dumbster.pop.Response;
import com.dumbster.smtp.MailStore;

public class DeleteMessage implements Action {

    private Integer _messageIndex = null;

    public DeleteMessage(String params) {
        try {
            Integer mi = Integer.valueOf(params);
            if (_messageIndex.intValue() > -1)
                _messageIndex = mi;
        } catch (NumberFormatException ignored) {
        }
    }

    @Override
    public String getCommand() {
        return "DELE";
    }

    @Override
    public Response response(POPState popState, MailStore mailStore) {
        if (_messageIndex != null) {
            mailStore.deleteMessage(_messageIndex.intValue());
        }
        return new Response(Response.OK, "message deleted", POPState.TRANSACTION);
    }
}
