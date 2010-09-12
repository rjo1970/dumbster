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

import com.dumbster.smtp.SmtpMessage;
import com.dumbster.smtp.SmtpRequest;
import com.dumbster.smtp.SmtpResponse;
import com.dumbster.smtp.SmtpState;
import com.dumbster.smtp.action.*;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class SmtpRequestTest {
	
	private static List<SmtpMessage> messages = new ArrayList<SmtpMessage>();

    @Test
    public void testUnrecognizedCommandConnectState() {
        SmtpRequest request = new SmtpRequest(new Unrecognized(), null, SmtpState.CONNECT);
        SmtpResponse response = request.execute(messages);
        assertEquals(500, response.getCode());
    }
    
    @Test
    public void testConnect_Connect() {
    	SmtpRequest request = new SmtpRequest(new Connect(), null, SmtpState.CONNECT);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(220, response.getCode());
    }
    
    @Test
    public void testConnect_NotConnectState() {
    	SmtpRequest request = new SmtpRequest(new Connect(), null, SmtpState.GREET);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(503, response.getCode());    	
    }
    
    @Test
    public void testEhlo_QuitState() {
    	SmtpRequest request = new SmtpRequest(new Ehlo(), null, SmtpState.QUIT);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(503, response.getCode());    	
    }

    @Test
    public void testEhlo_GreetState() {
    	SmtpRequest request = new SmtpRequest(new Ehlo(), null, SmtpState.GREET);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(250, response.getCode());    	
    }

    @Test
    public void testMail_MailState() {
    	SmtpRequest request = new SmtpRequest(new Mail(), null, SmtpState.MAIL);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(250, response.getCode());    	
    }

    @Test
    public void testMail_GreetState() {
    	SmtpRequest request = new SmtpRequest(new Mail(), null, SmtpState.GREET);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(503, response.getCode());    	
    }

    @Test
    public void testRcpt_RcptState() {
    	SmtpRequest request = new SmtpRequest(new Rcpt(), null, SmtpState.RCPT);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(250, response.getCode());    	
    }

    @Test
    public void testRcpt_Quit() {
    	SmtpRequest request = new SmtpRequest(new Rcpt(), null, SmtpState.QUIT);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(503, response.getCode());    	
    }
    
    @Test
    public void testData_Rcpt() {
    	SmtpRequest request = new SmtpRequest(new Data(), null, SmtpState.RCPT);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(354, response.getCode());    	
    }
    
    @Test
    public void testDataEnd_DataBody() {
    	SmtpRequest request = new SmtpRequest(new DataEnd(), null, SmtpState.DATA_BODY);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(250, response.getCode());    	
    }
    
    @Test
    public void testDataEnd_QUIT() {
    	SmtpRequest request = new SmtpRequest(new DataEnd(), null, SmtpState.QUIT);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(503, response.getCode());    	
    }
    
    @Test
    public void testQuit_QUIT() {
    	SmtpRequest request = new SmtpRequest(new Quit(), null, SmtpState.QUIT);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(221, response.getCode());    	
    }
    
    @Test @Ignore  // Quit now always works
    public void testQuit_RCPT() {
    	SmtpRequest request = new SmtpRequest(new Quit(), null, SmtpState.RCPT);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(503, response.getCode());    	
    }
    
    @Test
    public void testData_RcptQuit() {
    	SmtpRequest request = new SmtpRequest(new Data(), null, SmtpState.QUIT);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(503, response.getCode());    	
    }
    
    @Test
    public void testBlankLine_DataHeader() {
    	SmtpRequest request = new SmtpRequest(new BlankLine(), null, SmtpState.DATA_HDR);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(-1, response.getCode());    	
    }
    
    @Test
    public void testBlankLine_DataBody() {
    	SmtpRequest request = new SmtpRequest(new BlankLine(), null, SmtpState.DATA_BODY);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(-1, response.getCode());    	
    }
    
    @Test
    public void testBlankLine_Quit() {
    	SmtpRequest request = new SmtpRequest(new BlankLine(), null, SmtpState.QUIT);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(503, response.getCode());    	
    }
    
    @Test
    public void testRset() {
    	SmtpRequest request = new SmtpRequest(new Rset(), null, null);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(250, response.getCode());    	
    }
    
    @Test
    public void testVrfy() {
    	SmtpRequest request = new SmtpRequest(new Vrfy(), null, null);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(252, response.getCode());    	    	
    }
    
    @Test
    public void testExpn() {
    	SmtpRequest request = new SmtpRequest(new Expn(), null, null);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(252, response.getCode());    	    	
    }
    
    @Test
    public void testHelp() {
    	SmtpRequest request = new SmtpRequest(new Help(), null, null);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(211, response.getCode());    	    	
    }
    
    @Test
    public void testNoOp() {
    	SmtpRequest request = new SmtpRequest(new NoOp(), null, null);
    	SmtpResponse response = request.execute(messages);
    	assertEquals(250, response.getCode());    	    	
    }
    
    @Test
    public void testUnrecognizedCommandGreetState() {
        SmtpRequest request = new SmtpRequest(new Unrecognized(), null, SmtpState.GREET);
        SmtpResponse response = request.execute(messages);
        assertEquals(500, response.getCode());
    }

    @Test
    public void testUnrecognizedCommandMailState() {
        SmtpRequest request = new SmtpRequest(new Unrecognized(), null, SmtpState.MAIL);
        SmtpResponse response = request.execute(messages);
        assertEquals(500, response.getCode());
    }

    @Test
    public void testUnrecognizedCommandQuitState() {
        SmtpRequest request = new SmtpRequest(new Unrecognized(), null, SmtpState.QUIT);
        SmtpResponse response = request.execute(messages);
        assertEquals(500, response.getCode());
    }

    @Test
    public void testUnrecognizedCommandRcptState() {
        SmtpRequest request = new SmtpRequest(new Unrecognized(), null, SmtpState.RCPT);
        SmtpResponse response = request.execute(messages);
        assertEquals(500, response.getCode());
    }

    @Test
    public void testUnrecognizedCommandDataBodyState() {
        SmtpRequest request = new SmtpRequest(new Unrecognized(), null, SmtpState.DATA_BODY);
        SmtpResponse response = request.execute(messages);
        assertEquals(-1, response.getCode());
    }

    @Test
    public void testUnrecognizedCommandDataHdrState() {
        SmtpRequest request = new SmtpRequest(new Unrecognized(), null, SmtpState.DATA_HDR);
        SmtpResponse response = request.execute(messages);
        assertEquals(-1, response.getCode());
    }

}
