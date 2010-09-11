package com.dumbster.smtp;

public enum SmtpAction {
    CONNECT ("Connect", false),
    EHLO ("EHLO", false),
    MAIL ("MAIL", false),
    RCPT ("RCPT", false),
    DATA ("DATA", false),
    DATA_END (".", false),
    QUIT ("QUIT", false),
    UNRECOG ("Unrecognized command / data", false),
    BLANK_LINE ("Blank line", false),
    RSET("RSET", true),
    VRFY("VRFY", true),
    EXPN("EXPN", true),
    HELP("HELP", true),
    NOOP("NOOP", true);

    
    private boolean stateless;
    private String description;

    SmtpAction(String description, boolean stateless) {
        this.description = description;
        this.stateless = stateless;
    }

    public boolean isStateless() {
        return stateless;
    }

    public String toString() {
        return description;
    }

}
