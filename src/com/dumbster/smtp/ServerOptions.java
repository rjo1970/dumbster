package com.dumbster.smtp;

import com.dumbster.smtp.mailstores.RollingMailStore;

/**
 * User: rj
 * Date: 7/18/13
 * Time: 5:35 AM
 */
public class ServerOptions {
    public int port = SmtpServer.DEFAULT_SMTP_PORT;
    public boolean threaded = true;
    public MailStore mailStore = new RollingMailStore();
    public boolean valid = true;

    public ServerOptions() {
    }

    public ServerOptions(String[] args) {
        if (args.length == 0) {
            return;
        }

        for (String argument : args) {
            if (argument.startsWith("--mailStore")) {
                String[] values = argument.split("=");
                if (values.length != 2) {
                    this.valid = false;
                    return;
                }
                try {
                    this.mailStore = (MailStore) Class.forName("com.dumbster.smtp.mailstores."+values[1]).newInstance();
                } catch (Exception e) {
                    this.valid = false;
                    return;
                }
            } else if (argument.startsWith("--threaded")) {
                this.threaded = !argument.equalsIgnoreCase("--threaded=false");
            } else {
                try {
                    this.port = Integer.parseInt(argument);
                } catch (NumberFormatException e) {
                    this.valid = false;
                    break;
                }
            }
        }
    }
}
