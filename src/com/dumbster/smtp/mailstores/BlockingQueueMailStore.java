/**
 * 
 */
package com.dumbster.smtp.mailstores;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.MailStore;
import com.dumbster.smtp.ServerOptions;
import com.dumbster.smtp.SmtpServerFactory;

/**
 * MailStore which is backed by a BlockingQueue.
 * 
 * If you want to test asynchronous mail transport you can use
 * the queue's {@link BlockingQueue#take()} method.
 *
 * @author markus@malkusch.de
 * @see BlockingQueue
 * @see SmtpServerFactory#startServer(ServerOptions)
 * @see ServerOptions#mailStore
 */
public class BlockingQueueMailStore implements MailStore {
	
	private BlockingQueue<MailMessage> queue;
	
	private long timeout;
	
	private TimeUnit timeoutUnit;
	
	/**
	 * Initializes with a LinkedBlockingQueue.
	 * 
	 * @see LinkedBlockingQueue
	 */
	public BlockingQueueMailStore() {
		this(new LinkedBlockingQueue<MailMessage>());
	}
	
	/**
	 * Sets the Queue
	 */
	public BlockingQueueMailStore(BlockingQueue<MailMessage> queue) {
		this.queue = queue;
	}
	
	/**
	 * Returns the queue
	 */
	public BlockingQueue<MailMessage> getQueue() {
		return queue;
	}
	
	/**
	 * Sets an optional timeout for adding to the queue.
	 * 
	 * Set timeout to 0 if you don't want a timeout.
	 */
	public void setTimeout(long timeout, TimeUnit unit) {
		this.timeout = timeout;
		this.timeoutUnit = unit;
	}
	
	/**
	 * Returns whether adding to the queue has a timeout.
	 */
	public boolean isTimeout() {
		return timeout != 0;
	}
	
	@Override
	public int getEmailCount() {
		return this.queue.size();
	}

	@Override
	public void addMessage(MailMessage message) {
		try {
			if (isTimeout()) {
				queue.offer(message, timeout, timeoutUnit);
				
			} else {
				queue.put(message);
				
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			
		}
	}

	@Override
	public MailMessage[] getMessages() {
		return queue.toArray(new MailMessage[]{});
	}

	@Override
	public MailMessage getMessage(int index) {
		return getMessages()[index];
	}

	@Override
	public void clearMessages() {
		queue.clear();
	}

}
