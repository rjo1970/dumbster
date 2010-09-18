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

import org.junit.*;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.Request;
import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;
import com.dumbster.smtp.action.*;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class RequestTest {
	
	private static List<MailMessage> messages = new ArrayList<MailMessage>();
	private static MailMessage message = new MailMessage();

    @Test
    public void testUnrecognizedCommandConnectState() {
        Request request = new Request(new Unrecognized(), null, SmtpState.CONNECT);
        SmtpResponse response = request.execute(messages, message);
        assertEquals(500, response.getCode());
    }
    
    @Test
    public void testConnect_Connect() {
    	Request request = new Request(new Connect(), null, SmtpState.CONNECT);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(220, response.getCode());
    }
    
    @Test
    public void testConnect_NotConnectState() {
    	Request request = new Request(new Connect(), null, SmtpState.GREET);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(503, response.getCode());    	
    }
    
    @Test
    public void testEhlo_QuitState() {
    	Request request = new Request(new Ehlo(), null, SmtpState.QUIT);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(503, response.getCode());    	
    }

    @Test
    public void testEhlo_GreetState() {
    	Request request = new Request(new Ehlo(), null, SmtpState.GREET);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(250, response.getCode());    	
    }

    @Test
    public void testMail_MailState() {
    	Request request = new Request(new Mail(), null, SmtpState.MAIL);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(250, response.getCode());    	
    }

    @Test
    public void testMail_GreetState() {
    	Request request = new Request(new Mail(), null, SmtpState.GREET);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(503, response.getCode());    	
    }

    @Test
    public void testRcpt_RcptState() {
    	Request request = new Request(new Rcpt(), null, SmtpState.RCPT);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(250, response.getCode());    	
    }

    @Test
    public void testRcpt_Quit() {
    	Request request = new Request(new Rcpt(), null, SmtpState.QUIT);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(503, response.getCode());    	
    }
    
    @Test
    public void testData_Rcpt() {
    	Request request = new Request(new Data(), null, SmtpState.RCPT);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(354, response.getCode());    	
    }
    
    @Test
    public void testDataEnd_DataBody() {
    	Request request = new Request(new DataEnd(), null, SmtpState.DATA_BODY);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(250, response.getCode());    	
    }
    
    @Test
    public void testDataEnd_QUIT() {
    	Request request = new Request(new DataEnd(), null, SmtpState.QUIT);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(503, response.getCode());    	
    }
    
    @Test
    public void testQuit_QUIT() {
    	Request request = new Request(new Quit(), null, SmtpState.QUIT);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(221, response.getCode());    	
    }
    
    @Test @Ignore  // Quit now always works
    public void testQuit_RCPT() {
    	Request request = new Request(new Quit(), null, SmtpState.RCPT);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(503, response.getCode());    	
    }
    
    @Test
    public void testData_RcptQuit() {
    	Request request = new Request(new Data(), null, SmtpState.QUIT);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(503, response.getCode());    	
    }
    
    @Test
    public void testBlankLine_DataHeader() {
    	Request request = new Request(new BlankLine(), null, SmtpState.DATA_HDR);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(-1, response.getCode());    	
    }
    
    @Test
    public void testBlankLine_DataBody() {
    	Request request = new Request(new BlankLine(), null, SmtpState.DATA_BODY);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(-1, response.getCode());    	
    }
    
    @Test
    public void testBlankLine_Quit() {
    	Request request = new Request(new BlankLine(), null, SmtpState.QUIT);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(503, response.getCode());    	
    }
    
    @Test
    public void testRset() {
    	Request request = new Request(new Rset(), null, null);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(250, response.getCode());    	
    }
    
    @Test
    public void testVrfy() {
    	Request request = new Request(new Vrfy(), null, null);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(252, response.getCode());    	    	
    }
    
    @Test
    public void testExpn() {
    	Request request = new Request(new Expn(), null, null);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(252, response.getCode());    	    	
    }
    
    @Test
    public void testHelp() {
    	Request request = new Request(new Help(), null, null);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(211, response.getCode());    	    	
    }
    
    @Test
    public void testNoOp() {
    	Request request = new Request(new NoOp(), null, null);
    	SmtpResponse response = request.execute(messages, message);
    	assertEquals(250, response.getCode());    	    	
    }
    
    @Test
    public void testUnrecognizedCommandGreetState() {
        Request request = new Request(new Unrecognized(), null, SmtpState.GREET);
        SmtpResponse response = request.execute(messages, message);
        assertEquals(500, response.getCode());
    }

    @Test
    public void testUnrecognizedCommandMailState() {
        Request request = new Request(new Unrecognized(), null, SmtpState.MAIL);
        SmtpResponse response = request.execute(messages, message);
        assertEquals(500, response.getCode());
    }

    @Test
    public void testUnrecognizedCommandQuitState() {
        Request request = new Request(new Unrecognized(), null, SmtpState.QUIT);
        SmtpResponse response = request.execute(messages, message);
        assertEquals(500, response.getCode());
    }

    @Test
    public void testUnrecognizedCommandRcptState() {
        Request request = new Request(new Unrecognized(), null, SmtpState.RCPT);
        SmtpResponse response = request.execute(messages, message);
        assertEquals(500, response.getCode());
    }

    @Test
    public void testUnrecognizedCommandDataBodyState() {
        Request request = new Request(new Unrecognized(), null, SmtpState.DATA_BODY);
        SmtpResponse response = request.execute(messages, message);
        assertEquals(-1, response.getCode());
    }

    @Test
    public void testUnrecognizedCommandDataHdrState() {
        Request request = new Request(new Unrecognized(), null, SmtpState.DATA_HDR);
        SmtpResponse response = request.execute(messages, message);
        assertEquals(-1, response.getCode());
    }

}
