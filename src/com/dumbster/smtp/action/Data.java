package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.SmtpMessage;
import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class Data implements Action {

	@Override
	public boolean isStateless() {
		return false;
	}

	@Override
	public String toString() {
		return "DATA";
	}

	@Override
	public SmtpResponse response(SmtpState smtpState, List<SmtpMessage> messages, SmtpMessage currentMessage) {
			if (SmtpState.RCPT == smtpState) {
			return new SmtpResponse(354,
					"Start mail input; end with <CRLF>.<CRLF>",
					SmtpState.DATA_HDR);
		} else {
			return new SmtpResponse(503,
					"Bad sequence of commands: " + this, smtpState);
		}
	}

}
