package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.SmtpMessage;
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
	public SmtpResponse response(SmtpState smtpState, List<SmtpMessage> messages, SmtpMessage currentMessage) {
			return new SmtpResponse(221, "localhost Dumbster service closing transmission channel",
					SmtpState.CONNECT);
	}

}
