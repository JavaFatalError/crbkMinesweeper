package de.crbk.minesweeper.core;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

@SuppressWarnings({"SpellCheckingInspection", "HardCodedStringLiteral"})
public enum Status {
    
    EMPTY(loadImage("/de/crbk/minesweeper/0.png")),
    MINE(loadImage("/de/crbk/minesweeper/9.png")),
    ONE_CLOSE(loadImage("/de/crbk/minesweeper/1.png")),
    TWO_CLOSE(loadImage("/de/crbk/minesweeper/2.png")),
    THR_CLOSE(loadImage("/de/crbk/minesweeper/3.png")),
    FOU_CLOSE(loadImage("/de/crbk/minesweeper/4.png")),
    FIV_CLOSE(loadImage("/de/crbk/minesweeper/5.png")),
    SIX_CLOSE(loadImage("/de/crbk/minesweeper/6.png")),
    SEV_CLOSE(loadImage("/de/crbk/minesweeper/7.png")),
    EIG_CLOSE(loadImage("/de/crbk/minesweeper/8.png")),
    COVERED(loadImage("/de/crbk/minesweeper/10.png")),
    MARKED(loadImage("/de/crbk/minesweeper/11.png")),
    WRONG_MARK(loadImage("/de/crbk/minesweeper/12.png")),
    ;

    public static final int ICON_WIDTH = 15;
    public static final int ICON_HEIGHT = 15;

    private final Image image;
    
    Status(final Image pImage) {
        image = pImage;
    }
    
    public Image getImage() {
        return image;
    }
    
    private static Image loadImage(final String aFilePath) {
        final BufferedImage bufferedImage;
        try {
            final InputStream resourceAsStream = Status.class.getResourceAsStream(aFilePath);
            if (resourceAsStream == null) {
                throw new IllegalStateException(
                        MessageFormat.format("could not load resource ''{0}''", aFilePath)
                );
            }

            bufferedImage = ImageIO.read(resourceAsStream);
        } catch (final IOException e) {
            throw new RuntimeException(
                    MessageFormat.format("failed to load image ''{0}''", aFilePath)
            );
        }

        final ImageIcon imageIcon = new ImageIcon(bufferedImage);
        return imageIcon.getImage();
    }
}
