package com.dumbster.smtp;

import java.util.concurrent.Executors;

/**
 * User: rj
 * Date: Aug 28, 2011
 * Time: 6:48:14 AM
 */
public class SmtpServerFactory {
    public static SmtpServer startServer(int port) {
        SmtpServer server = new SmtpServer(port);
        Executors.newSingleThreadExecutor().execute(server);
        return whenReady(server);
    }

    private static SmtpServer whenReady(SmtpServer server) {
        while (!server.isReady()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return server;
    }

    public static SmtpServer startServer() {
        return startServer(SmtpServer.DEFAULT_SMTP_PORT);
    }
}
