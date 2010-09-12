package com.dumbster.smtp.action;

import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class DataEnd extends AbstractAction {

	@Override
	public boolean isStateless() {
		return false;
	}

	@Override
	public String toString() {
		return ".";
	}

	@Override
	public SmtpResponse response(SmtpState smtpState) {
		if (SmtpState.DATA_HDR == smtpState || SmtpState.DATA_BODY == smtpState) {
			return new SmtpResponse(250, "OK", SmtpState.QUIT);
		} else {
			return new SmtpResponse(503, "Bad sequence of commands: " + this, smtpState);
		}
	}

}
