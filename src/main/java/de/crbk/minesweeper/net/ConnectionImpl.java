package de.crbk.minesweeper.net;

import de.crbk.minesweeper.net.messages.MessageFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class ConnectionImpl implements Connection {

    private final Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    private final String username;
    private final MessageFactory messageFactory;

    public ConnectionImpl(final String pHost, final int pPort, final String pUsername) throws IOException {
        username = pUsername;
        socket = new Socket(pHost, pPort);

        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());

        messageFactory = new MessageFactory(username);
    }

    @Override
    public void transmit(final Serializable pSerializable) throws IOException {
        outputStream.writeObject(pSerializable);
        outputStream.flush();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T receive() throws IOException {
        try {
            return (T) inputStream.readObject();
        } catch (final ClassNotFoundException pE) {
            throw new RuntimeException(pE);
        }
    }

    @Override
    public boolean isClosed() {
        return socket.isClosed();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    public String getUsername() {
        return username;
    }
}
