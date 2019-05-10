package com.dumbster.smtp.eml;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.MailMessageImpl;
import com.dumbster.smtp.mailstores.EMLMailStore;
import com.dumbster.smtp.mailstores.EMLMailStore.EMLFilenameFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.*;

public class EMLMailStoreTest {

    private EMLMailStore mailStore;
    private File emlStoreDir;

    @Before
    public void setup() {
        mailStore = new EMLMailStore();
        emlStoreDir = new File("build/test/eml_store_test" + String.valueOf(new Random().nextInt(1000000)));
        mailStore.setDirectory(emlStoreDir);
    }

    @After
    public void tearDown() {
        int count = 1;
        for (MailMessage message : mailStore.getMessages()) {
            String filename = mailStore.getFilename(message, count++);
            new File(emlStoreDir, filename).delete();
        }
        mailStore.clearMessages();
        deleteTheTwoMessages();

        emlStoreDir.delete();
    }

    @Test
    public void testNewMailStoreHasNoMail() {
        givenMailStoreDirectoryExists();

        givenMailStoreDirectoryIsEmpty();

        assertEquals(0, mailStore.getEmailCount());
    }

    @Test
    public void testNewMailStoreShouldLoadMessagesFromDirectory() {
        givenMailStoreDirectoryExists();

        givenMailStoreDirectoryHasTwoMessages();

        assertEquals(2, mailStore.getEmailCount());
    }

    private void deleteTheTwoMessages() {
        new File(emlStoreDir, "1_message.eml").delete();
        new File(emlStoreDir, "2_message.eml").delete();
    }


    @Test
    public void testDirectoryShouldBeCreated() {
        givenMailStoreDirectoryDoesNotExist();

        whenAMessageIsAdded();

        directoryShouldBeCreatedAutomatically();

        messageFileShouldExist();
    }

    @Test
    public void testMessageIsLoadedProperly() {
        givenMailStoreDirectoryExists();

        givenMailStoreDirectoryIsEmpty();

        MailMessage message = new MailMessageImpl();
        message.addHeader("Message-ID", "<10298244.21372804359732.JavaMail.test@localhost>");
        message.addHeader("From", "<my_email@localhost.com>");
        message.addHeader("To", "email@localhost.com");
        message.addHeader("Subject", "The email subject");
        message.appendBody("This is the body");

        mailStore.addMessage(message);

        MailMessage storedMessage = mailStore.getMessage(0);

        assertEquals("<10298244.21372804359732.JavaMail.test@localhost>", storedMessage.getFirstHeaderValue("Message-ID"));
        assertEquals("<my_email@localhost.com>", storedMessage.getFirstHeaderValue("From"));
        assertEquals("email@localhost.com", storedMessage.getFirstHeaderValue("To"));
        assertEquals("The email subject", storedMessage.getFirstHeaderValue("Subject"));
        assertEquals("This is the body", storedMessage.getBody());
    }

    @Test
    public void testEMLFilenameFilter() {
        EMLFilenameFilter filter = new EMLFilenameFilter();

        assertTrue(filter.accept(null, "1_something.eml"));
        assertTrue(filter.accept(null, "1_something with spaces.eml"));
        assertTrue(filter.accept(null, "1_.eml"));
        assertTrue(filter.accept(null, "982734987235_.eml"));
        assertTrue(filter.accept(null, "982734987235_1234_something_1234.eml"));
        assertFalse(filter.accept(null, "_.eml"));
        assertFalse(filter.accept(null, "2 not_matching.eml"));
        assertFalse(filter.accept(null, "3 not_matching eml"));
        assertFalse(filter.accept(null, "not_matching.eml"));
        assertFalse(filter.accept(null, "1_something.txt"));
        assertFalse(filter.accept(null, "1_something with spaces.txt"));
    }

    @Test
    public void testAddOneMessageLeavesOneMail() {
        givenMailStoreDirectoryExists();

        givenMailStoreDirectoryIsEmpty();

        whenAMessageIsAdded();

        assertEquals(1, mailStore.getEmailCount());
    }

    @Test
    public void testNewMailStoreHasEmptyMailList() {
        givenMailStoreDirectoryExists();

        givenMailStoreDirectoryIsEmpty();

        assertEquals(0, mailStore.getMessages().length);
    }

    @Test
    public void testAddOneMessageLeavesOneMailInMailMessagesArray() {
        givenMailStoreDirectoryExists();

        givenMailStoreDirectoryIsEmpty();

        whenAMessageIsAdded();

        assertEquals(1, mailStore.getMessages().length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGettingMailFromEmptyMailStoreThrowsIndexOutOfBounds() {
        givenMailStoreDirectoryExists();

        givenMailStoreDirectoryIsEmpty();

        mailStore.getMessage(0);
    }

    @Test
    public void testGettingMail0FromMailStoreWithAnItemWorks() {
        whenAMessageIsAdded();
        assertNotNull(mailStore.getMessage(0));
    }

    /*
     * BDD methods.
     */

    private void givenMailStoreDirectoryIsEmpty() {
        File[] files = emlStoreDir.listFiles();
        for (File file : files) {
            file.delete();
        }
    }
    private void givenMailStoreDirectoryDoesNotExist() {
        if (emlStoreDir.exists()) {
            for (File file : emlStoreDir.listFiles()) {
                file.delete();
            }
            emlStoreDir.delete();
        }
    }
    private void givenMailStoreDirectoryExists() {
        if (!emlStoreDir.exists()) {
            emlStoreDir.mkdirs();
        }
    }
    private void givenMailStoreDirectoryHasTwoMessages() {
        givenMailStoreDirectoryIsEmpty();
        try {

            File file1 = new File(emlStoreDir, "1_message.eml");
            file1.createNewFile();

            File file2 = new File(emlStoreDir, "2_message.eml");
            file2.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private void whenAMessageIsAdded() {
        MailMessage message = new MailMessageImpl();
        mailStore.addMessage(message);
    }

    private void directoryShouldBeCreatedAutomatically() {
        assertTrue(emlStoreDir.exists());
    }

    private void messageFileShouldExist() {
        assertTrue(emlStoreDir.listFiles().length == 1);
    }
}
