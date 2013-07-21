package com.dumbster.smtp;

public class Main {

    public static void main(String[] args) {
        ServerOptions serverOptions = new ServerOptions(args);
        if (shouldShowHelp(args) || serverOptions.valid == false) {
            showHelp();
            System.exit(1);
        } else {
            SmtpServerFactory.startServer(serverOptions);
        }
    }

    private static boolean shouldShowHelp(String[] args) {
        if (args.length == 0)
            return true;
        for (String arg : args) {
            if ("--help".equals(arg) || "-h".equals(arg)
                    || "/?".equals(arg))
                return true;
        }
        return false;
    }

    private static void showHelp() {
        System.out.println("usage: java -jar dumbster.jar [options] [port]");
        System.out.println("Starts the SMTP server in the given port. Default port is 25.");
        System.out.println("");
        System.out.println("Options:");
        System.out.println("\t -h, --help \t\t this message");
        System.out.println("\t --mailStore=MAIL_STORE \t define the MailStore used as back end of SMTP server");
        System.out.println("\t\t\t\t Possible values: EMLMailStore, RollingMailStore");
        System.out.println("\t --threaded[=THREADED] \t toggles if the SMTP server is single or multi-threaded. THREADED is 'true' or 'false'. Default is true.");
        System.out.println("");
        System.out.println("MailStores:");
        System.out.println("\t EMLMailStore \t\t Save messages in EML files");
        System.out.println("\t RollingMailStore \t Store messages in memory. Only the last 100 messages will be kept in memory. This is the default.");
        System.out.println("");
    }

}
