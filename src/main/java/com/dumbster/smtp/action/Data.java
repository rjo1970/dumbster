package com.dumbster.smtp.action;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.MailStore;
import com.dumbster.smtp.Response;
import com.dumbster.smtp.SmtpState;

public class Data implements Action {

    @Override
    public String toString() {
        return "DATA";
    }

    public Response response(SmtpState smtpState, MailStore mailStore, MailMessage currentMessage) {
        if (SmtpState.RCPT == smtpState) {
            return new Response(354,
                    "Start mail input; end with <CRLF>.<CRLF>",
                    SmtpState.DATA_HDR);
        } else {
            return new Response(503,
                    "Bad sequence of commands: " + this, smtpState);
        }
    }

}
