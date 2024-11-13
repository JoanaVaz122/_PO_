package xxl;

public class ContentLString extends Content{
    private String _value;

    public ContentLString(String value) {
        _value = value;
    }

    @Override
    public String getValue(){
        return _value;
    }

    @Override
    public String contentToString() {
        return "";
    }
    
}
