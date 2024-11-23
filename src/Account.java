import java.sql.*;

import org.apache.log4j.Logger;

public class Account {
    private static final Logger logger = Logger.getLogger(Account.class);
    private static final String URL = "jdbc:mysql://localhost:3306/account";
    private static final String USER = "root";
    private static final String PASS = "rootroot";
    private static Connection connection = null;

    private final static String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS account ( id INT AUTO_INCREMENT PRIMARY KEY, account_number VARCHAR(20) NOT NULL, name VARCHAR(50) NOT NULL, balance DECIMAL(12,2) NOT NULL );";

    private final static String SQL_INSERT_DATA = "INSERT INTO account VALUES (?,?,?,?)";
    private final static String SQL_SELECT_DATA = "SELECT * FROM account";
    private final static String SQL_UPDATE_DATA = "UPDATE account SET balance = ? WHERE id = ?";

    public static void main(String[] args) throws Exception {
        try {
            connection = getConnection();
            logger.info("Connection successful with DB");
            createTable(SQL_CREATE_TABLE);
            setTableValues(2, 761217908, "Diego Diaz", 2345.00, SQL_INSERT_DATA);
            getTableValues(SQL_SELECT_DATA);
            transactionUpdateBalance(SQL_UPDATE_DATA, 2, 989222.02);
            getTableValues(SQL_SELECT_DATA);
        } catch (Exception e) {
            logger.error("Connection failure with DB");
            throw new Exception(e);
        } finally {
            closeConnection();
        }
    }

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void closeConnection() throws Exception {
        if (connection != null && !connection.isClosed()) {
            try {
                connection.close();
                logger.info("Connection Closed Successful");
            } catch (Exception e) {
                logger.error("Error closing connection");
                throw new Exception(e);
            }
        }
    }

    public static void createTable(String sqlQuery) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQuery);
            logger.info("Table Created");
        }
    }

    public static void setTableValues(Integer id, Integer accountNumber, String name, Double balance, String sqlQuery) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, accountNumber);
            preparedStatement.setString(3, name);
            preparedStatement.setDouble(4, balance);
            preparedStatement.execute();
            logger.info("Info inserted");
        }
    }

    public static void getTableValues(String sqlQuery) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Account Number: " + resultSet.getInt("account_number"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Balance: " + resultSet.getDouble("balance"));
            }
            logger.info("Data collected");
        }
    }

    public static void transactionUpdateBalance(String sqlQuery, Integer id, Double balaance) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement preparedStatementUpdate = connection.prepareStatement(sqlQuery)) {
            preparedStatementUpdate.setDouble(1,balaance);
            preparedStatementUpdate.setInt(2, id);
            preparedStatementUpdate.execute();
            connection.commit();
        }
        logger.info("Updated balance");
    }
}
