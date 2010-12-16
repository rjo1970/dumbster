package com.dumbster.smtp.action;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.MailStore;
import com.dumbster.smtp.Response;
import com.dumbster.smtp.SmtpState;

public class Connect implements Action {

    public String toString() {
        return "Connect";
    }

    public Response response(SmtpState smtpState, MailStore mailStore, MailMessage currentMessage) {
        if (SmtpState.CONNECT == smtpState) {
            return new Response(220,
                    "localhost Dumbster SMTP service ready", SmtpState.GREET);
        } else {
            return new Response(503, "Bad sequence of commands: " + this,
                    smtpState);
        }
    }

}
