package com.dumbster.smtp;

public class Main {

    public static void main(String[] args) {
        ServerOptions serverOptions = new ServerOptions(args);
        if (shouldShowHelp(args) || serverOptions.valid == false) {
            showHelp();
            System.exit(1);
        }

        SmtpServerFactory.startServer(serverOptions);
    }

    private static boolean shouldShowHelp(String[] args) {
        if (args.length == 0)
            return false;
        for (String arg : args) {
            if ("--help".equalsIgnoreCase(arg) || "-h".equalsIgnoreCase(arg))
                return true;
        }
        return false;
    }

    private static void showHelp() {
        System.out.println();
        System.out.println("Dumbster Fake SMTP Server");
        System.out.println("usage: java -jar dumbster.jar [options] [port]");
        System.out.println("Starts the SMTP server in the given port. Default port is 25.");
        System.out.println();
        System.out.println("Options:");
        System.out.println("\t-h, --help this message");
        System.out.println("\t--mailStore=EMLMailMessage Use a file-based mail store");
        System.out.println("MailStores:");
        System.out.println("\tRollingMailStore (Default)  Store messages in memory. Only the last 100 messages will be kept in memory");
        System.out.println("\tEMLMailStore Save messages in EML files");
        System.out.println();
        System.out.println("\t--threaded=false Forces the SMTP server to be single-threaded.");
    }

}
