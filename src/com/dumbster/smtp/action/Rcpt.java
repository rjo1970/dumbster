package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.Response;
import com.dumbster.smtp.SmtpState;

public class Rcpt implements Action {

	@Override
	public String toString() {
		return "RCPT";
	}

	public Response response(SmtpState smtpState, List<MailMessage> messages, MailMessage currentMessage) {
			if (SmtpState.RCPT == smtpState) {
			return new Response(250, "OK", smtpState);
		} else {
			return new Response(503,
					"Bad sequence of commands: " + this, smtpState);
		}
	}

}
