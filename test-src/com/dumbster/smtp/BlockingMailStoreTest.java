package com.dumbster.smtp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.dumbster.smtp.mailstores.BlockingQueueMailStore;

/**
 * Tests the BlockingQueueMailStore
 *
 * @author markus@malkusch.de
 * @see BlockingQueueMailStore
 */
public class BlockingMailStoreTest {
	
	private BlockingQueueMailStore mailStore;
	
	@Before
	public void setup() {
		mailStore = new BlockingQueueMailStore();
	}
	
	@Test
	public void testGetMessage() {
		MailMessage message0 = addAMessage();
		assertSame(message0, mailStore.getMessage(0));
		
		MailMessage message1 = addAMessage();
		MailMessage message2 = addAMessage();
		
		assertSame(message0, mailStore.getMessage(0));
		assertSame(message1, mailStore.getMessage(1));
		assertSame(message2, mailStore.getMessage(2));
	}
	
	@Test
	public void testTakeAfterAddMessage() throws InterruptedException {
		MailMessage message = addAMessage();
		assertSame(message, mailStore.getQueue().take());
	}
	
	@Test
	public void testTakeBeforeAddMessage() throws InterruptedException {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				addAMessage();
			}
		}).start();
		
		assertEquals(0, mailStore.getEmailCount());
		assertNotNull(mailStore.getQueue().take());
	}
	
	@Test
	public void testGetMessagesPreseversQueue() {
		MailMessage[] messages = new MailMessage[3];
		messages[0] = addAMessage();
		assertArrayEquals(Arrays.copyOf(messages, 1), mailStore.getMessages());
		
		messages[1] = addAMessage();
		messages[2] = addAMessage();
		assertArrayEquals(Arrays.copyOf(messages, 3), mailStore.getMessages());
	}
	
	@Test
	public void testTakeDecreasesEmailCount() throws InterruptedException {
		addAMessage();
		addAMessage();
		addAMessage();
		assertEquals(3, mailStore.getEmailCount());
		
		mailStore.getQueue().take();
		assertEquals(2, mailStore.getEmailCount());
		
		mailStore.getQueue().take();
		assertEquals(1, mailStore.getEmailCount());
		
		mailStore.getQueue().take();
		assertEquals(0, mailStore.getEmailCount());
	}
	
	private MailMessage addAMessage() {
        MailMessage message = new MailMessageImpl();
        mailStore.addMessage(message);
        return message;
    }

}
