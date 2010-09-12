package com.dumbster.smtp;

public class Main {
	public static void main(String[] args) {
		SimpleSmtpServer server = null;
		if (args.length == 1) {
			server = new SimpleSmtpServer(Integer.parseInt(args[0]));
		} else {
			server = new SimpleSmtpServer();
		}
		server.setThreaded(true);
		server.run();
	}

}
