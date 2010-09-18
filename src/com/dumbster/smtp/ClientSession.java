package com.dumbster.smtp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.dumbster.smtp.action.Connect;

public class ClientSession implements Runnable {

	private IOSource socket;
	private List<MailMessage> serverMessages;
	private MailMessage msg;
	private SmtpResponse smtpResponse;

	public ClientSession(IOSource socket, List<MailMessage> messages) {
		this.socket = socket;
		this.serverMessages = messages;
		msg = new MailMessage();
		Request smtpRequest = initializeStateMachine();
		smtpResponse = smtpRequest.execute(serverMessages, msg);
	}

	private void sessionLoop() throws IOException {
		BufferedReader input = socket.getInputStream();
		PrintWriter out = socket.getOutputStream();

		SmtpState smtpState = sendInitialResponse(out);

		while (smtpState != SmtpState.CONNECT) {
			String line = input.readLine();

			if (line == null) {
				break;
			}

			Request request = smtpState.createRequest(line);
			SmtpResponse response = request.execute(serverMessages, msg);
			storeInputInMessage(request, response);
			sendResponse(out, response);
			smtpState = response.getNextState();
			saveAndRefreshMessageIfComplete(smtpState);
		}
	}

	private void saveAndRefreshMessageIfComplete(SmtpState smtpState) {
		if (smtpState == SmtpState.QUIT) {
			serverMessages.add(msg);
			System.out.println(msg);
			msg = new MailMessage();
		}
	}

	private void storeInputInMessage(Request request, SmtpResponse response) {
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

	private SmtpState sendInitialResponse(PrintWriter out) {
		sendResponse(out, smtpResponse);
		return smtpResponse.getNextState();
	}

	private Request initializeStateMachine() {
		SmtpState smtpState = SmtpState.CONNECT;
		Request smtpRequest = new Request(new Connect(), "", smtpState);
		return smtpRequest;
	}

	private static void sendResponse(PrintWriter out, SmtpResponse smtpResponse) {
		if (smtpResponse.getCode() > 0) {
			int code = smtpResponse.getCode();
			String message = smtpResponse.getMessage();
			out.print(code + " " + message + "\r\n");
			out.flush();
		}
	}

	@Override
	public void run() {
		try {
			sessionLoop();
		} catch (Exception e) {
		} finally {
			try {
				socket.close();
			} catch (Exception e2) {
			}
		}
	}

}
