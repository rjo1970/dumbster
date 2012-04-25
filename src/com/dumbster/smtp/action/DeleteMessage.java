package com.dumbster.smtp.action;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.MailStore;
import com.dumbster.smtp.Response;
import com.dumbster.smtp.SmtpState;

public class DeleteMessage implements Action {

    private Integer messageIndex = null;

    public DeleteMessage(String params) {
        try {
            Integer messageIndex = Integer.valueOf(params);
            if (messageIndex.intValue() > -1)
                this.messageIndex = messageIndex;
        } catch (NumberFormatException ignored) {
        }
    }

    @Override
    public String toString() {
        return "DLMS";
    }

    @Override
    public Response response(SmtpState smtpState, MailStore mailStore, MailMessage currentMessage) {
        if (messageIndex != null) {
            mailStore.deleteMessage(messageIndex.intValue());
        }
        return new Response(250, "message deleted", SmtpState.GREET);
    }
}
