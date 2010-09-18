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

public class SmtpRequest {
	private Action clientAction;
	private SmtpState smtpState;
	private String params;	

	public SmtpRequest(Action action, String params, SmtpState state) {
		this.clientAction = action;
		this.smtpState = state;
		this.params = params;
	}

	public SmtpResponse execute(List<MailMessage> messages, MailMessage message) {		
		return clientAction.response(smtpState, messages, message);
	}
	
	public String getParams() {
		return params;
	}
}
