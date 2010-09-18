package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class Ehlo implements Action {

	public boolean isStateless() {
		return false;
	}

	public String toString() {
		return "EHLO";
	}

	@Override
	public SmtpResponse response(SmtpState smtpState, List<MailMessage> messages, MailMessage currentMessage) {
		if (SmtpState.GREET == smtpState) {
			return new SmtpResponse(250, "OK", SmtpState.MAIL);
		} else {
			return new SmtpResponse(503, "Bad sequence of commands: "
					+ this, smtpState);
		}
	}

}
