package dbbrowser;

import java.sql.SQLException;

public class Main {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final String SCHEMA_NAME = "library";
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/?useSSL=true";

    public static void main(String[] args) {
        Datasource datasource = new Datasource(CONNECTION_STRING, USERNAME, PASSWORD);

        Schema schema;

        try {
            schema = datasource.getSchema(SCHEMA_NAME);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return;
        }

        for (Table table : schema.getTables()) {
            System.out.println(table.getName());
            for (Column column : table.getColumns()) {
                System.out.println(column.getName() + " " + column.getDataType() + " " + column.getConstraints()
                        + " " + column.getExtraAttribute());

            }
            System.out.println();
        }

    }
}
