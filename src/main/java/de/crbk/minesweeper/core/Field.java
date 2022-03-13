package de.crbk.minesweeper.core;

import de.crbk.minesweeper.util.StringUtil;

import java.io.Serial;
import java.io.Serializable;

public class Field implements Serializable {

    @Serial
    private static final long serialVersionUID = 4778479887856399790L;

    private int x;
    private int y;
    
    private Status status;
    private boolean marked;
    
    private boolean hidden;
    
    Field() {
        x = -1;
        y = -1;
        
        status = Status.EMPTY;
        marked = false;
        hidden = true;
    }
    
    @Override
    public String toString() {
        return StringUtil.commonToString(Field.class, this);
    }
    
    public int getX() {
        return x;
    }
    
    void setX(final int pX) {
        x = pX;
    }
    
    public int getY() {
        return y;
    }
    
    void setY(final int pY) {
        y = pY;
    }
    
    public Status getStatus() {
        return status;
    }
    
    void setStatus(final Status pStatus) {
        status = pStatus;
    }
    
    public boolean isMarked() {
        return marked;
    }
    
    public void setMarked(final boolean pMarked) {
        marked = pMarked;
    }
    
    public boolean isHidden() {
        return hidden;
    }
    
    public void setHidden(final boolean pHidden) {
        hidden = pHidden;
    }
}
