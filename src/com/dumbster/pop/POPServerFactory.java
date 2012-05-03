package com.dumbster.pop;

import java.util.concurrent.Executors;

public class POPServerFactory {
    public static POPServer startServer(int port) {
        POPServer server = new POPServer(port);
        Executors.newSingleThreadExecutor().execute(server);
        return whenReady(server);
    }

    private static POPServer whenReady(POPServer server) {
        while (!server.isReady()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return server;
    }
}
