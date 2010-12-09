package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.Response;
import com.dumbster.smtp.SmtpState;

public class Vrfy implements Action {

	@Override
	public String toString() {
		return "VRFY";
	}

	public Response response(SmtpState smtpState, List<MailMessage> messages, MailMessage currentMessage) {
		return new Response(252, "Not supported", smtpState);
	}

}
