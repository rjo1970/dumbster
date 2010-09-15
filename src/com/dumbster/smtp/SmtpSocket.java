package com.dumbster.smtp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SmtpSocket implements SmtpIOSource {
	private Socket socket;

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public BufferedReader getInputStream() throws IOException {
		return new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
	}

	public PrintWriter getOutputStream() throws IOException {
		return new PrintWriter(socket.getOutputStream());
	}
	
	public void close() throws IOException {
		socket.close();
	}

}
