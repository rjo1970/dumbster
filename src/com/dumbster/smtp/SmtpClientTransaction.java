package com.dumbster.smtp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.dumbster.smtp.action.Connect;

public class SmtpClientTransaction implements Runnable {
	
	private Socket socket;	
	private List<SmtpMessage> receivedMail = new ArrayList<SmtpMessage>();
	private List<SmtpMessage> serverMessages;
	public SmtpClientTransaction(Socket socket, List<SmtpMessage> messages) {
		this.socket = socket;
		this.serverMessages = messages;
	}
	
	private void handleTransaction() throws IOException {
		BufferedReader input = getSocketInput();
		PrintWriter out = getSocketOutput();

		// Initialize the state machine
		SmtpState smtpState = SmtpState.CONNECT;
		SmtpRequest smtpRequest = new SmtpRequest(new Connect(), "",
				smtpState);

		// Execute the connection request
		SmtpResponse smtpResponse = smtpRequest.execute();

		// Send initial response
		sendResponse(out, smtpResponse);
		smtpState = smtpResponse.getNextState();

		List<SmtpMessage> msgList = new ArrayList<SmtpMessage>();
		SmtpMessage msg = new SmtpMessage();

		while (smtpState != SmtpState.CONNECT) {
			String line = input.readLine();

			if (line == null) {
				break;
			}

			// Create request from client input and current state
			SmtpRequest request = SmtpRequest.createRequest(line, smtpState);
			// Execute request and create response object
			SmtpResponse response = request.execute();
			// Move to next internal state
			smtpState = response.getNextState();
			// Send response to client
			sendResponse(out, response);

			// Store input in message
			String params = request.getParams();
			msg.store(response, params);

			// If message reception is complete save it
			if (smtpState == SmtpState.QUIT) {
				msgList.add(msg);
				msg = new SmtpMessage();
			}
		}
		receivedMail.addAll(msgList);
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

	public List<SmtpMessage> getReceivedMail() {
		return receivedMail;
	}

	@Override
	public void run() {
		try {
		handleTransaction();
		serverMessages.addAll(getReceivedMail());
		} catch(Exception e) {}
		finally {
			try {
				socket.close();
			} catch (Exception e2) {}
		}
	}
	
}
