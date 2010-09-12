package com.dumbster.smtp.action;

import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class Unrecognized extends AbstractAction {

	@Override
	public boolean isStateless() {
		return false;
	}

	@Override
	public String toString() {
		return "Unrecognized command / data";
	}

	@Override
	public SmtpResponse response(SmtpState smtpState) {
		if (SmtpState.DATA_HDR == smtpState || SmtpState.DATA_BODY == smtpState) {
			return new SmtpResponse(-1, "", smtpState);
		} else {
			return new SmtpResponse(500, "Command not recognized",
					smtpState);
		}
	}

}
