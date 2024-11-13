package xxl;

import java.io.Serializable;

abstract class Storage implements Serializable {
    
    abstract Cell getCell(int line, int column);
    abstract void setCell(Cell cell);
}
