package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.SmtpMessage;
import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class Connect extends AbstractAction {

	public boolean isStateless() {
		return false;
	}

	public String toString() {
		return "Connect";
	}

	public SmtpResponse response(SmtpState smtpState, List<SmtpMessage> messages, SmtpMessage currentMessage) {
		if (SmtpState.CONNECT == smtpState) {
			return new SmtpResponse(220,
					"localhost Dumbster SMTP service ready", SmtpState.GREET);
		} else {
			return new SmtpResponse(503, "Bad sequence of commands: " + this,
					smtpState);
		}
	}

}
