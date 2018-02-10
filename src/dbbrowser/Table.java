package dbbrowser;

import java.util.List;

public class Table {

    private final String name;

    private final List<Column> columns;

    public Table(String name, List<Column> columnList) {
        this.name = name;
        this.columns = columnList;
    }

    public String getName() {
        return name;
    }

    public List<Column> getColumns() {
        return columns;
    }
}
