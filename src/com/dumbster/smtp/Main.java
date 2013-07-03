package com.dumbster.smtp;

import com.dumbster.smtp.eml.EMLMailStore;


public class Main {
    public static void main(String[] args) {
        Arguments arguments = parseArguments(args);
        startServer(arguments);

    }

    private static Arguments parseArguments(String[] args) {
        Arguments arguments = new Arguments();
        if (args.length == 0) {
            arguments.type = Arguments.Type.START;
            arguments.valid = true;
        } else {
            for (int i = 0; i < args.length; i++) {
                if ("--help".equals(args[i]) || "-h".equals(args[i])) {
                    arguments.type = Arguments.Type.HELP;
                    break;
                } else {
                    try {
                        int port = Integer.parseInt(args[i]);
                        arguments.port = port;
                    } catch (NumberFormatException e) {
                        
                    }
                }
                    
            }
        }
        return arguments;
    }

    private static void startServer(Arguments arguments) {
        final SmtpServer server = SmtpServerFactory.startServer(arguments.port);

        server.setThreaded(arguments.threaded);

        server.setMailStore(new EMLMailStore());

        System.out.println("Dumbster SMTP Server started on port " + arguments.port + ".\n");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                server.stop();
                System.out.println("Dumbster SMTP Server stopped");
                System.out.println("Total messages receives: " + server.getEmailCount());
            }
         });
    }

    private static class Arguments {
        static enum Type {
            HELP,
            START;
        }
        
        public int port = SmtpServer.DEFAULT_SMTP_PORT;
        public boolean threaded = true;
        public Type type;
        public boolean valid;
    }
}
