package com.dumbster.smtp.action;

import com.dumbster.smtp.*;
import java.util.List;

public interface Action {
	
	public abstract boolean isStateless();

	public abstract String toString();
	
	public abstract SmtpResponse response(SmtpState smtpState, List<SmtpMessage> messages, SmtpMessage currentMessage);
	
}
