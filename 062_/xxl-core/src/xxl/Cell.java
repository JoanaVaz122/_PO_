package xxl;

import java.io.Serializable;

import xxl.exceptions.UnrecognizedEntryException;

public class Cell implements Serializable{

    private int _n_line;
    private int _n_column;
    private Content _content;

    public Cell(int line, int column){
        _n_line = line;
        _n_column = column;
        _content = null;
    }

    public void setLine(int line){
        _n_line = line;
    }

    public int getLine(){
        return _n_line;
    }

    public void setColumn(int column){
        _n_column = column;
    }

    public int getColumn(){
        return _n_column;
    }

    public void setContent(Content content){
        _content = content;
    }

    public Content getContent(){
        return _content;
    }

    public void recognizeContent(String inputContent) throws UnrecognizedEntryException{
        
        if(inputContent.charAt(0) == '-' || Character.isDigit(inputContent.charAt(0))){ //int case
            _content = new ContentLInt(inputContent);
        }
        if(inputContent.charAt(0) == '\''){ // string case
            _content = new ContentLString(inputContent);
        }
        if(inputContent.charAt(0) == '=' && Character.isDigit(inputContent.charAt(1))){ // reference case
            _content = new ContentReference(inputContent);
        }
        if(inputContent.charAt(0) == '=' && !Character.isDigit(inputContent.charAt(1))){ // function case
            ContentFunction contentFunc = new ContentFunction(inputContent);
            int functionType = contentFunc.recognizeFunct(inputContent);
            switch(functionType){
                case 1:
                    _content = new Add(inputContent);
                    break;
                case 2:
                    _content = new Sub(inputContent);
                    break;
                case 3:
                    _content = new Mul(inputContent);
                    break;
                case 4:
                    _content = new Div(inputContent);
                    break;
                case 5:
                    _content = new Average(inputContent);
                    break;
                case 6:
                    _content = new Coalesce(inputContent);
                    break;
                case 7:
                    _content = new Concat(inputContent);
                    break;
                case 8:
                    _content = new Product(inputContent);
                    break;
                default:
                    throw new UnrecognizedEntryException(inputContent);
            } 
        }
    }

    public String toString(){
        return _n_line + ";" + _n_column;
    }
    
}
