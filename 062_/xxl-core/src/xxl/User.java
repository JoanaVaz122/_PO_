package xxl;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{

    private List<Spreadsheet> _spreadList = null;
    private String _userName = "root";

    public User(){
        _spreadList = new ArrayList<>();
    }

    public void addSpread(Spreadsheet spreadsheet) {
        _spreadList.add(spreadsheet);
    }

}
