package de.crbk.minesweeper.net;

import de.crbk.minesweeper.net.messages.Message;
import de.crbk.minesweeper.net.messages.MessageFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Server {

    protected final Logger logger = Logger.getLogger(getClass().getName());

    private final int port;

    private ServerSocket serverSocket;
    private List<Client> clients;

    private LoginListener loginListener;

    public Server(final int pPort) {
        port = pPort;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        clients = new LinkedList<>();

        loginListener = new LoginListener();
        loginListener.start();
    }

    public void stop() throws IOException {
        final MessageFactory messageFactory = new MessageFactory("SERVER");
        final Message logoutMessage = messageFactory.createLogoutMessage();
        for (final Client client : clients) {
            client.transmit(logoutMessage);
        }

        loginListener.kill();
        loginListener.interrupt();

        clients.clear();
        serverSocket.close();
    }

    protected abstract void handleLoginEvent(final Client pClient);

    protected abstract void handleLogoutEvent(final Client pClient);

    protected abstract void handleIncomingMessage(final Client pClient, final Message pMessage);

    private class LoginListener extends Thread {

        private boolean keepAlive;

        @Override
        public void run() {
            keepAlive = true;
            while (keepAlive && !serverSocket.isClosed()) {
                try {
                    final Socket socket = serverSocket.accept();
                    final ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    final ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                    final Client client = new Client(socket, objectOutputStream, objectInputStream);
                    clients.add(client);
                } catch (final IOException pE) {
                    logger.log(
                            Level.SEVERE,
                            "an exception was thrown while connection a new client",
                            pE
                    );
                }
            }
        }

        private void kill() {
            keepAlive = false;
        }

    }

    protected final class Client extends Thread implements Connection {

        private String username;

        private final Socket socket;
        private final ObjectOutputStream outputStream;
        private final ObjectInputStream inputStream;

        private Client(final Socket pSocket, final ObjectOutputStream pOutputStream, final ObjectInputStream pInputStream) {
            socket = pSocket;
            outputStream = pOutputStream;
            inputStream = pInputStream;
        }

        @Override
        public void run() {
            while (isClosed()) {
                try {
                    final Message message = receive();

                    if (message == null) {
                        continue;
                    }

                    switch (message.getMessageType()) {
                        case LOGIN:
                            username = (String) message.getContent();
                            handleLoginEvent(this);
                            break;
                        case LOGOUT:
                            close();
                            handleLogoutEvent(this);
                            break;
                        case FIELD_MARKED:
                        case FIELD_UNCOVERED:
                        case TEXT:
                        default:
                            handleIncomingMessage(this, message);
                            break;
                    }
                } catch (final IOException pE) {
                    logger.log(
                            Level.SEVERE,
                            "an exception occurred receiving a message",
                            pE
                    );
                }
            }
        }

        @Override
        public void transmit(final Serializable pSerializable) throws IOException {
            outputStream.writeObject(pSerializable);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T receive() throws IOException {
            try {
                return ((T) inputStream.readObject());
            } catch (final ClassNotFoundException ignored) {
                return null;
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

        public String getUsername() {
            return username;
        }
    }

}
