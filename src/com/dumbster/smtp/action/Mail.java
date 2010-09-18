package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.Response;
import com.dumbster.smtp.SmtpState;

public class Mail implements Action {

	@Override
	public boolean isStateless() {
		return false;
	}

	@Override
	public String toString() {
		return "MAIL";
	}

	@Override
	public Response response(SmtpState smtpState, List<MailMessage> messages, MailMessage currentMessage) {
			if (SmtpState.MAIL == smtpState || SmtpState.QUIT == smtpState) {
			return new Response(250, "OK", SmtpState.RCPT);
		} else {
			return new Response(503,
					"Bad sequence of commands: " + this, smtpState);
		}
	}

}
