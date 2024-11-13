package xxl;

public class ContentReference extends Content{

    private String _value;
    private String _reference;

    public ContentReference(String reference) {
        _reference = reference;
        _value = reference;
    }

    public String getReference(){
        return _reference;
    }

    @Override
    public String getValue(){
        return _value;
    }

    @Override
    public String contentToString() {
        return _reference;
    }
    
}

