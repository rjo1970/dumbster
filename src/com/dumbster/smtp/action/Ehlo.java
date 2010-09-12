package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.SmtpMessage;
import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class Ehlo extends AbstractAction {

	public boolean isStateless() {
		return false;
	}

	public String toString() {
		return "EHLO";
	}

	@Override
	public SmtpResponse response(SmtpState smtpState, List<SmtpMessage> messages, SmtpMessage currentMessage) {
		if (SmtpState.GREET == smtpState) {
			return new SmtpResponse(250, "OK", SmtpState.MAIL);
		} else {
			return new SmtpResponse(503, "Bad sequence of commands: "
					+ this, smtpState);
		}
	}

}
