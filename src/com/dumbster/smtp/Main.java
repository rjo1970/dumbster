package com.dumbster.smtp;

import com.dumbster.smtp.eml.EMLMailStore;


public class Main {
    public static void main(String[] args) {
        Arguments arguments = parseArguments(args);
        if (arguments.type == Arguments.Type.HELP || !arguments.valid) {
            showHelp();
            System.exit(1);

        } else {
            startServer(arguments);
        }
    }

    private static Arguments parseArguments(String[] args) {
        Arguments arguments = new Arguments();
        if (args.length == 0) {
            arguments.type = Arguments.Type.START;
            arguments.valid = true;
        } else {
            for (int i = 0; i < args.length; i++) {
                String argument = args[i];
                if ("--help".equals(argument) || "-h".equals(argument)
                        || "/?".equals(argument)) {
                    arguments.type = Arguments.Type.HELP;
                    break;

                } else if (argument.startsWith("--store")) {
                    String[] values = argument.split("=");
                    if (values.length != 2) {
                        arguments.valid = false;
                        break;
                    }

                    String storeName = values[1];

                    if (storeName.equalsIgnoreCase("EMLMailStore")) {
                        arguments.store = new EMLMailStore();
                    } else {
                        arguments.store = new RollingMailStore();
                    }

                } else if (argument.startsWith("--threaded")) {
                    arguments.threaded = !argument.equalsIgnoreCase("--threaded=false");

                } else {
                    try {
                        int port = Integer.parseInt(args[i]);
                        arguments.port = port;
                    } catch (NumberFormatException e) {
                        arguments.valid = false;
                        break;
                    }
                }
                    
            }
        }
        return arguments;
    }

    private static void startServer(Arguments arguments) {
        final SmtpServer server = SmtpServerFactory.startServer(arguments.port);

        server.setThreaded(arguments.threaded);

        server.setMailStore(arguments.store != null ? arguments.store : new RollingMailStore());

        System.out.println("Dumbster SMTP Server started on port " + arguments.port + ".\n");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                server.stop();
                System.out.println("Dumbster SMTP Server stopped");
                System.out.println("Total messages receives: " + server.getEmailCount());
            }
         });
    }

    private static void showHelp() {
        System.out.println("usage: java -jar dumbster.jar [options] [port]");
        System.out.println("Starts the SMTP server in the given port. Default port is 25.");
        System.out.println("");
        System.out.println("Options:");
        System.out.println("\t -h, --help \t\t this message");
        System.out.println("\t --store=MAIL_STORE \t define the MailStore used as back end of SMTP server");
        System.out.println("\t\t\t\t Possible values: EMLMailStore, RollingMailStore");
        System.out.println("\t --threaded[=THREADED] \t toggles if the SMTP server is single or multi-threaded. THREADED is 'true' or 'false'. Default is true.");
        System.out.println("");
        System.out.println("MailStores:");
        System.out.println("\t EMLMailStore \t\t Save messages in EML files");
        System.out.println("\t RollingMailStore \t Store messages in memory. Only the last 100 messages will be kept in memory. This is the default.");
        System.out.println("");
    }

    private static class Arguments {
        static enum Type {
            HELP,
            START;
        }
        
        public int port = SmtpServer.DEFAULT_SMTP_PORT;
        public boolean threaded = true;
        public Type type = Type.START;
        public MailStore store;
        public boolean valid = true;
    }
}
