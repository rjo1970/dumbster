package com.dumbster.smtp;

import com.dumbster.smtp.mailstores.EMLMailStore;
import com.dumbster.smtp.mailstores.RollingMailStore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * User: rj
 * Date: 7/21/13
 * Time: 8:20 AM
 */
public class ServerOptionsTest {

    private ServerOptions options;


    @Test
    public void defaultConfiguration() {
        options = new ServerOptions();
        assertEquals(true, options.valid);
        assertEquals(25, options.port);
        assertEquals(true, options.threaded);
        assertEquals(RollingMailStore.class, options.mailStore.getClass());
    }

    @Test
    public void emptyOptions() {
        String[] args = new String[]{};
        options = new ServerOptions(args);
        assertEquals(true, options.valid);
        assertEquals(25, options.port);
        assertEquals(true, options.threaded);
        assertEquals(RollingMailStore.class, options.mailStore.getClass());
    }

    @Test
    public void optionMailStoreEMLMailStore() {
        String[] args = new String[]{"--mailStore=EMLMailStore"};
        options = new ServerOptions(args);
        assertEquals(EMLMailStore.class, options.mailStore.getClass());
        assertEquals(true, options.valid);
        assertEquals(25, options.port);
        assertEquals(true, options.threaded);
    }

    @Test
    public void optionMailStoreInvalid() {
        String[] args = new String[]{"--mailStore"};
        options = new ServerOptions(args);
        assertEquals(false, options.valid);
    }

    @Test
    public void badMailStore() {
        String[] args = new String[]{"--mailStore=foo"};
        options = new ServerOptions(args);
        assertEquals(RollingMailStore.class, options.mailStore.getClass());
        assertEquals(false, options.valid);
    }

    @Test
    public void threaded() {
        String[] args = new String[]{"--threaded"};
        options = new ServerOptions(args);
        assertEquals(true, options.threaded);
        assertEquals(true, options.valid);
        assertEquals(25, options.port);
        assertEquals(RollingMailStore.class, options.mailStore.getClass());
    }

    @Test
    public void notThreaded() {
        String[] args = new String[]{"--threaded=false"};
        options = new ServerOptions(args);
        assertEquals(false, options.threaded);
        assertEquals(true, options.valid);
        assertEquals(25, options.port);
        assertEquals(RollingMailStore.class, options.mailStore.getClass());
    }

    @Test
    public void alternativePort() {
        String[] args = new String[]{"12345"};
        options = new ServerOptions(args);
        assertEquals(12345, options.port);
        assertEquals(true, options.threaded);
        assertEquals(true, options.valid);
        assertEquals(RollingMailStore.class, options.mailStore.getClass());
    }

    @Test
    public void badPort() {
        String[] args = new String[]{"invalid"};
        options = new ServerOptions(args);
        assertEquals(false, options.valid);
    }


}
