package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.Response;
import com.dumbster.smtp.SmtpState;

public class Expn implements Action {

	@Override
	public boolean isStateless() {
		return true;
	}

	@Override
	public String toString() {
		return "EXPN";
	}

	@Override
	public Response response(SmtpState smtpState, List<MailMessage> messages, MailMessage currentMessage) {
		return new Response(252, "Not supported", smtpState);
	}

}
