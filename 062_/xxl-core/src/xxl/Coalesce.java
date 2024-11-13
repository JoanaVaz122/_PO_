package xxl;

public class Coalesce extends IntervalFunctions{

    private String _startArg;
    private String _endArg;

    public Coalesce(String arg) {
        super(arg);
        parseArg(arg);
    }

    public void parseArg(String arg){
        String[] parsedArg = arg.split(":");
        _startArg = parsedArg[0];
        _endArg = parsedArg[1];
    }
    
    @Override
    public String executeIO(String[] arrayArg){
        for(int i = 0; i < arrayArg.length; i++){
            if(arrayArg[i] == null){
                continue;
            }else if(arrayArg[i].startsWith("'")){
                return arrayArg[i];
            }
        }
        return "'";
    }
}
