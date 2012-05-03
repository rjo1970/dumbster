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

import java.util.*;

/**
 * Container for a complete SMTP message - headers and message body.
 */
public class MailMessageImpl implements MailMessage {
    private Map<String, List<String>> headers;
    private StringBuffer body;
    private final UUID uuid = UUID.randomUUID();

    public MailMessageImpl() {
        headers = new HashMap<String, List<String>>(10);
        body = new StringBuffer();
    }

    @Override
    public Iterator<String> getHeaderNames() {
        Set<String> nameSet = headers.keySet();
        return nameSet.iterator();
    }

    @Override
    public String[] getHeaderValues(String name) {
        List<String> values = headers.get(name);
        if (values == null) {
            return new String[0];
        } else {
            return values.toArray(new String[values.size()]);
        }
    }

    @Override
    public String getFirstHeaderValue(String name) {
        List<String> values = headers.get(name);
        if (values == null) {
            return null;
        } else {
            Iterator<String> iterator = values.iterator();
            return iterator.next();
        }
    }

    @Override
    public String getBody() {
        return body.toString();
    }

    @Override
    public void addHeader(String name, String value) {
        List<String> valueList = headers.get(name);
        if (valueList == null) {
            valueList = new ArrayList<String>(1);
        }
        valueList.add(value);
        headers.put(name, valueList);
    }

    @Override
    public void appendHeader(String name, String value) {
        List<String> values = headers.get(name);
        String oldValue = values.get(values.size()-1);
        values.remove(oldValue);
        values.add(oldValue + value);
        headers.put(name, values);
    }

    @Override
    public void appendBody(String line) {
        body.append(line);
    }

    @Override
    public String getUID() {
        return uuid.toString();
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        for (String name : headers.keySet()) {
            List<String> values = headers.get(name);
            for (String value : values) {
                msg.append(name);
                msg.append(": ");
                msg.append(value);
                msg.append('\n');
            }
        }
        msg.append('\n');
        msg.append(body);
        msg.append('\n');
        return msg.toString();
    }

    @Override
    public String byteStuff() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> e : headers.entrySet()) {
            String name = e.getKey();
            sb.append(name).append(": ");
            for (Iterator<String> viter = e.getValue().iterator(); viter.hasNext(); ) {
                sb.append(viter.next());
                if (viter.hasNext()) {
                    sb.append(", ");
                }
            }
            sb.append("\r\n");
        }
        sb.append("\r\n");
        String body = getBody();

        // headers are done, now it's time to build the body.
//        POP3 says we're supposed to "byte stuff" any termination sequence (CRLF.CRLF) that appears in the message
//        but when we do that then Apple's Mail doesn't un-stuff the dots. It may be that Mail is b0rk3n, but
//        since that's what I'm using on my test system, I'm not bothered. I would LOVE if someone could point
//        me to a comprehensible explanation of how this is really supposed to work.
        sb.append(body);
        // finally, the termination sequence
        sb.append("\r\n.\r\n");
        return sb.toString();
    }
}
