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

/**
 * Contains an SMTP client request. Handles state transitions using the
 * following state transition table.
 * 
 * <PRE>
 * -----------+-------------------------------------------------------------------------------------------------
 *            |                                 State
 *  Action    +-------------+-----------+-----------+--------------+---------------+---------------+------------
 *            | CONNECT     | GREET     | MAIL      | RCPT         | DATA_HDR      | DATA_BODY     | QUIT
 * -----------+-------------+-----------+-----------+--------------+---------------+---------------+------------
 * connect    | 220/GREET   | 503/GREET | 503/MAIL  | 503/RCPT     | 503/DATA_HDR  | 503/DATA_BODY | 503/QUIT
 * ehlo       | 503/CONNECT | 250/MAIL  | 503/MAIL  | 503/RCPT     | 503/DATA_HDR  | 503/DATA_BODY | 503/QUIT
 * mail       | 503/CONNECT | 503/GREET | 250/RCPT  | 503/RCPT     | 503/DATA_HDR  | 503/DATA_BODY | 250/RCPT
 * rcpt       | 503/CONNECT | 503/GREET | 503/MAIL  | 250/RCPT     | 503/DATA_HDR  | 503/DATA_BODY | 503/QUIT
 * data       | 503/CONNECT | 503/GREET | 503/MAIL  | 354/DATA_HDR | 503/DATA_HDR  | 503/DATA_BODY | 503/QUIT
 * data_end   | 503/CONNECT | 503/GREET | 503/MAIL  | 503/RCPT     | 250/QUIT      | 250/QUIT      | 503/QUIT
 * unrecog    | 500/CONNECT | 500/GREET | 500/MAIL  | 500/RCPT     | ---/DATA_HDR  | ---/DATA_BODY | 500/QUIT
 * quit       | 503/CONNECT | 503/GREET | 503/MAIL  | 503/RCPT     | 503/DATA_HDR  | 503/DATA_BODY | 250/CONNECT
 * blank_line | 503/CONNECT | 503/GREET | 503/MAIL  | 503/RCPT     | ---/DATA_BODY | ---/DATA_BODY | 503/QUIT
 * rset       | 250/GREET   | 250/GREET | 250/GREET | 250/GREET    | 250/GREET     | 250/GREET     | 250/GREET
 * vrfy       | 252/CONNECT | 252/GREET | 252/MAIL  | 252/RCPT     | 252/DATA_HDR  | 252/DATA_BODY | 252/QUIT
 * expn       | 252/CONNECT | 252/GREET | 252/MAIL  | 252/RCPT     | 252/DATA_HDR  | 252/DATA_BODY | 252/QUIT
 * help       | 211/CONNECT | 211/GREET | 211/MAIL  | 211/RCPT     | 211/DATA_HDR  | 211/DATA_BODY | 211/QUIT
 * noop       | 250/CONNECT | 250/GREET | 250/MAIL  | 250/RCPT     | 250|DATA_HDR  | 250/DATA_BODY | 250/QUIT
 * </PRE>
 */
public class SmtpRequest {
	private AbstractAction clientAction;
	private SmtpState smtpState;
	private String params;	

	public SmtpRequest(AbstractAction action, String params, SmtpState state) {
		this.clientAction = action;
		this.smtpState = state;
		this.params = params;
	}

	public SmtpResponse execute(List<SmtpMessage> messages, SmtpMessage message) {		
		return clientAction.response(smtpState, messages, message);
	}
	
	public String getParams() {
		return params;
	}
}
