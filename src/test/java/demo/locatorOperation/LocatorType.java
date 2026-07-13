package demo.locatorOperation;

public enum LocatorType {
    CSS("css"),
    XPATH(""),
    ID(""),
    NAME(""),
    CLASS(""),
    TEXT("");

    String locatorTag ;
    LocatorType(String css) {
        this.locatorTag = css;
    }

    public String getLocatorTag(Enum tag)
    {
        return locatorTag;
    }
}
