package xxl;

public class Average extends IntervalFunctions{

    private String _startArg;
    private String _endArg;

    public Average(String arg) {
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
        int sum = 0;
        for(int i = 0; i < arrayArg.length; i++){
            if(arrayArg[i] == null){
                return "#VALUE";
            }
            try {
                int number = Integer.parseInt(arrayArg[i]);
            } catch (NumberFormatException e) {
                return "#VALUE";
            }
            sum += Integer.valueOf(arrayArg[i]);
        }
        int resInt = sum / arrayArg.length;
        String resString = String.valueOf(resInt);
        return resString;
    }
}
    

