package de.crbk.minesweeper.core;

import java.util.*;
import java.util.stream.Collectors;

public class Board {
    
    private final int width;
    private final int height;
    private final int mineCount;
    private final Field[][] fields;
    
    public Board(final int pWidth, final int pHeight, final int pMineCount) {
        width = pWidth;
        height = pHeight;
        mineCount = pMineCount;
        
        fields = new Field[height][width];
    }
    
    public void build() {
        initFields();
        placeMinesAndNeighbours();
    }
    
    private void initFields() {
        for (int y = 0; y < fields.length; y++) {
            for (int x = 0; x < fields[y].length; x++) {
                final Field field = new Field();
                field.setX(x);
                field.setY(y);
                fields[y][x] = field;
            }
        }
    }
    
    private void placeMinesAndNeighbours() {
        final Set<Field> neighbours = new HashSet<>();
        for (int i = 0; i < mineCount; i++) {
            final Field mineField = placeRandomMine();
            neighbours.addAll(getNeighbours(mineField.getX(), mineField.getY()));
        }
    
        for (final Field neighbour : neighbours) {
            if(neighbour.getStatus() == Status.MINE) {
                continue;
            }
            final int closeMines = (int) getNeighbours(neighbour.getX(), neighbour.getY()).stream()
                    .filter(aField -> aField.getStatus() == Status.MINE)
                    .count();
            
            final Status status;
            switch (closeMines) {
                case 1:
                    status = Status.ONE_CLOSE;
                    break;
                case 2:
                    status = Status.TWO_CLOSE;
                    break;
                case 3:
                    status = Status.THR_CLOSE;
                    break;
                case 4:
                    status = Status.FOU_CLOSE;
                    break;
                case 5:
                    status = Status.FIV_CLOSE;
                    break;
                case 6:
                    status = Status.SIX_CLOSE;
                    break;
                case 7:
                    status = Status.SEV_CLOSE;
                    break;
                case 8:
                    status = Status.EIG_CLOSE;
                    break;
                default:
                    status = Status.EMPTY;
            }
            neighbour.setStatus(status);
        }
    }
    
    private Field placeRandomMine() {
        final Random random = new Random();
        Field mineField;
        do {
            final int mineX = random.nextInt(width - 1);
            final int mineY = random.nextInt(height - 1);
            mineField = getField(mineX, mineY);
        } while (mineField.getStatus() == Status.MINE);
        mineField.setStatus(Status.MINE);
        return mineField;
    }
    
    public Field getField(final int x, final int y) {
        if(x < 0 || x >= width) {
            return null;
        }
        if(y < 0 || y >= height) {
            return null;
        }
        return fields[y][x];
    }
    
    public List<Field> getNeighbours(final int x, final int y) {
        final Field[] n = new Field[8];
        
        n[0] = getField(x - 1, y - 1);
        n[1] = getField(x, y - 1);
        n[2] = getField(x + 1, y - 1);
    
        n[3] = getField(x - 1, y);
        n[4] = getField(x + 1, y);
    
        n[5] = getField(x - 1, y + 1);
        n[6] = getField(x, y + 1);
        n[7] = getField(x + 1, y + 1);
        
        return Arrays.stream(n).filter(Objects::nonNull).collect(Collectors.toList());
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getMineCount() {
        return mineCount;
    }
    
    public Field[][] getFields() {
        return fields;
    }
    
}
