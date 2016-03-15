package com.royalkid.db;

import java.sql.*;

/**
 * @author Vladimir Pantasenko
 * @since JDK1.8
 */
public class DBHandler {
    private static DBHandler thisInstance = new DBHandler();

    private String schema;

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public static DBHandler getInstance() {
        return thisInstance;
    }

    /**
     * Don't let anyone instantiate this class
     */
    private DBHandler() {
    }

    /**
     * Executes query, creates and returns result set of categories.
     *
     * @param connection A Connection. Used to connect to the remote database
     * @return result set of categories
     */
    public ResultSet getCategoriesResultSet(Connection connection) {
        if (connection == null) {
            System.out.println("Can not get categories from DB");
            System.out.print("Shutdown application");
            System.exit(1);
        }

        ResultSet categoriesResultSet = null;

        try {
            connection.setAutoCommit(false);

            String sqlQueryUse = "USE " + schema;

            Statement statement;
            statement = connection.createStatement();
            statement.execute(sqlQueryUse);

            String sqlQuery = "SELECT" +
                    " id," +
                    " parent," +
                    " pagetitle AS title" +
                    " FROM rk_site_content" +
                    " WHERE class_key = 'msCategory'" +
                    " ORDER BY id;";

            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(sqlQuery);
            categoriesResultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Can not get categories from DB");
            DBConnector.getInstance().closeConnection();
            System.out.print("Shutdown application");
            System.exit(1);
        }
        return categoriesResultSet;
    }

    /**
     * Executes query, creates and returns result set of item list.
     *
     * @param connection A Connection. Used to connect to the remote database
     * @return result set of item list
     */
    public ResultSet getItemListResultSet(Connection connection) {
        if (connection == null) {
            System.out.println("Can not get item list from DB");
            System.out.print("Shutdown application");
            System.exit(1);
        }

        ResultSet itemListResultSet = null;

        try {
            connection.setAutoCommit(false);

            String sqlQuery = "USE " + schema;

            Statement statement;
            statement = connection.createStatement();
            statement.execute(sqlQuery);

            String sqlQueryView = "CREATE VIEW temp_db_rk AS" +
                    "  SELECT" +
                    "    rk_site_content.id        'id'," +
                    "    rk_site_content.parent    'category'," +
                    "    rk_ms2_products.image     'img'," +
                    "    rk_site_content.uri       'click'," +
                    "    rk_site_content.parent    'parent'," +
                    "    rk_ms2_products.vendor    'vendor'," +
                    "    rk_ms2_products.article   'article'," +
                    "    rk_site_content.pagetitle 'name'," +
                    "    rk_ms2_products.price     'price'," +
                    "    rk_site_content.introtext 'description'" +
                    "  FROM" +
                    "    rk_site_content" +
                    "    JOIN" +
                    "    rk_ms2_products ON rk_site_content.id = rk_ms2_products.id;";

            Statement statementView;
            statementView = connection.createStatement();
            statementView.execute(sqlQueryView);

            String sqlQueryItems = "SELECT" +
                    "  temp_db_rk.id," +
                    "  temp_db_rk.category," +
                    "  temp_db_rk.img," +
                    "  temp_db_rk.click," +
                    "  rk_site_content.pagetitle 'type'," +
                    "  temp_db_rk.vendor," +
                    "  temp_db_rk.article," +
                    "  temp_db_rk.name," +
                    "  temp_db_rk.price," +
                    "  temp_db_rk.description" +
                    " FROM" +
                    "  temp_db_rk" +
                    "  JOIN" +
                    "  rk_site_content ON temp_db_rk.parent = rk_site_content.id" +
                    " ORDER BY id;";

            Statement statementItems;
            statementItems = connection.createStatement();
            itemListResultSet = statementItems.executeQuery(sqlQueryItems);

            String sqlQueryDropView = "DROP VIEW temp_db_rk;";

            Statement statementDropView;
            statementDropView = connection.createStatement();
            statementDropView.execute(sqlQueryDropView);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Can not get item list from DB");
            DBConnector.getInstance().closeConnection();
            System.out.print("Shutdown application");
            System.exit(1);
        }
        return itemListResultSet;
    }
}
