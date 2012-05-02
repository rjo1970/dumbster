package com.dumbster.smtp;

import com.dumbster.pop.POPServer;
import com.dumbster.pop.POPServerFactory;

public class Main {
    public static void main(String[] args) {
        SmtpServer server;
        int pt = SmtpServer.DEFAULT_SMTP_PORT;
        if (args.length > 0) {
            pt = port(args[0]);
        }
        server = SmtpServerFactory.startServer(pt);
        FixedSizeMailStore store = new FixedSizeMailStore(50);
        server.setMailStore(store);
        server.setThreaded(true);
        System.out.println("Dumbster SMTP Server started on port "+pt+"\n");
        // Start up the POP3 server as well
        POPServer pserver;
        pt = POPServer.DEFAULT_POP_PORT;
        if (args.length > 1) {
            pt = port(args[1]);
        }
        pserver = POPServerFactory.startServer(pt);
        pserver.setMailStore(store);
        pserver.setThreaded(true);
        System.out.println("Dumbster POP3 Server started on port "+pt+"\n");
    }

    private static int port(String s)  {
        return Integer.parseInt(s);
    }

}
