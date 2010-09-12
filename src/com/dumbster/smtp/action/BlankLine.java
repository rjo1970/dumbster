package com.dumbster.smtp.action;

import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class BlankLine extends AbstractAction {

	@Override
	public boolean isStateless() {
		return false;
	}

	@Override
	public String toString() {
		return "Blank line";
	}

	@Override
	public SmtpResponse response(SmtpState smtpState) {
		if (SmtpState.DATA_HDR == smtpState) {
			return new SmtpResponse(-1, "", SmtpState.DATA_BODY);
		} else if (SmtpState.DATA_BODY == smtpState) {
			return new SmtpResponse(-1, "", smtpState);
		} else {
			return new SmtpResponse(503,
					"Bad sequence of commands: " + this, smtpState);
		}		
	}

}
