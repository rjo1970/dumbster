package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.Response;
import com.dumbster.smtp.SmtpState;

public class Unrecognized implements Action {

	public boolean isStateless() {
		return false;
	}

	@Override
	public String toString() {
		return "Unrecognized command / data";
	}

	public Response response(SmtpState smtpState, List<MailMessage> messages, MailMessage currentMessage) {
		if (SmtpState.DATA_HDR == smtpState || SmtpState.DATA_BODY == smtpState) {
			return new Response(-1, "", smtpState);
		} else {
			return new Response(500, "Command not recognized",
					smtpState);
		}
	}

}
