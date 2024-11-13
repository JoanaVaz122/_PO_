package xxl;

public class Mul extends BinaryFunctions {

    private String _arg1;
    private String _arg2;

    public Mul(String arg) {
        super(arg);
        parseArg(arg);
    }

    public void parseArg(String arg){
        String[] parsedArg = arg.split(",");
        _arg1 = parsedArg[0];
        _arg2 = parsedArg[1];
    }

    @Override
    public String getArg1(){
        return _arg1;
    }
    
    @Override
    public String getArg2(){
        return _arg2;
    }

    public void setArg1(String inputArg){
        _arg1=inputArg;
    }

    public void setArg2(String inputArg){
        _arg2=inputArg;
    }

    @Override
    public String execute(String arg1, String arg2){
        int resInt;
        String resStr;
        try {
            int number1 = Integer.parseInt(arg1);
            int number2 = Integer.parseInt(arg2);

        } catch (NumberFormatException e) {
            return "#VALUE";
        }
        resInt = Integer.valueOf(arg1) * Integer.valueOf(arg2);
        resStr = String.valueOf(resInt);
        return resStr;
    }
}
