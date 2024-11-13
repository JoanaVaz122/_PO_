package xxl;

public class Concat extends IntervalFunctions{

    private String _startArg;
    private String _endArg;

    public Concat(String arg) {
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
        String res = "'";
        for(int i = 0; i < arrayArg.length; i++){
            if(arrayArg[i] == null){
                continue;
            }else if (arrayArg[i].startsWith("'")){
                res+=(arrayArg[i].substring(1));
            }
        }
        return res;
    }
}
