package com.dumbster.smtp;

import com.dumbster.smtp.action.*;


public class RequestFactory {

    public static Request createRequest(SmtpState state, String s) {
        Action action;
        String params = null;

        if (SmtpState.DATA_HDR == state) {
            if (s.equals(".")) {
                action = new DataEnd();
            } else if (s.length() < 1) {
                action = new BlankLine();
            } else {
                action = new Unrecognized();
                params = s;
            }
        } else if (SmtpState.DATA_BODY == state) {
            if (s.equals(".")) {
                action = new DataEnd();
            } else {
                action = new Unrecognized();
                if (s.length() < 1) {
                    params = "\n";
                } else {
                    params = s;
                }
            }
        } else {
            String su = s.toUpperCase();
            if (su.startsWith("EHLO ") || su.startsWith("HELO")) {
                action = new Ehlo();
                params = s.substring(5);
            } else if (su.startsWith("MAIL FROM:")) {
                action = new Mail();
                params = s.substring(10);
            } else if (su.startsWith("RCPT TO:")) {
                action = new Rcpt();
                params = s.substring(8);
            } else if (su.startsWith("DATA")) {
                action = new Data();
            } else if (su.startsWith("QUIT")) {
                action = new Quit();
            } else if (su.startsWith("RSET")) {
                action = new Rset();
            } else if (su.startsWith("NOOP")) {
                action = new NoOp();
            } else if (su.startsWith("EXPN")) {
                action = new Expn();
            } else if (su.startsWith("VRFY")) {
                action = new Vrfy();
            } else if (su.startsWith("HELP")) {
                action = new Help();
            } else {
                action = new Unrecognized();
            }
        }
        return new Request(action, params, state);
    }

}
