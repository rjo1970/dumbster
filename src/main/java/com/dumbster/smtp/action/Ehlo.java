package com.dumbster.smtp.action;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.MailStore;
import com.dumbster.smtp.Response;
import com.dumbster.smtp.SmtpState;

public class Ehlo implements Action {

    public String toString() {
        return "EHLO";
    }

    public Response response(SmtpState smtpState, MailStore mailStore, MailMessage currentMessage) {
        if (SmtpState.GREET == smtpState) {
            return new Response(250, "OK", SmtpState.MAIL);
        } else {
            return new Response(503, "Bad sequence of commands: "
                    + this, smtpState);
        }
    }

}
