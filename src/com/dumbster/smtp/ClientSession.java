package com.dumbster.smtp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.dumbster.smtp.action.Connect;

public class ClientSession implements Runnable {

	private IOSource socket;
	private MailStore mailStore;
	private MailMessage msg;
	private Response smtpResponse;

	public ClientSession(IOSource socket, MailStore mailStore) {
		this.socket = socket;
		this.mailStore = mailStore;
		msg = new MailMessage();
		Request smtpRequest = Request.initialRequest();
		smtpResponse = smtpRequest.execute(mailStore, msg);
	}

	private void sessionLoop() throws IOException {

		BufferedReader input = socket.getInputStream();
		PrintWriter out = socket.getOutputStream();
        sendResponse(out, smtpResponse);
        SmtpState smtpState = smtpResponse.getNextState();

        String line;
        while (smtpState != SmtpState.CONNECT && ((line = input.readLine()) != null)) {
			Request request = Request.createRequest(smtpState, line);
			Response response = request.execute(mailStore, msg);
			storeInputInMessage(request, response);
			sendResponse(out, response);
			smtpState = response.getNextState();
			saveAndRefreshMessageIfComplete(smtpState);
		}
	}

	private void saveAndRefreshMessageIfComplete(SmtpState smtpState) {
		if (smtpState == SmtpState.QUIT) {
			mailStore.addMessage(msg);
			msg = new MailMessage();
		}
	}

	private void storeInputInMessage(Request request, Response response) {
		String params = request.getParams();
		if (params != null) {
			if (SmtpState.DATA_HDR.equals(response.getNextState())) {
				int headerNameEnd = params.indexOf(':');
				if (headerNameEnd >= 0) {
					String name = params.substring(0, headerNameEnd).trim();
					String value = params.substring(headerNameEnd + 1).trim();
					msg.addHeader(name, value);
				}
			} else if (SmtpState.DATA_BODY == response.getNextState()) {
				msg.appendBody(params);
			}
		}
	}

	private static void sendResponse(PrintWriter out, Response smtpResponse) {
		if (smtpResponse.getCode() > 0) {
			int code = smtpResponse.getCode();
			String message = smtpResponse.getMessage();
			out.print(code + " " + message + "\r\n");
			out.flush();
		}
	}

	public void run() {
		try {
			sessionLoop();
		} catch (Exception ignored) {
		} finally {
			try {
				socket.close();
			} catch (Exception ignored) {
			}
		}
	}

}
