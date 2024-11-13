package xxl;

public class ContentLInt extends Content{
    private String _value;

    public ContentLInt(String value) {
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

