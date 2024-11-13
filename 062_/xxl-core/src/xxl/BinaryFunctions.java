package xxl;

public class BinaryFunctions extends ContentFunction{

    private String _arg1;
    private String _arg2;

    public BinaryFunctions(String arg) {
        super(arg);
    }

    public String getArg1(){
        return _arg1;
    }

    public String getArg2(){
        return _arg2;
    }

    @Override
    public String execute(String arg1, String arg2){
        return "";
    }
}
