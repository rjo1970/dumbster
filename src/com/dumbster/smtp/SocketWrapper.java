package com.dumbster.smtp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketWrapper implements IOSource {
    private Socket socket;

    public SocketWrapper(Socket socket) throws IOException {
        this.socket = socket;
        this.socket.setSoTimeout(10000); // protects against hanged clients
    }

    public BufferedReader getInputStream() throws IOException {
        return new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
    }

    public PrintWriter getOutputStream() throws IOException {
        return new PrintWriter(socket.getOutputStream());
    }

    public void close() throws IOException {
        socket.close();
    }

}
