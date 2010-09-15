package com.dumbster.smtp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public interface SmtpIOSource {

	public BufferedReader getInputStream() throws IOException;
	public PrintWriter getOutputStream() throws IOException;
	public void close() throws IOException;
	
}
