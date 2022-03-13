package de.crbk.minesweeper.swing;

import de.crbk.minesweeper.core.Board;
import de.crbk.minesweeper.core.Field;
import de.crbk.minesweeper.core.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SwingBoard extends JComponent {
    
    public enum GameStatus {
        GAME_RUNNING(""),
        GAME_OVER("Game Over"),
        GAME_WON("Game Won"),
        ;
        
        private final String message;
    
        GameStatus(final String pMessage) {
            message = pMessage;
        }
        
        public boolean isRunning() {
            return this == GAME_RUNNING;
        }
    
        public boolean isOver() {
            return this == GAME_OVER || this == GAME_WON;
        }
        
        public String getMessage() {
            return message;
        }
        
    }

    private final Logger logger = Logger.getLogger(SwingBoard.class.getName());

    private final Board board;
    private GameStatus gameStatus;
    
    private int markedFields;
    
    SwingBoard(final int pWidth, final int pHeight, final int pMineCount) {
        board = new Board(pWidth, pHeight, pMineCount);
        board.build();
        
        gameStatus = GameStatus.GAME_RUNNING;
        markedFields = 0;
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                
                if (gameStatus.isOver()) {
                    return;
                }
                
                final int x = e.getX();
                final int y = e.getY();
    
                final int fieldWidth = getWidth() / board.getWidth();
                final int fieldHeight = getHeight() / board.getHeight();
                
                final Field field = board.getField( x / fieldWidth, y / fieldHeight);

                logger.info(e.toString());
                logger.info(field.toString());
                
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if(!field.isHidden()) {
                        return;
                    }
                    if(field.getStatus() == Status.MINE) {
                        field.setHidden(false);
                        gameStatus = GameStatus.GAME_OVER;
                    } else {
                        discoverAsFarAsPossible(field);
                    }
                } else if(e.getButton() == MouseEvent.BUTTON3) {
                    if(field.isHidden()) {
                        if (field.isMarked()) {
                            field.setMarked(false);
                            --markedFields;
                        } else {
                            field.setMarked(true);
                            ++markedFields;
                        }
                    }
                }
    
                if (checkWinConditions()) {
                    gameStatus = GameStatus.GAME_WON;
                }
                repaint();
            }
        });
    
        setup();
    }
    
    private void setup() {
        setPreferredSize(new Dimension(board.getWidth() * 15, board.getHeight() * 15));
        setSize(board.getWidth() * 15, board.getHeight() * 15);
    }
    
    void rebuild() {
        gameStatus = GameStatus.GAME_RUNNING;
        markedFields = 0;
        board.build();
        
        repaint();
    }
    
    private void discoverAsFarAsPossible(final Field pField) {
        if(!pField.isHidden()) {
            return;
        }
        final List<Field> fields = new LinkedList<>();
        fields.add(pField);
        for (int i = 0; i < fields.size(); i++) {
            final Field currentField = fields.get(i);
            logger.info(currentField.toString());
            currentField.setHidden(false);
    
            if (currentField.getStatus() == Status.MINE) {
                continue;
            }
            if(currentField.getStatus() != Status.EMPTY) {
                continue;
            }
            
            fields.addAll(
                    board.getNeighbours(currentField.getX(), currentField.getY())
                            .stream()
                            .filter(Field::isHidden)
                            .filter(pNeighbour -> pNeighbour.getStatus() != Status.MINE)
                            .filter(pNeighbour -> !fields.contains(pNeighbour))
                            .collect(Collectors.toList())
            );
        }
    }
    
    @Override
    public void paintComponent(final Graphics pGraphics) {
        final int componentWidth = getWidth();
        final int componentHeight = getHeight();
        
        final int fieldWidth = componentWidth / board.getWidth();
        final int fieldHeight = componentHeight / board.getHeight();
        
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                final Field field = board.getField(x, y);
                if(field.isHidden()) {
                    if (field.isMarked()) {
                        if(gameStatus.isOver() && field.getStatus() != Status.MINE) {
                            pGraphics.drawImage(Status.WRONG_MARK.getImage(),x * fieldWidth, y * fieldHeight, this);
                        } else {
                            pGraphics.drawImage(Status.MARKED.getImage(),x * fieldWidth, y * fieldHeight, this);
                        }
                    } else {
                        pGraphics.drawImage(Status.COVERED.getImage(),x * fieldWidth, y * fieldHeight, this);
                    }
                } else {
                    pGraphics.drawImage(field.getStatus().getImage(),x * fieldWidth, y * fieldHeight, this);
                }
            }
        }
    }
    
    private boolean checkWinConditions() {
        final List<Field> fields = Arrays.stream(board.getFields())
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
        
        return fields.stream().allMatch(aField -> {
            if (aField.getStatus() != Status.MINE) {
                return !aField.isHidden() && !aField.isMarked();
            } else {
                return aField.isMarked();
            }
        });
    }
    
    Board getBoard() {
        return board;
    }
    
    GameStatus getGameStatus() {
        return gameStatus;
    }
    
    int getMarkedFields() {
        return markedFields;
    }
    
}
