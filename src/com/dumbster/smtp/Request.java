/*
 * Dumbster - a dummy SMTP server
 * Copyright 2004 Jason Paul Kitchen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dumbster.smtp;

import com.dumbster.smtp.action.*;

public class Request {
    private Action clientAction;
    private SmtpState state;
    private String params;

    Request(Action action, String params, SmtpState state) {
        this.clientAction = action;
        this.state = state;
        this.params = params;
    }

    private Request() {
    }

    public Response execute(MailStore mailStore, MailMessage message) {
        return clientAction.response(state, mailStore, message);
    }

    Action getClientAction() {
        return clientAction;
    }

    SmtpState getState() {
        return state;
    }

    public String getParams() {
        return params;
    }

    private boolean isInDataHeaderState() {
        return SmtpState.DATA_HDR == state;
    }

    private boolean isInDataBodyState() {
        return SmtpState.DATA_BODY == state;
    }

    public static Request initialRequest() {
        return new Request(new Connect(), "", SmtpState.CONNECT);
    }

    public static Request createRequest(SmtpState state, String message) {
        Request request = new Request();
        request.state = state;

        if (request.isInDataHeaderState()) {
            return buildDataHeaderRequest(message, request);
        }

        if (request.isInDataBodyState()) {
            return buildDataBodyRequest(message, request);
        }
        return buildCommandRequest(message, request);
    }

    private static Request buildDataHeaderRequest(String message, Request request) {
        if (message.equals(".")) {
            request.clientAction = new DataEnd();
        } else if (message.length() < 1) {
            request.clientAction = new BlankLine();
        } else {
            request.clientAction = new Unrecognized();
            request.params = message;
        }
        return request;
    }

    private static Request buildDataBodyRequest(String message, Request request) {
        if (message.equals(".")) {
            request.clientAction = new DataEnd();
        } else {
            request.clientAction = new Unrecognized();
            if (message.length() < 1) {
                request.params = "\n";
            } else {
                request.params = message.startsWith(".") ? message.substring(1) : message;
            }
        }
        return request;
    }

    private static Request buildCommandRequest(String message, Request request) {
        String su = message.toUpperCase();
        if (su.startsWith("EHLO ") || su.startsWith("HELO")) {
            request.clientAction = new Ehlo();
            extractParams(message, request);
        } else if (su.startsWith("MAIL FROM:")) {
            request.clientAction = new Mail();
            request.params = message.substring(10);
        } else if (su.startsWith("RCPT TO:")) {
            request.clientAction = new Rcpt();
            request.params = message.substring(8);
        } else if (su.startsWith("DATA")) {
            request.clientAction = new Data();
        } else if (su.startsWith("QUIT")) {
            request.clientAction = new Quit();
        } else if (su.startsWith("RSET")) {
            request.clientAction = new Rset();
        } else if (su.startsWith("NOOP")) {
            request.clientAction = new NoOp();
        } else if (su.startsWith("EXPN")) {
            request.clientAction = new Expn();
        } else if (su.startsWith("VRFY")) {
            request.clientAction = new Vrfy();
        } else if (su.startsWith("HELP")) {
            request.clientAction = new Help();
        } else if (su.startsWith("LIST")) {
            extractParams(message, request);
            request.clientAction = new List(request.params);
        } else {
            request.clientAction = new Unrecognized();
        }
        return request;
    }

    private static void extractParams(String message, Request request) {
        try {
            request.params = message.substring(5);
        } catch (StringIndexOutOfBoundsException ignored) {
        }
    }

}
