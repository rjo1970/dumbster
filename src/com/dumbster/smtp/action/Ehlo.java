package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.Response;
import com.dumbster.smtp.SmtpState;

public class Ehlo implements Action {

	public boolean isStateless() {
		return false;
	}

	public String toString() {
		return "EHLO";
	}

	@Override
	public Response response(SmtpState smtpState, List<MailMessage> messages, MailMessage currentMessage) {
		if (SmtpState.GREET == smtpState) {
			return new Response(250, "OK", SmtpState.MAIL);
		} else {
			return new Response(503, "Bad sequence of commands: "
					+ this, smtpState);
		}
	}

}
