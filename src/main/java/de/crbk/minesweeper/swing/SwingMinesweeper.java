package de.crbk.minesweeper.swing;

import de.crbk.minesweeper.core.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;

public class SwingMinesweeper extends JPanel {

    @Serial
    private static final long serialVersionUID = 2563276030271345247L;

    private final SwingBoard swingBoard;
    private final JLabel lblStatus;
    
    public SwingMinesweeper(final int pWidth, final int pHeight, final int pMineCount) {
        super(new FlowLayout(FlowLayout.CENTER));
        swingBoard = new SwingBoard(pWidth, pHeight, pMineCount);
        swingBoard.setSize(pWidth * Status.ICON_WIDTH, pHeight * Status.ICON_HEIGHT);
        swingBoard.setVisible(true);
        swingBoard.setPreferredSize(new Dimension(pWidth * Status.ICON_WIDTH, pHeight * Status.ICON_HEIGHT));
        swingBoard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                updateLblStatus();
            }
        });
        add(swingBoard);
        
        lblStatus = new JLabel();
        add(lblStatus);
        updateLblStatus();

        final JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(e -> {
            swingBoard.rebuild();
            updateLblStatus();
        });
        add(btnReset);
    }
    
    private void updateLblStatus() {
        final String statusText;
        if (!swingBoard.getGameStatus().isRunning()) {
            statusText = swingBoard.getGameStatus().getMessage();
        } else {
            statusText = String.format("Mines marked: %d/%d", swingBoard.getMarkedFields(), swingBoard.getBoard().getMineCount());
        }
        
        lblStatus.setText(statusText);
    }
}
