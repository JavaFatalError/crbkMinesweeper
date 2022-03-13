package de.crbk.minesweeper.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;

public interface Connection extends Closeable {

    void transmit(final Serializable pSerializable) throws IOException;

    <T> T receive() throws IOException;

    boolean isClosed();

}
