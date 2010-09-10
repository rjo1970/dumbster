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

import junit.framework.TestCase;

public class SmtpRequestTest extends TestCase {

  public void testUnrecognizedCommandConnectState() {
    SmtpRequest request = new SmtpRequest(SmtpActionType.UNRECOG, null, SmtpState.CONNECT);
    SmtpResponse response = request.execute();
    assertTrue(response.getCode() == 500);
  }

  public void testUnrecognizedCommandGreetState() {
    SmtpRequest request = new SmtpRequest(SmtpActionType.UNRECOG, null, SmtpState.GREET);
    SmtpResponse response = request.execute();
    assertTrue(response.getCode() == 500);
  }

  public void testUnrecognizedCommandMailState() {
    SmtpRequest request = new SmtpRequest(SmtpActionType.UNRECOG, null, SmtpState.MAIL);
    SmtpResponse response = request.execute();
    assertTrue(response.getCode() == 500);
  }

  public void testUnrecognizedCommandQuitState() {
    SmtpRequest request = new SmtpRequest(SmtpActionType.UNRECOG, null, SmtpState.QUIT);
    SmtpResponse response = request.execute();
    assertTrue(response.getCode() == 500);
  }

  public void testUnrecognizedCommandRcptState() {
    SmtpRequest request = new SmtpRequest(SmtpActionType.UNRECOG, null, SmtpState.RCPT);
    SmtpResponse response = request.execute();
    assertTrue(response.getCode() == 500);
  }

  public void testUnrecognizedCommandDataBodyState() {
    SmtpRequest request = new SmtpRequest(SmtpActionType.UNRECOG, null, SmtpState.DATA_BODY);
    SmtpResponse response = request.execute();
    assertTrue(response.getCode() == -1);
  }

  public void testUnrecognizedCommandDataHdrState() {
    SmtpRequest request = new SmtpRequest(SmtpActionType.UNRECOG, null, SmtpState.DATA_HDR);
    SmtpResponse response = request.execute();
    assertTrue(response.getCode() == -1);
  }


}
