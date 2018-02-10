package dbbrowser;

import java.util.List;

public class Schema {

    private final String name;

    private List<Table> tables;

    public Schema(String name, List<Table> tableList) {
        this.name = name;
        this.tables = tableList;
    }

    public String getName() {
        return name;
    }

    public List<Table> getTables() {
        return tables;
    }
}
