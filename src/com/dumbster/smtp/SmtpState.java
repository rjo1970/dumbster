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

/**
 * SMTP server state.
 */
public enum SmtpState {
  CONNECT ((byte) 1, "CONNECT"),
  GREET ((byte) 2, "GREET"),
  MAIL ((byte) 3, "MAIL"),
  RCPT ((byte) 4, "RCPT"),
  DATA_HDR ((byte) 5, "DATA_HDR"),
  DATA_BODY ((byte) 6, "DATA_BODY"),
  QUIT ((byte) 7, "QUIT");


    private byte value;
    private String description;

    SmtpState(byte value, String description) {
        this.value = value;
        this.description = description;
    }

    public byte getValue() {
	      return this.value;
    }

    public String toString() {
	      return this.description;
    }
}
