package com.dumbster.smtp.action;

import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class Mail extends AbstractAction {

	@Override
	public boolean isStateless() {
		return false;
	}

	@Override
	public String toString() {
		return "MAIL";
	}

	@Override
	public SmtpResponse response(SmtpState smtpState) {
			if (SmtpState.MAIL == smtpState || SmtpState.QUIT == smtpState) {
			return new SmtpResponse(250, "OK", SmtpState.RCPT);
		} else {
			return new SmtpResponse(503,
					"Bad sequence of commands: " + this, smtpState);
		}
	}

}
