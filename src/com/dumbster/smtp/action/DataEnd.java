package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.SmtpMessage;
import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;

public class DataEnd implements Action {

	@Override
	public boolean isStateless() {
		return false;
	}

	@Override
	public String toString() {
		return ".";
	}

	@Override
	public SmtpResponse response(SmtpState smtpState, List<SmtpMessage> messages, SmtpMessage currentMessage) {
		if (SmtpState.DATA_HDR == smtpState || SmtpState.DATA_BODY == smtpState) {
			return new SmtpResponse(250, "OK", SmtpState.QUIT);
		} else {
			return new SmtpResponse(503, "Bad sequence of commands: " + this, smtpState);
		}
	}

}
