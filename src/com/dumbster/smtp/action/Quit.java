package com.dumbster.smtp.action;

import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class Quit extends AbstractAction {

	@Override
	public boolean isStateless() {
		return false;
	}

	@Override
	public String toString() {
		return "QUIT";
	}

	@Override
	public SmtpResponse response(SmtpState smtpState) {
		if (SmtpState.QUIT == smtpState) {
			return new SmtpResponse(221, "localhost Dumbster service closing transmission channel",
					SmtpState.CONNECT);
		} else {
			return new SmtpResponse(503, "Bad sequence of commands: " + this, smtpState);
		}
	}

}
