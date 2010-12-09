package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.Response;
import com.dumbster.smtp.SmtpState;

public class BlankLine implements Action {


	@Override
	public String toString() {
		return "Blank line";
	}

	public Response response(SmtpState smtpState, List<MailMessage> messages, MailMessage currentMessage) {
		if (SmtpState.DATA_HDR == smtpState) {
			return new Response(-1, "", SmtpState.DATA_BODY);
		} else if (SmtpState.DATA_BODY == smtpState) {
			return new Response(-1, "", smtpState);
		} else {
			return new Response(503,
					"Bad sequence of commands: " + this, smtpState);
		}		
	}

}
