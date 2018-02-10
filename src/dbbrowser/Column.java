package dbbrowser;


public class Column {
    private final String name;
    private final String constraints;
    private final String dataType;
    private final String extraAttribute;

    public String getName() {
        return name;
    }

    public String getConstraints() {
        return constraints;
    }

    public String getDataType() {
        return dataType;
    }

    public String getExtraAttribute() {
        return extraAttribute;
    }

    public Column(String name, String constraints, String dataType, String extraAttribute) {
        this.name = name;
        this.constraints = constraints;
        this.dataType = dataType;
        this.extraAttribute = extraAttribute;
    }
}
