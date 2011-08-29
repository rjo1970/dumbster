package com.dumbster.smtp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: rj
 * Date: Aug 28, 2011
 * Time: 6:48:14 AM
 */
public class SmtpServerFactory {
    public static SimpleSmtpServer startServer(int port) {
        SimpleSmtpServer server = new SimpleSmtpServer(port);
        executeServer(server);
        waitForReadyServer(server);
        return server;
    }

    private static void executeServer(SimpleSmtpServer server) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(server);
    }

    private static void waitForReadyServer(SimpleSmtpServer server) {
        while (!server.isReady()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static SimpleSmtpServer startServer() {
        return startServer(SimpleSmtpServer.DEFAULT_SMTP_PORT);
    }
}
