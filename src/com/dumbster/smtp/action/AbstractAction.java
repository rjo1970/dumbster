package com.dumbster.smtp.action;

import com.dumbster.smtp.*;

public abstract class AbstractAction {
	
	public abstract boolean isStateless();

	public abstract String toString();
	
	public abstract SmtpResponse response(SmtpState smtpState);
	
}
