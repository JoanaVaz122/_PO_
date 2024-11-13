package xxl;

import java.io.Serializable;

public class CutBuffer implements Serializable{

    private int _lines;
    private int _columns;
    private Storage1 _storageCB;

    public CutBuffer(int lines, int columns){
        _lines = lines;
        _columns = columns;
        _storageCB = new Storage1(lines, columns);
    }

    public int getLines(){
        return _lines;
    }

    public int getColumns(){
        return _columns;
    }

    public Storage1 getCutBufferStorage(){
        return _storageCB;
    }    
}