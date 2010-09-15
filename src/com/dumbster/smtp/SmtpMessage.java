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

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

/**
 * Container for a complete SMTP message - headers and message body.
 */
public class SmtpMessage {
	private Map<String, List<String>> headers;
	private StringBuffer body;

	public SmtpMessage() {
		headers = new HashMap<String, List<String>>(10);
		body = new StringBuffer();
	}

	public Iterator<String> getHeaderNames() {
		Set<String> nameSet = headers.keySet();
		return nameSet.iterator();
	}

	public String[] getHeaderValues(String name) {
		List<String> values = (List<String>) headers.get(name);
		if (values == null) {
			return new String[0];
		} else {
			return (String[]) values.toArray(new String[0]);
		}
	}

	public String getFirstHeaderValue(String name) {
		List<String> values = (List<String>) headers.get(name);
		if (values == null) {
			return null;
		} else {
			Iterator<String> iterator = values.iterator();
			return (String) iterator.next();
		}
	}

	public String getBody() {
		return body.toString();
	}

	public void addHeader(String name, String value) {
		List<String> valueList = (List<String>) headers.get(name);
		if (valueList == null) {
			valueList = new ArrayList<String>(1);
		}
		valueList.add(value);
		headers.put(name, valueList);
	}
	
	public void appendBody(String line) {
		body.append(line);
	}

	public String toString() {
		StringBuffer msg = new StringBuffer();
		for (Iterator<String> i = headers.keySet().iterator(); i.hasNext();) {
			String name = (String) i.next();
			List<String> values = (List<String>) headers.get(name);
			for (Iterator<String> j = values.iterator(); j.hasNext();) {
				String value = (String) j.next();
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
}
