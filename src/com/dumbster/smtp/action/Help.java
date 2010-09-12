package com.dumbster.smtp.action;

import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class Help extends AbstractAction {

	@Override
	public boolean isStateless() {
		return true;
	}

	@Override
	public String toString() {
		return "HELP";
	}

	@Override
	public SmtpResponse response(SmtpState smtpState) {
		return new SmtpResponse(211, "No help available", smtpState);
	}

}
