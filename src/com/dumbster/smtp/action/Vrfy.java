package com.dumbster.smtp.action;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.MailStore;
import com.dumbster.smtp.Response;
import com.dumbster.smtp.SmtpState;

public class Vrfy implements Action {

    @Override
    public String toString() {
        return "VRFY";
    }

    public Response response(SmtpState smtpState, MailStore mailStore, MailMessage currentMessage) {
        return new Response(252, "Not supported", smtpState);
    }

}
