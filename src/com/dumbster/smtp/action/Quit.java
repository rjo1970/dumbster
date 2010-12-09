package com.dumbster.smtp.action;

import java.util.List;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.Response;
import com.dumbster.smtp.SmtpState;

public class Quit implements Action {

	@Override
	public String toString() {
		return "QUIT";
	}

	public Response response(SmtpState smtpState, List<MailMessage> messages, MailMessage currentMessage) {
			return new Response(221, "localhost Dumbster service closing transmission channel",
					SmtpState.CONNECT);
	}

}
