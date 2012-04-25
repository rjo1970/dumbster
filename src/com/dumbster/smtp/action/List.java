package com.dumbster.smtp.action;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.MailStore;
import com.dumbster.smtp.Response;
import com.dumbster.smtp.SmtpState;


public class List implements Action {

    private Integer messageIndex = null;

    public List(String params) {
        try {
            Integer messageIndex = Integer.valueOf(params);
            if (messageIndex > -1)
                this.messageIndex = messageIndex;
        } catch (NumberFormatException ignored) {
        }
    }

    @Override
    public String toString() {
        return "LIST";
    }

    @Override
    public Response response(SmtpState smtpState, MailStore mailStore, MailMessage currentMessage) {
        StringBuilder result = new StringBuilder();
        MailMessage[] messages = mailStore.getMessages();
        if (messageIndex != null) {
        final int ix = messageIndex.intValue();
            if (ix > 0 && ix < messages.length) {
                result.append("\n-------------------------------------------\n");
                result.append(messages[ix].toString());
            }
        }
        result.append("There are ");
        result.append(messages.length);
        result.append(" message(s).");
        return new Response(250, result.toString(), SmtpState.GREET);
    }
}
