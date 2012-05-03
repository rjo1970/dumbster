package com.dumbster.smtp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketWrapper implements IOSource {
    private Socket socket;

    public SocketWrapper(Socket socket)  {
        this.socket = socket;
    }

    @Override
    public BufferedReader getInputStream() throws IOException {
        return new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public PrintWriter getOutputStream() throws IOException {
        return new PrintWriter(socket.getOutputStream());
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

}
