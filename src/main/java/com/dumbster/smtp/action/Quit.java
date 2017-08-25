package com.dumbster.smtp.action;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.MailStore;
import com.dumbster.smtp.Response;
import com.dumbster.smtp.SmtpState;

public class Quit implements Action {

    @Override
    public String toString() {
        return "QUIT";
    }

    public Response response(SmtpState smtpState, MailStore mailStore, MailMessage currentMessage) {
        return new Response(221, "localhost Dumbster service closing transmission channel",
                SmtpState.CONNECT);
    }

}
