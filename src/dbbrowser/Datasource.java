package dbbrowser;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    private String username;

    private String password;

    private String connectionString;

    private Connection connection = null;


    public Datasource(String connectionString, String username, String password) {
        this.username = username;
        this.password = password;
        this.connectionString = connectionString;
    }

    public Schema getSchema(String schemaName) throws SQLException {

        openConnection();

        Schema schema = new Schema(
                schemaName,
                getTablesForSchema(schemaName)
        );

        closeConnection();

        return schema;

    }


    private void openConnection() throws SQLException {

        try {
            connection = DriverManager.getConnection(connectionString, username, password);

        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error when closing connection");
                }
            }
            throw new SQLException(ex.getMessage());
        }
    }

    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error when closing");
        }
    }

    private List<Table> getTablesForSchema(String schemaName) throws SQLException {

        try (Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT table_name " +
                    "FROM information_schema.tables " +
                    "WHERE table_schema = '" + schemaName + "'");

            List<Table> tables = new ArrayList<>();

            while (resultSet.next()) {

                String tableName = resultSet.getString(1);

                List<Column> columns = getColumnsForTable(tableName, schemaName);

                tables.add(
                        new Table(tableName, columns)
                );

            }

            if (tables.isEmpty()) {
                throw new SQLException("Could not find specified schema");
            }

            return tables;

        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }

    }


    private List<Column> getColumnsForTable(String tableName, String schemaName) {

        try (Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(
                    "SELECT column_name, data_type, extra " +
                            "FROM information_schema.columns " +
                            "WHERE table_name = '" + tableName + "' " +
                            "AND table_schema = '" + schemaName + "' " +
                            "ORDER BY ordinal_position ASC");

            List<Column> columns = new ArrayList<>();

            while (resultSet.next()) {

                String columnName = resultSet.getString(1);
                String columnConstraints = getConstraintsForColumn(columnName, tableName, schemaName);


                columns.add(
                        new Column(
                                columnName,
                                columnConstraints,
                                resultSet.getString(2),
                                resultSet.getString(3)
                        )
                );
            }

            return columns;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }


    }

    private String getConstraintsForColumn(String columnName, String tableName, String schemaName) {
        try (Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(
                        "SELECT tc.constraint_type " +
                            "FROM information_schema.columns c " +
                            "LEFT JOIN information_schema.KEY_COLUMN_USAGE kcu " +
                            "ON (c.table_schema = kcu.table_schema " +
                            "AND c.table_name = kcu.table_name " +
                            "AND c.column_name = kcu.column_name) " +
                            "LEFT JOIN information_schema.table_constraints tc " +
                            "ON (tc.table_schema = c.table_schema " +
                            "AND tc.table_name = c.table_name " +
                            "AND tc.constraint_name = kcu.constraint_name) " +
                            "WHERE c.table_schema = '" + schemaName + "' " +
                            "AND c.table_name = '" + tableName + "' " +
                            "AND c.column_name = '" + columnName + "'"
            );

            StringBuilder constraints = new StringBuilder();

            while (resultSet.next()) {
                if (constraints.length() > 0)
                    constraints.append(", ");
                constraints.append(resultSet.getString(1));
            }

            if (constraints.toString().equals("null")) {
                return "";
            } else return constraints.toString();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

}
