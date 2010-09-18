package com.dumbster.smtp.action;

import com.dumbster.smtp.*;
import java.util.List;

public interface Action {
	
	public abstract boolean isStateless();

	public abstract String toString();
	
	public abstract Response response(SmtpState smtpState, List<MailMessage> messages, MailMessage currentMessage);
	
}
