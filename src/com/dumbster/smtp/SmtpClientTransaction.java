package com.dumbster.smtp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import com.dumbster.smtp.action.Connect;

public class SmtpClientTransaction implements Runnable {
	
	private Socket socket;	
	private List<SmtpMessage> serverMessages;
	public SmtpClientTransaction(Socket socket, List<SmtpMessage> messages) {
		this.socket = socket;
		this.serverMessages = messages;
	}
	
	private void handleTransaction() throws IOException {
		BufferedReader input = getSocketInput();
		PrintWriter out = getSocketOutput();

		SmtpRequest smtpRequest = initializeStateMachine();
		SmtpResponse smtpResponse = smtpRequest.execute();
		SmtpState smtpState = sendInitialResponse(out, smtpResponse);

		SmtpMessage msg = new SmtpMessage();

		while (smtpState != SmtpState.CONNECT) {
			String line = input.readLine();

			if (line == null) {
				break;
			}

			SmtpRequest request = SmtpRequest.createRequest(line, smtpState);
			SmtpResponse response = request.execute();
			smtpState = response.getNextState();
			sendResponse(out, response);

			// Store input in message
			String params = request.getParams();
			msg.store(response, params);

			// If message reception is complete save it
			if (smtpState == SmtpState.QUIT) {
				serverMessages.add(msg);
				System.out.println(msg);
				msg = new SmtpMessage();
			}
		}
	}

	private SmtpState sendInitialResponse(PrintWriter out,
			SmtpResponse smtpResponse) {
		SmtpState smtpState;
		// Send initial response
		sendResponse(out, smtpResponse);
		smtpState = smtpResponse.getNextState();
		return smtpState;
	}

	private SmtpRequest initializeStateMachine() {
		// Initialize the state machine
		SmtpState smtpState = SmtpState.CONNECT;
		SmtpRequest smtpRequest = new SmtpRequest(new Connect(), "",
				smtpState);
		return smtpRequest;
	}
	
	private BufferedReader getSocketInput() throws IOException {
		return new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
	}

	private PrintWriter getSocketOutput() throws IOException {
		return new PrintWriter(socket.getOutputStream());
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
		handleTransaction();
		} catch(Exception e) {}
		finally {
			try {
				socket.close();
			} catch (Exception e2) {}
		}
	}
	
}
