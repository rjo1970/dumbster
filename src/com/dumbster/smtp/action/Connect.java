package com.dumbster.smtp.action;

import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class Connect extends AbstractAction {

	public boolean isStateless() {
		return false;
	}

	public String toString() {
		return "Connect";
	}

	public SmtpResponse response(SmtpState smtpState) {
		if (SmtpState.CONNECT == smtpState) {
			return new SmtpResponse(220,
					"localhost Dumbster SMTP service ready", SmtpState.GREET);
		} else {
			return new SmtpResponse(503, "Bad sequence of commands: " + this,
					smtpState);
		}
	}

}
