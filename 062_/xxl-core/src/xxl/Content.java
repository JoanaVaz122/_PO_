package xxl;

import java.io.Serializable;

public class Content implements Serializable{
    String _value = null;

    public Content getContent() {
        return null;
    }

    public void setValue(String newValue){
        _value = newValue;
    }

    public String getValue(){
        return _value;
    }

    public String contentToString() {
        return "Not implemented";
    }

    public String execute(String arg1, String arg2){
        return "";
    }

    public String executeIO(String[] arrayArg){
        return "";
    }

}
