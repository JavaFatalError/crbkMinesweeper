package de.crbk.minesweeper;

import de.crbk.minesweeper.swing.SwingMinesweeper;

import javax.swing.*;
import java.awt.*;

public class Main {
    
    public static void main(final String[] args) {
        final JFrame frame = new JFrame("MinesweeperDemo");
        frame.setVisible(false);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.add(new SwingMinesweeper(40, 40, 20));
        frame.repaint();
        
        frame.setVisible(true);
    }
    
}
