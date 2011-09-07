package com.dumbster.smtp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: rj
 * Date: Aug 28, 2011
 * Time: 6:48:14 AM
 */
public class SmtpServerFactory {
    public static SmtpServer startServer(int port) {
        SmtpServer server = new SmtpServer(port);
        executeServer(server);
        waitForReadyServer(server);
        return server;
    }

    private static void executeServer(SmtpServer server) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(server);
    }

    private static void waitForReadyServer(SmtpServer server) {
        while (!server.isReady()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static SmtpServer startServer() {
        return startServer(SmtpServer.DEFAULT_SMTP_PORT);
    }
}
