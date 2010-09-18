package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class Connect implements Action {

	public boolean isStateless() {
		return false;
	}

	public String toString() {
		return "Connect";
	}

	public SmtpResponse response(SmtpState smtpState, List<MailMessage> messages, MailMessage currentMessage) {
		if (SmtpState.CONNECT == smtpState) {
			return new SmtpResponse(220,
					"localhost Dumbster SMTP service ready", SmtpState.GREET);
		} else {
			return new SmtpResponse(503, "Bad sequence of commands: " + this,
					smtpState);
		}
	}

}
