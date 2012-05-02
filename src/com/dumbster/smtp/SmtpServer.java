/*
 * Dumbster - a dummy SMTP server
 * Copyright 2004 Jason Paul Kitchen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dumbster.smtp;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * Dummy SMTP server for testing purposes.
 */
public class SmtpServer implements Runnable {
    public static final String PROP_NUM_THREADS = "dumbster.numThreads";
    public static final String DEFAULT_THREADS = "1"; // as implemented by rjo

    public static final int DEFAULT_SMTP_PORT = 25;
    private static final int SERVER_SOCKET_TIMEOUT = 5000;
    private static final int MAX_THREADS = 10;

    private volatile MailStore mailStore = new NullMailStore();
    private volatile boolean stopped = true;
    private volatile boolean ready = false;
    private volatile boolean threaded = false;

    private ServerSocket serverSocket;
    private int port;
    private ThreadPoolExecutor executor = null;
    private int threadCount = 1;

    SmtpServer(int port) {
        this.port = port;
        String configThreads = System.getProperty(PROP_NUM_THREADS, DEFAULT_THREADS);
        try {
            threadCount = Integer.parseInt(configThreads);
            threadCount = Math.max(threadCount, 1);
            if (threadCount > MAX_THREADS) {
                threadCount = MAX_THREADS;
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            threadCount = Integer.parseInt(DEFAULT_THREADS);
        }
        executor = new ThreadPoolExecutor(threadCount, threadCount, 5, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
    }

    public boolean isReady() {
        return ready;
    }

    @Override
    public void run() {
        stopped = false;
        try {
            initializeServerSocket();
            serverLoop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ready = false;
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initializeServerSocket() throws Exception {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(SERVER_SOCKET_TIMEOUT);
    }

    private void serverLoop() throws IOException {
        while (!isStopped()) {
            SocketWrapper source = new SocketWrapper(clientSocket());
            ClientSession session = new ClientSession(source, mailStore);
            executor.execute(session);
        }
        ready = false;
    }

    private Socket clientSocket() throws IOException {
        Socket socket = null;
        while (socket == null) {
            socket = accept();
        }
        return socket;
    }

    private Socket accept() {
        try {
            ready = true;
            return serverSocket.accept();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isStopped() {
        return stopped;
    }

    public synchronized void stop() {
        stopped = true;
        try {
            serverSocket.close();
        } catch (IOException ignored) {
        }
    }

    public MailMessage[] getMessages() {
        return mailStore.getMessages();
    }

    public MailMessage getMessage(int i) {
        return mailStore.getMessage(i);
    }

    public int getEmailCount() {
        return mailStore.getEmailCount();
    }

    public void anticipateMessageCountFor(int messageCount, int ticks) {
        int tickdown = ticks;
        while (mailStore.getEmailCount() < messageCount && tickdown > 0) {
            tickdown--;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    /**
     * Toggles if the SMTP server is single or multi-threaded for response to
     * SMTP sessions.
     *
     * @param threaded whether or not to allow multiple simultaneous connections
     */
    public void setThreaded(boolean threaded) {
        /* The problem here is that we might start out as a single thread and then later get told to be multi-threaded.
         * The solution ought to be something like, "Oh, we're switching state so we need to resize our executor's thread pool."
         */
        if (threaded != this.threaded) {
            // we're changing something
            if (threaded) {
                executor.setMaximumPoolSize(threadCount);
                executor.setCorePoolSize(threadCount);
            } else {
                executor.setCorePoolSize(1);
                executor.setMaximumPoolSize(1);
            }
            this.threaded = threaded;
        }
    }

    public void setMailStore(MailStore mailStore) {
        this.mailStore = mailStore;
    }

    public void clearMessages() {
        this.mailStore.clearMessages();
    }
}
