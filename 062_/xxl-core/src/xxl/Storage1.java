package xxl;

import java.util.TreeMap;
import java.io.Serializable;

public class Storage1 extends Storage implements Serializable{

    private TreeMap<String, Cell> cells = new TreeMap<>();
    private int _n_lines;
    private int _n_columns;


    public Storage1(int lines, int columns){
        _n_lines = lines;
        _n_columns = columns;
        for(int line = 1; line <= lines; line++){
            for(int column = 1; column <= columns; column++){
                String coordinates = line + ";" + column;
                Cell cell = new Cell(line, column);
                cells.put(coordinates, cell);
            }
        }
    }

    public int getLines(){
        return _n_lines;
    }

    public int getColumns(){
        return _n_columns;
    }

    @Override
    public Cell getCell(int line, int column) {
        if (line >= 1 && line <= _n_lines && column >= 1 && column <= _n_columns) {
            String coordinates = line + ";" + column;
            return cells.get(coordinates);
        }
        return null;
    }

    @Override
    public void setCell(Cell cell) {
        cells.put(cell.toString(), cell);
    }
}