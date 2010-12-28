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

    public Response response(SmtpState smtpState, MailStore mailStore, MailMessage currentMessage) {

        StringBuffer result = new StringBuffer();
        if (messageIndex != null && messageIndex < mailStore.getEmailCount()) {
            result.append("\n-------------------------------------------\n");            
            result.append(mailStore.getMessage(messageIndex).toString());
        }
        result.append("There are ");
        result.append(mailStore.getEmailCount());
        result.append(" message(s).");
        return new Response(250, result.toString(), SmtpState.GREET);
    }
}
