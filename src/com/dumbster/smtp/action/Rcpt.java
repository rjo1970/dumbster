package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.SmtpMessage;
import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class Rcpt extends AbstractAction {

	@Override
	public boolean isStateless() {
		return false;
	}

	@Override
	public String toString() {
		return "RCPT";
	}

	@Override
	public SmtpResponse response(SmtpState smtpState, List<SmtpMessage> messages, SmtpMessage currentMessage) {
			if (SmtpState.RCPT == smtpState) {
			return new SmtpResponse(250, "OK", smtpState);
		} else {
			return new SmtpResponse(503,
					"Bad sequence of commands: " + this, smtpState);
		}
	}

}
