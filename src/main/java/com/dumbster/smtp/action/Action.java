package com.dumbster.smtp.action;

import com.dumbster.smtp.*;

public interface Action {

    public abstract String toString();

    public abstract Response response(SmtpState smtpState, MailStore mailStore, MailMessage currentMessage);

}
