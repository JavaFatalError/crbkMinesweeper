package de.crbk.minesweeper.net.messages;

import de.crbk.minesweeper.core.Field;

public class MessageFactory {

    private final String username;

    public MessageFactory(final String pUsername) {
        username = pUsername;
    }

    public Message createLoginMessage() {
        return new Message(username, MessageType.LOGIN, username);
    }

    public Message createLogoutMessage() {
        return new Message(username, MessageType.LOGOUT, null);
    }

    public Message createTextMessage(final String pText) {
        return new Message(username, MessageType.TEXT, pText);
    }

    public Message createFieldMarkedMessage(final Field pField) {
        return new Message(username, MessageType.FIELD_MARKED, pField);
    }

    public Message createFieldUncoveredMessage(final Field pField) {
        return new Message(username, MessageType.FIELD_UNCOVERED, pField);
    }

    public String getUsername() {
        return username;
    }

}
