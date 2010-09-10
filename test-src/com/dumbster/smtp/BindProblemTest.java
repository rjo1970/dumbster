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

/**
 * @author JeremyH
 */
public class BindProblemTest extends TestCase {

  private SimpleSmtpServer server;

  /**
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    server = SimpleSmtpServer.start();
  }

  /**
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    server.stop();
    super.tearDown();
  }

  public void test1() {
    assertTrue(true);
  }

  public void test2() {
    assertTrue(true);
  }

  public void test3() {
    assertTrue(true);
  }

  public void test4() {
    assertTrue(true);
  }

  public void test5() {
    assertTrue(true);
  }
}

