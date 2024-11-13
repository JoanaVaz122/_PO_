package xxl;

public class Product extends IntervalFunctions {

    private String _startArg;
    private String _endArg;

    public Product(String arg) {
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
        int res = 0;
        for(int i = 0; i < arrayArg.length; i++){
            if(arrayArg[i] == null){
                return "#VALUE";
            }
            try {
                int number = Integer.parseInt(arrayArg[i]);
            } catch (NumberFormatException e) {
                return "#VALUE";
            }
            if(res == 0){
                res += Integer.valueOf(arrayArg[i]);
            }else{
                res *= Integer.valueOf(arrayArg[i]);
            }
        }
        String resString = String.valueOf(res);
        return resString;
    }
}
