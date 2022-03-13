package de.crbk.minesweeper.net.messages;

import java.io.Serial;
import java.io.Serializable;

public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 5764227949801008453L;

    private final String username;
    private final MessageType messageType;
    private final Serializable content;

    Message(final String pUsername, final MessageType pMessageType, final Serializable pContent) {
        username = pUsername;
        messageType = pMessageType;
        content = pContent;
    }
    
    @Override
    public String toString() {
        return String.format(
                "%s [%s] %s",
                ((username != null) && (!username.isEmpty())) ? "[" + username + "]" : "",
                messageType.name(),
                content != null ? content.toString() : "*null*"
        );
    }
    
    public String getUsername() {
        return username;
    }
    
    public MessageType getMessageType() {
        return messageType;
    }
    
    public Serializable getContent() {
        return content;
    }
    
}
