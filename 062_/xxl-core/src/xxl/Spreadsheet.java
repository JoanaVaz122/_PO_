package xxl;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.ArrayList;

import xxl.exceptions.UnrecognizedEntryException;

/**
 * Class representing a spreadsheet.
 */
public class Spreadsheet implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private Storage1 _spreadStorage;
    private String _filename = "";
    private CutBuffer _cutBuffer;
    private boolean _changed = false;
    private List<User> _userList = new ArrayList<>();

    public void changed(){
        setChanged(true);
    }


    public boolean hasChanged(){
        return _changed;
    }


    public void setChanged(boolean changed) {
        _changed = changed;
    }


    public Spreadsheet(int n_lines, int n_columns){
        _spreadStorage = new Storage1(n_lines, n_columns);
        setChanged(false);
    }

    public boolean hasFilename(){
        if(!_filename.equals("")){
            return true;
        }
        return false;
    }

    public String getFilename(){
        return _filename;
    }

    public void setFilename(String input){
        _filename = input;
    }

    public void addUser(User user) {
        _userList.add(user);
    }
    

    public String[] parseCell(String stringToParse){
        return stringToParse.split(";");
    }


    public String[] parseInterval(String stringToParse){
        return stringToParse.split(":");
    }


    public String parseFunction(String stringToParse){
        if("".equals(stringToParse) || (isCellReference(stringToParse))){
            return "";
        }
        return stringToParse.substring(stringToParse.indexOf("=") + 1, stringToParse.indexOf("("));
    }


    /**
     * Checks if the input string represents a single valid cell reference.
     *
     * @param input The input string to be checked.
     * @return `true` if the input represents a single valid cell reference, `false` otherwise.
     */
    private boolean isSingleCell(String input){
        String[] parsedInput = parseInterval(input);
        if(parsedInput.length == 1 && input.length() > 2){
            String [] parsedCell = parseCell(parsedInput[0]);
            if(Integer.valueOf(parsedCell[0]) <= _spreadStorage.getLines() && 
            Integer.valueOf(parsedCell[1]) <= _spreadStorage.getColumns() &&
            Integer.valueOf(parsedCell[0]) != 0 && Integer.valueOf(parsedCell[1]) != 0){
                return true;
            }   
        }
        return false;
    }


    /**
     * Checks if the input string represents a valid cell range interval.
     *
     * @param input The input string to be checked.
     * @return `true` if the input represents a valid cell range interval, `false` otherwise.
     */
    private boolean isCellInt(String input){
        String[] parsedInput = parseInterval(input);
        if(parsedInput.length == 2){
            String [] parsedStartCell = parseCell(parsedInput[0]);
            String [] parsedEndCell = parseCell(parsedInput[1]);
            if(Integer.valueOf(parsedStartCell[0]) <= _spreadStorage.getLines() 
            && Integer.valueOf(parsedStartCell[1]) <= _spreadStorage.getColumns()
            && Integer.valueOf(parsedEndCell[0]) <= _spreadStorage.getLines() 
            && Integer.valueOf(parsedEndCell[1]) <= _spreadStorage.getColumns()
            && (Integer.valueOf(parsedStartCell[0]) == Integer.valueOf(parsedEndCell[0]) ||
                Integer.valueOf(parsedStartCell[1]) == Integer.valueOf(parsedEndCell[1]))){
                return true;
            }  
        }
        return false;
    }


    /**
     * Checks if the given cell range interval is horizontal, i.e., it spans across a single row.
     *
     * @param startCell The starting cell of the interval.
     * @param endCell   The ending cell of the interval.
     * @return `true` if the interval is horizontal, `false` otherwise.
     */
    private boolean isHorizontalInterval(String startCell, String endCell) {
        String[] startLine = parseCell(startCell);
        String[] endLine = parseCell(endCell);
            
        return startLine[0].equals(endLine[0]);
    }
    

    /**
     * Checks if the given cell range interval is vertical, i.e., it spans across a single column.
     *
     * @param startCell The starting cell of the interval.
     * @param endCell   The ending cell of the interval.
     * @return `true` if the interval is vertical, `false` otherwise.
     */
    private boolean isVerticalInterval(String startCell, String endCell) {
        String[] startColumn = parseCell(startCell);
        String[] endColumn = parseCell(endCell);
            
        return startColumn[1].equals(endColumn[1]);
    }


    private boolean isIntOrString(String value) {
        return (value.charAt(0) == '-' || Character.isDigit(value.charAt(0)) || value.charAt(0) == '\'');
    }
    

    private boolean isCellReference(String value) {
        return (!value.equals("") && value.charAt(0) == '=' && Character.isDigit(value.charAt(1)));
    }
    

    private boolean isFunction(String value) {
        return (!value.equals("") && value.charAt(0) == '=' && !Character.isDigit(value.charAt(1)));
    }


    /**
     * Calculate the length of a cell range interval based on the starting and ending cells.
     *
     * @param startCell The starting cell of the interval.
     * @param endCell   The ending cell of the interval.
     * @return The length of the interval, or 0 if the interval is not horizontal or vertical.
     */
    public int getIntervalLength(String startCell, String endCell){
        String[] startCellParsed = parseCell(startCell);
        String[] endCellParsed = parseCell(endCell);
        if(isHorizontalInterval(startCell, endCell)){
            return Integer.valueOf(endCellParsed[1]) - Integer.valueOf(startCellParsed[1]) + 1;
        } else if(isVerticalInterval(startCell, endCell)){
            return Integer.valueOf(endCellParsed[0]) - Integer.valueOf(startCellParsed[0]) + 1;
        }
        return 0;
    }


    /**
     * Checks if the given input represents a valid address in the spreadsheet(a single cell or a cell range)
     *
     * @param inputAddress The input address to be checked.
     * @return `true` if the input is a valid address, `false` otherwise.
     */
    public boolean validAdress(String inputAddress){
        if (!isSingleCell(inputAddress) && !isCellInt(inputAddress)) {
            return false;
        }
        return true;
    }


    /**
     * Recognizes and identifies the type of a function based on its input representation.
     *
     * @param inputFunction The input representation of the function.
     * @return An integer representing the type of the recognized function. Returns 0 if the function is not recognized.
     */
    public int recognizeFunct(String inputFunction){
        String function = parseFunction(inputFunction);
        switch(function){
            case "ADD":
                return 1;
            case "SUB":
                return 2;
            case "MUL":
                return 3;
            case "DIV":
                return 4;
            case "AVERAGE":
                return 5;
            case "COALESCE":
                return 6;
            case "CONCAT":
                return 7;
            case "PRODUCT":
                return 8;
        }
        return 0;
    }


    /**
     * Insert specified content in specified range.
     *
     * @param rangeSpecification
     * @param contentSpecification
     */
    public void insertContents(String rangeSpecification, String contentSpecification) throws UnrecognizedEntryException {
        rangeSpecification = normalizeInterval(rangeSpecification);
        if(!validAdress(rangeSpecification)){
            throw new UnrecognizedEntryException(rangeSpecification);
        }
        String[] parts = parseInterval(rangeSpecification);
        changed();
        
        if(contentSpecification.equals("")){
            delete(contentSpecification);
            return;
        }
        if (parts.length == 1) {
            insertContentInSingleCell(parts[0], contentSpecification);
        } else{
            String startCell = parts[0];
            String endCell = parts[1];
            insertContentInInterval(startCell, endCell, contentSpecification);
            }
    }
    

    /**
     * Inserts content into a single cell of the spreadsheet.
     *
     * @param cellSpecification The cell specification in the format "row,column".
     * @param contentSpecification The content specification to be inserted into the cell.
    */
    private void insertContentInSingleCell(String cellSpecification, String contentSpecification)throws UnrecognizedEntryException {
        String[] cellParts = parseCell(cellSpecification);
        int line = Integer.valueOf(cellParts[0]);
        int column = Integer.valueOf(cellParts[1]);
        Cell temp = _spreadStorage.getCell(line, column);
    
        if (temp != null) {
            temp.recognizeContent(contentSpecification);
            _spreadStorage.setCell(temp);
        }
    }
    

    /**
     * Inserts content into a range of cells in the spreadsheet, specified by a start and end cell.
     *
     * @param startCell The starting cell in the format "row,column".
     * @param endCell The ending cell in the format "row,column".
     * @param contentSpecification The content specification to be inserted into the cells.
     */
    private void insertContentInInterval(String startCell, String endCell, String contentSpecification)throws UnrecognizedEntryException {
        String[] startCellParts = parseCell(startCell);
        String[] endCellParts = parseCell(endCell);
        
        if (isHorizontalInterval(startCell, endCell)) {
            int currentLine = Integer.valueOf(startCellParts[0]);
            int startColumn = Integer.valueOf(startCellParts[1]);
            int endColumn = Integer.valueOf(endCellParts[1]);
    
            for (int i = startColumn; i <= endColumn; i++) {
                Cell temp = _spreadStorage.getCell(currentLine, i);
                if (temp != null) {
                    temp.recognizeContent(contentSpecification);
                    _spreadStorage.setCell(temp);
                }
            }
        } else if (isVerticalInterval(startCell, endCell)) {
            int currentColumn = Integer.valueOf(startCellParts[1]);
            int startLine = Integer.valueOf(startCellParts[0]);
            int endLine = Integer.valueOf(endCellParts[0]);
    
            for (int i = startLine; i <= endLine; i++) {
                Cell temp = _spreadStorage.getCell(i, currentColumn);
                if (temp != null) {
                    temp.recognizeContent(contentSpecification);
                    _spreadStorage.setCell(temp);
                }
            }
        }
    }
    

    /**
     * Normalizes a cell range interval to ensure that it is in the correct order.
     *
     * @param interval A string representing the cell range interval to be normalized.
     * @return The normalized cell range interval with components in the correct order.
     */
    public String normalizeInterval(String interval) {
        String[] parts = interval.split(":");
    
        if (parts.length != 2) {
            return interval;
        }
        String[] startParts = parts[0].split(";");
        String[] endParts = parts[1].split(";");
    
        if (startParts.length != 2 || endParts.length != 2) {
            return interval;
        }
    
        int startLine = Integer.parseInt(startParts[0]);
        int startColumn = Integer.parseInt(startParts[1]);
        int endLine = Integer.parseInt(endParts[0]);
        int endColumn = Integer.parseInt(endParts[1]);
    
        if (startLine > endLine || (startLine == endLine && startColumn > endColumn)) {
            return endLine + ";" + endColumn + ":" + startLine + ";" + startColumn;
        }
        return interval;
    }
    

    /**
     * Searches for cells containing a specific input value in the spreadsheet.
     *
     * @param inputValue The value to search for within the cells.
     * @return A string containing the cell locations (in the format "row;column") and their content that match the input value.
     */
    public String searchValue(String inputValue) {
        Cell temp;
        StringBuilder res = new StringBuilder();
        for (int i = 1; i <= _spreadStorage.getLines(); i++) {
            for (int j = 1; j <= _spreadStorage.getColumns(); j++) {
                temp = _spreadStorage.getCell(i, j);
                if (temp.getContent() != null && obtainValue(temp).equals(inputValue)) {
                    try {
                        res.append(showContent(i + ";" + j)).append("\n");
                    } catch (UnrecognizedEntryException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return res.toString();
    }

    public String searchReferences() {
        Cell temp;
        StringBuilder res = new StringBuilder();
        for (int i = 1; i <= _spreadStorage.getLines(); i++) {
            for (int j = 1; j <= _spreadStorage.getColumns(); j++) {
                temp = _spreadStorage.getCell(i, j);
                if (temp!= null && temp.getContent() != null && recognizeFunct(temp.getContent().contentToString()) > 4) {
                    try {
                        res.append(showContent(i + ";" + j)).append("\n");
                    } catch (UnrecognizedEntryException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return res.toString();
    }

    public String searchEvenNumbers() {
        Cell temp;
        StringBuilder res = new StringBuilder();
        for (int i = 1; i <= _spreadStorage.getLines(); i++) {
            for (int j = 1; j <= _spreadStorage.getColumns(); j++) {
                temp = _spreadStorage.getCell(i, j);
                if (temp.getContent() != null && obtainValue(temp).equals("\'")) {
                    try {
                        res.append(showContent(i + ";" + j)).append("\n");
                    } catch (UnrecognizedEntryException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return res.toString();
    }
        

    /**
     * Searches for cells containing a specific function in the spreadsheet.
     *
     * @param inputValue The function to search for within the cells.
     * @return A string containing the cell locations (in the format "row;column") and their content that contain the specified function, sorted alphabetically.
     */
    public String searchFunction(String inputValue) {
        Cell temp;
        StringBuilder res = new StringBuilder();
        List<Cell> matchingCells = new ArrayList<>();
        for (int i = 1; i <= _spreadStorage.getLines(); i++) {
            for (int j = 1; j <= _spreadStorage.getColumns(); j++) {
                temp = _spreadStorage.getCell(i, j);
                if (temp.getContent() != null && parseFunction(temp.getContent().contentToString()).contains(inputValue)){ 
                    matchingCells.add(temp);
                }
            }
        }
        matchingCells.sort((c1, c2) -> parseFunction(c1.getContent().contentToString()).compareTo(parseFunction(c2.getContent().contentToString())));
        for (Cell cell : matchingCells) {
            try {
                res.append(showContent(cell.getLine() + ";" + cell.getColumn())).append("\n");
            } catch (UnrecognizedEntryException e) {
                e.printStackTrace();
            }
        }
        return res.toString();
    }


    /**
     * Extracts the value from a cell's content (handles various types of content, such as literals, cell references, and functions.) 
     *
     * @param cell The cell from which to obtain the value.
     * @return The extracted value as a string, or "#VALUE" if the content is not recognized or an error occurs.
     */
    public String obtainValue(Cell cell) {
        String value = cell.getContent().getValue();
         
        if (value == null) {
            return "#VALUE";
        }
       
        if (isIntOrString(value)) {
            return value;
        } else if (isCellReference(value)) {
            return handleCellReference(value, cell);
        } else if (isFunction(value)) {
            String subOperation = value.substring(value.indexOf("(") + 1, value.indexOf(")"));
            int functionType = recognizeFunct(value);
    
            if (functionType != 0) {
                if (functionType <= 4) {
                    return handleTwoArgFunction(subOperation, cell);
                } else if (functionType > 4 && functionType <= 8) {
                    subOperation = normalizeInterval(subOperation);
                    return handleIntervalFunction(subOperation, cell);
                }
            }
        }
    
        return "#VALUE";
    }
    

    /**
     * Handles the execution of a two-argument function contained within a cell's content.
     *
     * @param subOperation The substring representing the function and its arguments.
     * @param cell The cell containing the function.
     * @return The result of executing the function, and the cell's content is updated accordingly.
     */
    private String handleTwoArgFunction(String subOperation, Cell cell) {
        String firstArg = subOperation.substring(0, subOperation.indexOf(","));
        String secondArg = subOperation.substring(subOperation.indexOf(",") + 1);
    
        if (!subOperation.contains(";")) {
            String value = cell.getContent().execute(firstArg, secondArg);
            cell.getContent().setValue(value);
            return value;
        } else {
            firstArg = isSingleCell(firstArg)
                    ? obtainValueFromCellReference(parseCell(firstArg), cell)
                    : firstArg;
    
            secondArg = isSingleCell(secondArg)
                    ? obtainValueFromCellReference(parseCell(secondArg), cell)
                    : secondArg;
    
            String value = cell.getContent().execute(firstArg, secondArg);
            cell.getContent().setValue(value);
            return value;
        }
    }
    

    /**
     * Handles the execution of an interval function contained within a cell's content.
     *
     * @param subOperation The substring representing the interval function and its arguments.
     * @param cell The cell containing the interval function.
     * @return The result of executing the interval function, or "#VALUE" if the content is not recognized or an error occurs.
     */
    private String handleIntervalFunction(String subOperation, Cell cell) {
        String startArg = subOperation.substring(0, subOperation.indexOf(":"));
        String endArg = subOperation.substring(subOperation.indexOf(":") + 1);
    
        if (isCellInt(subOperation)) {
            if (isVerticalInterval(startArg, endArg)) {
                return createArgVertical(startArg, endArg, cell);
            } else if (isHorizontalInterval(startArg, endArg)) {
                return createArgHorizontal(startArg, endArg, cell);
            }
        }
        return "#VALUE";
    }
    

    /**
     * Creates an argument for a vertical interval function and executes it.
     *
     * @param startArg The starting cell reference in the vertical interval.
     * @param endArg The ending cell reference in the vertical interval.
     * @param cell The cell containing the interval function.
     * @return The result of executing the interval function, or "#VALUE" if the content is not recognized or an error occurs.
     */
    private String createArgVertical(String startArg, String endArg, Cell cell) {
        String[] startArgParsed = parseCell(startArg);
        String[] endArgParsed = parseCell(endArg);
        int currentColumn = Integer.valueOf(startArgParsed[1]);
        int startLine = Integer.valueOf(startArgParsed[0]);
        int endLine = Integer.valueOf(endArgParsed[0]);
    
        String[] arrayArg = new String[getIntervalLength(startArg, endArg)];
        int counter = 0;
    
        for (int i = startLine; i <= endLine; i++) {
            Cell currentCell = _spreadStorage.getCell(i, currentColumn);
    
            if (currentCell.getContent() == null) {
                return "#VALUE";
            }
    
            String arg = obtainValue(currentCell);
            arrayArg[counter] = arg;
            counter++;
        }
    
        String value = cell.getContent().executeIO(arrayArg);
        cell.getContent().setValue(value);
        return value;
    }
    

    /**
     * Creates an argument for a horizontal interval function and executes it.
     *
     * @param startArg The starting cell reference in the horizontal interval.
     * @param endArg The ending cell reference in the horizontal interval.
     * @param cell The cell containing the interval function.
     * @return The result of executing the interval function, or "#VALUE" if the content is not recognized or an error occurs.
     */
    private String createArgHorizontal(String startArg, String endArg, Cell cell) {
        String[] startArgParsed = parseCell(startArg);
        String[] endArgParsed = parseCell(endArg);
        int currentLine = Integer.valueOf(startArgParsed[0]);
        int startColumn = Integer.valueOf(startArgParsed[1]);
        int endColumn = Integer.valueOf(endArgParsed[1]);
    
        String[] arrayArg = new String[getIntervalLength(startArg, endArg)];
        int counter = 0;
    
        for (int i = startColumn; i <= endColumn; i++) {
            if (_spreadStorage.getCell(currentLine, i).getContent() == null) {
                continue;
            }
    
            String arg = obtainValue(_spreadStorage.getCell(currentLine, i));
            arrayArg[counter] = arg;
            counter++;
        }
    
        String value = cell.getContent().executeIO(arrayArg);
        cell.getContent().setValue(value);
        return value;
    }
    

    /**
     * Obtains the value from a cell referenced by a given cell reference and handles potential errors.
     *
     * @param cellRef An array representing the cell reference in the format [row, column].
     * @param cell The cell containing the reference.
     * @return The value of the referenced cell or "#VALUE" if the referenced cell's content is not recognized or an error occurs.
     */
    private String obtainValueFromCellReference(String[] cellRef, Cell cell) {
        int row = Integer.valueOf(cellRef[0]);
        int column = Integer.valueOf(cellRef[1]);
        Cell referencedCell = _spreadStorage.getCell(row, column);
    
        if (referencedCell.getContent() == null) {
            return "#VALUE";
        }
    
        return obtainValue(referencedCell);
    }
    

    /**
     * Handles the execution of a cell reference contained within a cell's content.
     *
     * @param value The cell reference in the format "row,column".
     * @param cell The cell containing the cell reference.
     * @return The value of the referenced cell or "#VALUE" if the referenced cell's content is not recognized or an error occurs.
     */
    private String handleCellReference(String value, Cell cell) {
        String parsedValue = value.substring(value.indexOf("=") + 1);
        String[] stringReference = parseCell(parsedValue);
        if (_spreadStorage.getCell(Integer.valueOf(stringReference[0]), Integer.valueOf(stringReference[1])).getContent() == null) {
            return "#VALUE"; 
        }
        return obtainValue(_spreadStorage.getCell(Integer.valueOf(stringReference[0]), Integer.valueOf(stringReference[1])));
    }

    public String showAll(){
        int lines = _spreadStorage.getLines();
        int columns =  _spreadStorage.getColumns();
        StringBuilder str = new StringBuilder();

        for(int i = 1; i <= lines; i++){
            for(int j = 1; i <= columns; j++){
                Cell temp = _spreadStorage.getCell(i, j);
                if (temp != null && temp.getContent() != null) {
                    str.append(temp.toString())
                        .append("|")
                        .append(obtainValue(temp))
                        .append(temp.getContent().contentToString());
                } else {
                    str.append(temp.toString())
                        .append("|");
                }
            }
            str.append("\n");
        }
        return str.toString();
    }

    /**
     * Displays the content of a cell or a cell range, specified by the input string.
     *
     * @param cellRange The string representation of the cell or cell range to display.
     * @return The content of the specified cell or cell range.
     * @throws UnrecognizedEntryException If the cell or cell range is not recognized.
     */
    public String showContent(String cellRange) throws UnrecognizedEntryException{
        if(!validAdress(cellRange)){
            throw new UnrecognizedEntryException(cellRange);
        }
        if (isSingleCell(cellRange)) {
            return showSingleCellContent(cellRange);
        } else if (isCellInt(cellRange)) {
            cellRange = normalizeInterval(cellRange);
            return showIntervalContent(cellRange);
        }
        return "";
    }
    

    /**
     * Displays the content of a single cell specified by the input cell range.
     *
     * @param cellRange A string representing a single cell in the format "row,column".
     * @return The content of the specified cell, including its string representation and value, or an empty string if the cell is not recognized.
     * @throws UnrecognizedEntryException If the cell is not recognized.
     */
    private String showSingleCellContent(String cellRange) throws UnrecognizedEntryException {
        String[] cellToShow = parseCell(cellRange);
        Cell temp = _spreadStorage.getCell(Integer.valueOf(cellToShow[0]), Integer.valueOf(cellToShow[1]));
        if (temp != null && temp.getContent() != null) {
            return temp.toString() + "|" + obtainValue(temp) + temp.getContent().contentToString();
        } else {
            return temp.toString() + "|";
        }
    }
    

    /**
     * Displays the content of a cell interval specified by the input cell range.
     *
     * @param cellRange A string representing a cell interval in the format "startCell:endCell".
     * @return The content of the specified cell interval, or an empty string if the interval is not recognized.
     * @throws UnrecognizedEntryException If the cell interval is not recognized or if an error occurs.
     */
    private String showIntervalContent(String cellRange) throws UnrecognizedEntryException {
        String[] parsedCellRange = parseInterval(cellRange);
        String[] startCellParts = parseCell(parsedCellRange[0]);
        String[] endCellParts = parseCell(parsedCellRange[1]);
    
        if (isHorizontalInterval(parsedCellRange[0], parsedCellRange[1])) {
            return showHorizontalIntervalContent(startCellParts, endCellParts);
        }
    
        if (isVerticalInterval(parsedCellRange[0], parsedCellRange[1])) {
            return showVerticalIntervalContent(startCellParts, endCellParts);
        }
    
        return "";
    }
    

    /**
     * Displays the content of a horizontal cell interval specified by the start and end cell parts.
     *
     * @param startCellParts An array representing the parts of the starting cell in the format [row, column].
     * @param endCellParts An array representing the parts of the ending cell in the format [row, column].
     * @return The content of the specified horizontal cell interval, or an empty string if the interval is not recognized.
     * @throws UnrecognizedEntryException If the cell interval is not recognized or if an error occurs.
     */
    private String showHorizontalIntervalContent(String[] startCellParts, String[] endCellParts) throws UnrecognizedEntryException {
        int currentLine = Integer.valueOf(startCellParts[0]);
        int startColumn, endColumn;
        startColumn = Integer.valueOf(startCellParts[1]);
        endColumn = Integer.valueOf(endCellParts[1]);
        int counter = 0;
    
        StringBuilder str = new StringBuilder();
        for (int i = startColumn; i <= endColumn; i++) {
            Cell temp = _spreadStorage.getCell(currentLine, i);
            if (temp != null && temp.getContent() != null) {
                counter ++;
                str.append(temp.toString())
                    .append("|")
                    .append(obtainValue(temp))
                    .append(temp.getContent().contentToString())
                    .append("\n");
            } else {
                counter ++;
                str.append(temp.toString())
                    .append("|")
                    .append("\n");
            }
        }
        return str.toString() + counter;
    }
    

    /**
     * Displays the content of a vertical cell interval specified by the start and end cell parts.
     *
     * @param startCellParts An array representing the parts of the starting cell in the format [row, column].
     * @param endCellParts An array representing the parts of the ending cell in the format [row, column].
     * @return The content of the specified vertical cell interval, or an empty string if the interval is not recognized.
     * @throws UnrecognizedEntryException If the cell interval is not recognized or if an error occurs.
     */
    private String showVerticalIntervalContent(String[] startCellParts, String[] endCellParts) throws UnrecognizedEntryException {
        int currentColumn = Integer.valueOf(startCellParts[1]);
        int startLine, endLine;
        startLine = Integer.valueOf(startCellParts[0]);
        endLine = Integer.valueOf(endCellParts[0]);
        int counter = 0;
    
        StringBuilder str = new StringBuilder();
        for (int i = startLine; i <= endLine; i++) {
            Cell temp = _spreadStorage.getCell(i, currentColumn);
            if (temp != null && temp.getContent() != null) {
                counter ++;
                str.append(temp.toString())
                    .append("|")
                    .append(obtainValue(temp))
                    .append(temp.getContent().contentToString())
                    .append("\n");
            } else {
                counter ++;
                str.append(temp.toString())
                    .append("|")
                    .append("\n");
            }
        }
        return str.toString() + counter;
    }


    /**
     * Pastes the content from the cut buffer to the specified cell range.
     *
     * @param cellRange A string representing the target cell range where the content should be pasted.
     */
    public void paste(String cellRange)throws UnrecognizedEntryException {
        if (_cutBuffer == null) {
            return;
        }
        cellRange = normalizeInterval(cellRange);

        if(!validAdress(cellRange)){
            throw new UnrecognizedEntryException(cellRange);
        }

        String[] parsedCellRange = parseInterval(cellRange);
        String[] parsedStartCell = parseCell(parsedCellRange[0]);
        Cell tempSource, tempDestination;
        int counter = 0;
        
        if (isSingleCell(cellRange)) {
            int startLine = Integer.valueOf(parsedStartCell[0]);
            int startColumn = Integer.valueOf(parsedStartCell[1]);
            
            if (_cutBuffer.getColumns() == 1) {
                if (_cutBuffer.getLines() == 1) {
                   
                    copyContent(_cutBuffer.getCutBufferStorage().getCell(1, 1), _spreadStorage.getCell(startLine, startColumn));
                } else {
                    
                    int endLine = Math.min(_cutBuffer.getLines() + startLine - 1, _spreadStorage.getLines());
                    for (int i = startLine; i <= endLine; i++) {
                        counter++;
                        tempSource = _cutBuffer.getCutBufferStorage().getCell(counter, 1);
                        tempDestination = _spreadStorage.getCell(i, startColumn);
                        copyContent(tempSource, tempDestination);
                    }
                }
            } else if (_cutBuffer.getLines() == 1) {
              
                int endColumn = Math.min(_cutBuffer.getColumns() + startColumn - 1, _spreadStorage.getColumns());
                for (int i = startColumn; i <= endColumn; i++) {
                    counter++;
                    tempSource = _cutBuffer.getCutBufferStorage().getCell(1, counter);
                    tempDestination = _spreadStorage.getCell(startLine, i);
                    copyContent(tempSource, tempDestination);
                }
            }
        } else if (isCellInt(cellRange)) {

            String[] parsedEndCell = parseCell(parsedCellRange[1]);
            
            if (_cutBuffer.getColumns() == 1 && _cutBuffer.getLines() == 1) {
             
                int currentLine = Integer.valueOf(parsedStartCell[0]);
                int currentColumn = Integer.valueOf(parsedStartCell[1]);
                copyContent(_cutBuffer.getCutBufferStorage().getCell(1, 1), _spreadStorage.getCell(currentLine, currentColumn));
            } else if (_cutBuffer.getColumns() == getIntervalLength(parsedCellRange[0], parsedCellRange[1]) && isHorizontalInterval(parsedCellRange[0], parsedCellRange[1])) {
              
                int currentLine = Integer.valueOf(parsedStartCell[0]);
                int startColumn = Integer.valueOf(parsedStartCell[1]);
                int endColumn = Integer.valueOf(parsedEndCell[1]);
                for (int i = startColumn; i <= endColumn; i++) {
                    counter++;
                    tempSource = _cutBuffer.getCutBufferStorage().getCell(1, counter);
                    tempDestination = _spreadStorage.getCell(currentLine, i);
                    copyContent(tempSource, tempDestination);
                }
            } else if (_cutBuffer.getLines() == getIntervalLength(parsedCellRange[0], parsedCellRange[1]) && isVerticalInterval(parsedCellRange[0], parsedCellRange[1])) {
        
                int currentColumn = Integer.valueOf(parsedStartCell[1]);
                int startLine = Integer.valueOf(parsedStartCell[0]);
                int endLine = Integer.valueOf(parsedEndCell[0]);
                for (int i = startLine; i <= endLine; i++) {
                    counter++;
                    tempSource = _cutBuffer.getCutBufferStorage().getCell(counter, 1);
                    tempDestination = _spreadStorage.getCell(i, currentColumn);
                    copyContent(tempSource, tempDestination);
                }
            }
        }
    }
    

    /**
     * Copies content from the source cell to the destination cell.
     *
     * @param source The source cell from which content will be copied.
     * @param destination The destination cell where the content will be copied to.
     */
    private void copyContent(Cell source, Cell destination)throws UnrecognizedEntryException {
        if (source != null && source.getContent() != null) {
            String valueToCopy = obtainValue(source);
            String contentToCopy = source.getContent().contentToString();
            destination.recognizeContent(contentToCopy.isEmpty() ? valueToCopy : contentToCopy);
        } else {
            destination.setContent(null);
        }
    }
    
    
    /**
     * Cuts content from the specified cell range.
     *
     * @param cellRange A string representing the cell range from which content should be cut.
     */
    public void cut(String cellRange) throws UnrecognizedEntryException{
        copy(cellRange);
        delete(cellRange);
    }


    /**
     * Deletes the content from the specified cell range.
     *
     * @param cellRange A string representing the cell range from which content should be deleted.
     */
    public void delete(String cellRange)throws UnrecognizedEntryException{
        cellRange = normalizeInterval(cellRange);
        if(!validAdress(cellRange)){
            throw new UnrecognizedEntryException(cellRange);
        }
        String[] parsedCellRange = parseInterval(cellRange);
        String[] parsedStartCell = parseCell(parsedCellRange[0]);
        if(parsedCellRange.length == 1){
            _spreadStorage.getCell(Integer.valueOf(parsedStartCell[0]), Integer.valueOf(parsedStartCell[1])).setContent(null);
        }
        else{
            String[] parsedEndCell = parseCell(parsedCellRange[1]);
            if(isHorizontalInterval(parsedCellRange[0], parsedCellRange[1])){
                int currentLine = Integer.valueOf(parsedStartCell[0]);
                int startColumn = Integer.valueOf(parsedStartCell[1]);
                int endColumn = Integer.valueOf(parsedEndCell[1]);
                for(int i = startColumn; i <= endColumn; i++){
                    _spreadStorage.getCell(currentLine, i).setContent(null);
                }
            } else if(isVerticalInterval(parsedCellRange[0], parsedCellRange[1])){
                int currentColumn = Integer.valueOf(parsedStartCell[1]);
                int startLine = Integer.valueOf(parsedStartCell[0]);
                int endLine = Integer.valueOf(parsedEndCell[0]);
                for(int i = startLine; i <= endLine; i++){
                    _spreadStorage.getCell(i, currentColumn).setContent(null);
                }
            }
        }
    }


    /**
     * Copies the content from the specified cell range.
     *
     * @param cellRange A string representing the cell range from which content should be copied.
     */
    public void copy(String cellRange)throws UnrecognizedEntryException {

        cellRange = normalizeInterval(cellRange);

        if(!validAdress(cellRange)){
            throw new UnrecognizedEntryException(cellRange);
        }

        String[] parsedCellRange = parseInterval(cellRange);
    
        if (parsedCellRange.length == 1) {
            copySingleCell(parsedCellRange[0]);
        } else {
            copyCellRange(parsedCellRange);
        }
    }
    

    /**
     * Copies the content from a single cell and stores it in the cut buffer.
     *
     * @param singleCell A string representing the single cell from which content should be copied.
     */
    private void copySingleCell(String singleCell)throws UnrecognizedEntryException {
        String[] parsedStartCell = parseCell(singleCell);
        int startLine = Integer.valueOf(parsedStartCell[0]);
        int startColumn = Integer.valueOf(parsedStartCell[1]);
        Cell temp = _spreadStorage.getCell(startLine, startColumn);
    
        _cutBuffer = new CutBuffer(1, 1);
        if (temp != null && temp.getContent() != null) {
            copyContentToCutBuffer(temp, 1, 1);
        } else {
            _cutBuffer.getCutBufferStorage().getCell(1, 1).setContent(null);
        }
    }
    

    /**
     * Copies the content from a cell range and stores it in the cut buffer.
     *
     * @param parsedCellRange An array containing the parsed start and end cells of the cell range.
     */
    private void copyCellRange(String[] parsedCellRange)throws UnrecognizedEntryException {
        String[] parsedStartCell = parseCell(parsedCellRange[0]);
        String[] parsedEndCell = parseCell(parsedCellRange[1]);
        int intervalLength = getIntervalLength(parsedCellRange[0], parsedCellRange[1]);
    
        if (isHorizontalInterval(parsedCellRange[0], parsedCellRange[1])) {
            copyHorizontalCellRange(parsedStartCell, parsedEndCell, intervalLength);
        } else if (isVerticalInterval(parsedCellRange[0], parsedCellRange[1])) {
            copyVerticalCellRange(parsedStartCell, parsedEndCell, intervalLength);
        }
    }
    

    /**
     * Copies the content from a horizontal cell range and stores it in the cut buffer.
     *
     * @param parsedStartCell An array containing the parsed coordinates of the start cell in the range.
     * @param parsedEndCell An array containing the parsed coordinates of the end cell in the range.
     * @param intervalLength The length of the horizontal cell range.
     */
    private void copyHorizontalCellRange(String[] parsedStartCell, String[] parsedEndCell, int intervalLength)throws UnrecognizedEntryException {
        int currentLine = Integer.valueOf(parsedStartCell[0]);
        int startColumn = Integer.valueOf(parsedStartCell[1]);
        int endColumn = Integer.valueOf(parsedEndCell[1]);
    
        _cutBuffer = new CutBuffer(1, intervalLength);
        for (int i = startColumn; i <= endColumn; i++) {
            Cell temp = _spreadStorage.getCell(currentLine, i);
            if (temp != null && temp.getContent() != null) {
                copyContentToCutBuffer(temp, 1, i - startColumn + 1);
            } else {
                _cutBuffer.getCutBufferStorage().getCell(1, i - startColumn + 1).setContent(null);
            }
        }
    }
    

    /**
     * Copies the content from a vertical cell range and stores it in the cut buffer.
     *
     * @param parsedStartCell An array containing the parsed coordinates of the start cell in the range.
     * @param parsedEndCell An array containing the parsed coordinates of the end cell in the range.
     * @param intervalLength The length of the vertical cell range.
     */
    private void copyVerticalCellRange(String[] parsedStartCell, String[] parsedEndCell, int intervalLength)throws UnrecognizedEntryException {
        int currentColumn = Integer.valueOf(parsedStartCell[1]);
        int startLine = Integer.valueOf(parsedStartCell[0]);
        int endLine = Integer.valueOf(parsedEndCell[0]);
    
        _cutBuffer = new CutBuffer(intervalLength, 1);
        for (int i = startLine; i <= endLine; i++) {
            Cell temp = _spreadStorage.getCell(i, currentColumn);
            if (temp != null && temp.getContent() != null) {
                copyContentToCutBuffer(temp, i - startLine + 1, 1);
            } else {
                _cutBuffer.getCutBufferStorage().getCell(i - startLine + 1, 1).setContent(null);
            }
        }
    }


    /**
     * Copies the content from a source cell to a specific location in the cut buffer.
     *
     * @param sourceCell  The source cell from which content should be copied.
     * @param destRow     The destination row in the cut buffer where the content should be placed.
     * @param destColumn  The destination column in the cut buffer where the content should be placed.
     */
    private void copyContentToCutBuffer(Cell sourceCell, int destRow, int destColumn)throws UnrecognizedEntryException {
        String valueToCopy = obtainValue(sourceCell);
        String contentToCopy = sourceCell.getContent().contentToString();
    
        if (!contentToCopy.isEmpty()) {
            _cutBuffer.getCutBufferStorage().getCell(destRow, destColumn).recognizeContent(contentToCopy);
        } else {
            _cutBuffer.getCutBufferStorage().getCell(destRow, destColumn).recognizeContent(valueToCopy);
        }
    }

    /**
     * Retrieves and displays the content stored in the cut buffer.
     *
     * @return A string representing the content stored in the cut buffer.
     */
    public String showCutBuffer(){
        if(_cutBuffer != null){
            StringBuilder res = new StringBuilder();
            Cell temp;
            int counter = 0;
            for(int i = 1; i <= _cutBuffer.getLines(); i++){
                for(int j = 1; j<= _cutBuffer.getColumns(); j++){
                    temp = _cutBuffer.getCutBufferStorage().getCell(i, j);
                    counter ++;
                    if (temp != null && temp.getContent() != null){
                        res.append(temp.toString())
                        .append("|")
                        .append(obtainValue(temp))
                        .append(temp.getContent().contentToString())
                        .append("\n");
                    } else{
                        res.append(temp.toString())
                        .append("|")
                        .append("\n");
                }
            }
            }
            return res.toString()+counter;
        }
        return "";
    }
}




