package com.dumbster.pop;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.dumbster.smtp.MailStore;
import com.dumbster.smtp.NullMailStore;
import com.dumbster.smtp.SmtpServer;
import com.dumbster.smtp.SocketWrapper;

public class POPServer implements Runnable {
    public static final int DEFAULT_POP_PORT = 110;

    private static final int SERVER_SOCKET_TIMEOUT = 5000;
    private static final int MAX_THREADS = 10;

    private volatile MailStore _mailstore = new NullMailStore();
    private volatile boolean _stopped = true;
    private volatile boolean _ready = false;
    private volatile boolean _threaded = false;

    private ServerSocket _serverSocket;
    private int _port;
    private ThreadPoolExecutor _executor = null;
    private int _threadCount = 1;

    POPServer(int port) {
        _port = port;
        String configThreads = System.getProperty(SmtpServer.PROP_NUM_THREADS, SmtpServer.DEFAULT_THREADS);
        try {
            _threadCount = Integer.parseInt(configThreads);
            _threadCount = Math.max(_threadCount, 1);
            if (_threadCount > MAX_THREADS) {
                _threadCount = MAX_THREADS;
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            _threadCount = Integer.parseInt(SmtpServer.DEFAULT_THREADS);
        }
        // It would probably be nice if the thread factory named the threads things like, "POP thread xx"
        _executor = new ThreadPoolExecutor(_threadCount, _threadCount, 5, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
    }

    public boolean isReady() {
        return _ready;
    }

    @Override
    public void run() {
        _stopped = false;
        try {
            initializeServerSocket();
            serverLoop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            _ready = false;
            if (_serverSocket != null) {
                try {
                    _serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initializeServerSocket() throws Exception {
        _serverSocket = new ServerSocket(_port);
        _serverSocket.setSoTimeout(SERVER_SOCKET_TIMEOUT);
    }

    private void serverLoop() throws IOException {
        while (!isStopped()) {
            SocketWrapper source = new SocketWrapper(clientSocket());
            ClientSession session = new ClientSession(source, _mailstore);
            _executor.execute(session);
        }
        _ready = false;
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
            _ready = true;
            return _serverSocket.accept();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isStopped() {
        return _stopped;
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
        if (threaded != _threaded) {
            // we're changing something
            if (threaded) {
                _executor.setMaximumPoolSize(_threadCount);
                _executor.setCorePoolSize(_threadCount);
            } else {
                _executor.setCorePoolSize(1);
                _executor.setMaximumPoolSize(1);
            }
            _threaded = threaded;
        }
    }

    public void setMailStore(MailStore mailStore) {
        _mailstore = mailStore;
    }
}
