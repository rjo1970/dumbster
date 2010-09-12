package com.dumbster.smtp.action;

import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class NoOp extends AbstractAction {

	@Override
	public boolean isStateless() {
		return true;
	}

	@Override
	public String toString() {
		return "NOOP";
	}

	@Override
	public SmtpResponse response(SmtpState smtpState) {
		return new SmtpResponse(250, "OK", smtpState);
	}

}
