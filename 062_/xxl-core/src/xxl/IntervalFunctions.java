package xxl;

public class IntervalFunctions extends ContentFunction{

    private String _startArg;
    private String _endArg;

    public IntervalFunctions(String arg) {
        super(arg);
    }

    public String getStartArg(){
        return _startArg;
    }

    public String getEndArg(){
        return _endArg;
    }

    @Override
    public String executeIO(String[] arrayArg){
        return "";
    }

}

