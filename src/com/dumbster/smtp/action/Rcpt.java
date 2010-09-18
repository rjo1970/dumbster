package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class Rcpt implements Action {

	@Override
	public boolean isStateless() {
		return false;
	}

	@Override
	public String toString() {
		return "RCPT";
	}

	@Override
	public SmtpResponse response(SmtpState smtpState, List<MailMessage> messages, MailMessage currentMessage) {
			if (SmtpState.RCPT == smtpState) {
			return new SmtpResponse(250, "OK", smtpState);
		} else {
			return new SmtpResponse(503,
					"Bad sequence of commands: " + this, smtpState);
		}
	}

}
