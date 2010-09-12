package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.SmtpMessage;
import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class Expn extends AbstractAction {

	@Override
	public boolean isStateless() {
		return true;
	}

	@Override
	public String toString() {
		return "EXPN";
	}

	@Override
	public SmtpResponse response(SmtpState smtpState, List<SmtpMessage> messages, SmtpMessage currentMessage) {
		return new SmtpResponse(252, "Not supported", smtpState);
	}

}
