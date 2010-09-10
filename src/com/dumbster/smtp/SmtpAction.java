package com.dumbster.smtp;

public enum SmtpAction {
    CONNECT ((byte)1, "Connect", false),
    EHLO ((byte)2, "EHLO", false),
    MAIL ((byte)3, "MAIL", false),
    RCPT ((byte)4, "RCPT", false),
    DATA ((byte)5, "DATA", false),
    DATA_END ((byte)6, ".", false),
    QUIT ((byte)7, "QUIT", false),
    UNRECOG ((byte)8, "Unrecognized command / data", false),
    BLANK_LINE ((byte)9, "Blank line", false),
    RSET((byte)-1, "RSET", true),
    VRFY((byte)-2, "VRFY", true),
    EXPN((byte)-3, "EXPN", true),
    HELP((byte)-4, "HELP", true),
    NOOP((byte)-5, "NOOP", true);

    
    private byte code;
    private boolean stateless;
    private String description;

    SmtpAction(byte code, String description, boolean stateless) {
        this.code = code;
        this.description = description;
        this.stateless = stateless;
    }

    public byte getCode() {
        return code;
    }

    public boolean isStateless() {
        return stateless;
    }

    public String toString() {
        return description;
    }

}
