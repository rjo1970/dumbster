package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.SmtpMessage;
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
	public SmtpResponse response(SmtpState smtpState, List<SmtpMessage> messages, SmtpMessage currentMessage) {
		if (SmtpState.DATA_HDR == smtpState || SmtpState.DATA_BODY == smtpState) {
			return new SmtpResponse(-1, "", smtpState);
		} else {
			return new SmtpResponse(500, "Command not recognized",
					smtpState);
		}
	}

}
