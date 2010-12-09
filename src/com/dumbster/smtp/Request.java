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

import java.util.List;

import com.dumbster.smtp.action.*;

public class Request {
	private Action clientAction;
	private SmtpState smtpState;
	private String params;	

	Request(Action action, String params, SmtpState state) {
		this.clientAction = action;
		this.smtpState = state;
		this.params = params;
	}

	public Response execute(List<MailMessage> messages, MailMessage message) {		
		return clientAction.response(smtpState, messages, message);
	}
	
	public String getParams() {
		return params;
	}

    public static Request initialRequest() {
        return new Request(new Connect(), "", SmtpState.CONNECT);
    }

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
