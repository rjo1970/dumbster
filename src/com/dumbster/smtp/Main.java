package com.dumbster.smtp;

public class Main {
    public static void main(String[] args) {
        SimpleSmtpServer server;
        if (args.length == 1) {
            server = SmtpServerExecutor.startServer(port(args[0]));
        } else {
            server = SmtpServerExecutor.startServer();
        }
        server.setThreaded(true);
        server.run();
    }

    private static int port(String s)  {
        return Integer.parseInt(s);
    }

}
