package com.dumbster.smtp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: rj
 * Date: Aug 28, 2011
 * Time: 6:48:14 AM
 */
public class SmtpServerExecutor {
    public static SimpleSmtpServer startServer(int port) {
        SimpleSmtpServer server = new SimpleSmtpServer(port);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(server);
        return server;
    }

    public static SimpleSmtpServer startServer() {
        return startServer(SimpleSmtpServer.DEFAULT_SMTP_PORT);
    }
}
