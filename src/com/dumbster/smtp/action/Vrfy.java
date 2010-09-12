package com.dumbster.smtp.action;

import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class Vrfy extends AbstractAction {

	@Override
	public boolean isStateless() {
		return true;
	}

	@Override
	public String toString() {
		return "VRFY";
	}

	@Override
	public SmtpResponse response(SmtpState smtpState) {
		return new SmtpResponse(252, "Not supported", smtpState);
	}

}
