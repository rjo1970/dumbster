package com.dumbster.smtp.action;

import com.dumbster.smtp.*;
import java.util.List;

public abstract class AbstractAction {
	
	public abstract boolean isStateless();

	public abstract String toString();
	
	public abstract SmtpResponse response(SmtpState smtpState, List<SmtpMessage> messages, SmtpMessage currentMessage);
	
}
