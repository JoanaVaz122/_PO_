package xxl;

public class ContentFunction extends Content{
    
    private String _value;
    private String _function;

    public ContentFunction(String function) {
        _function = function;
        _value = function;
    }

    @Override
    public String getValue(){
        return _value;
    }

    public int recognizeFunct(String inputFunction){
        String function = inputFunction.substring(inputFunction.indexOf("=") + 1, inputFunction.indexOf("("));
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

    @Override
    public String contentToString(){
        return _function;
    }
}
