package com.dumbster.smtp;

import org.junit.*;

import static org.junit.Assert.*;

public class SmtpStateTest {

    @Test
    public void testToString() {
        SmtpState state = SmtpState.CONNECT;
        assertEquals("CONNECT", state.toString());
    }

}
