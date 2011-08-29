package com.dumbster.smtp;

public class Main {
    public static void main(String[] args) {
        SimpleSmtpServer server;
        if (args.length == 1) {
            server = SmtpServerFactory.startServer(port(args[0]));
        } else {
            server = SmtpServerFactory.startServer();
        }
        server.setThreaded(true);
        System.out.println("Dumbster SMTP Server started.\n");
    }

    private static int port(String s)  {
        return Integer.parseInt(s);
    }

}
