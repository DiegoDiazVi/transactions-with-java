import java.sql.*;
import org.apache.log4j.Logger;

public class Account {
    private static final Logger logger = Logger.getLogger(Account.class);

    /* DB CONFIGURATION */
    private static final String URL = "jdbc:mysql://localhost:3306/account";
    private static final String USER = "root";
    private static final String PASS = "rootroot";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    /* SQL QUERIES */
    private static final String SQL_CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS account (
            id INT AUTO_INCREMENT PRIMARY KEY,
            account_number VARCHAR(20) NOT NULL,
            name VARCHAR(50) NOT NULL,
            balance DECIMAL(12,2) NOT NULL
        );""";
    private final static String SQL_INSERT_DATA = "INSERT INTO account (id, account_number, name, balance) VALUES (?,?,?,?)";
    private final static String SQL_SELECT_DATA = "SELECT * FROM account";
    private final static String SQL_UPDATE_DATA = "UPDATE account SET balance = ? WHERE id = ?";

    private Connection connection;


    public void setConnection(Connection connection) {
        this.connection = connection;
        logger.info("DB Connected successfully");
    }

    public Connection getConnection() throws Exception {
        Class.forName(DRIVER);
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public void closeConnection() throws Exception {
        if (this.connection != null) {
            try {
                this.connection.close();
                logger.info("DB Connection Closed Successfully");
            } catch (Exception e) {
                logger.error("Error closing DB connection");
                throw new DatabaseException("Error closing DB connection", e);
            }
        }
    }

    public void createTable() throws SQLException {
        try (Statement statement = this.connection.createStatement()) {
            statement.execute(SQL_CREATE_TABLE);
            logger.info("Table Created Successfully");
        } catch (SQLException e) {
            logger.error("Error creating table");
            throw new DatabaseException("Error creating table", e);
        }
    }

    public void setTableValues(Integer id, Integer accountNumber, String name, Double balance) throws SQLException {
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(SQL_INSERT_DATA)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, accountNumber);
            preparedStatement.setString(3, name);
            preparedStatement.setDouble(4, balance);
            preparedStatement.execute();
            logger.info("Info inserted successfully");
        } catch (SQLException e) {
            logger.error("Error inserting data");
            throw new DatabaseException("Error inserting data", e);
        }
    }

    public void getTableValues() throws SQLException {
        try (Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_DATA)) {

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Account Number: " + resultSet.getInt("account_number"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Balance: " + resultSet.getDouble("balance"));
            }
            logger.info("Data collected");
        } catch (SQLException e) {
            logger.error("Error retrieving data");
            throw new DatabaseException("Error retrieving data", e);
        }
    }

    public void transactionUpdateBalance(Integer id, Double balance) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement preparedStatementUpdate = this.connection.prepareStatement(SQL_UPDATE_DATA)) {
            preparedStatementUpdate.setDouble(1,balance);
            preparedStatementUpdate.setInt(2, id);

            int rowsAffected = preparedStatementUpdate.executeUpdate();
            if ( rowsAffected == 0) {
                throw new DatabaseException("No account found with ID: " + id);
            }
            // int exception = 4/0;
            this.connection.commit();
            logger.info("Balance updated successfully for account");
        } catch (Exception ex) {
            rollbackConnection();
            logger.error("Error updating balance");
            throw new DatabaseException("Error updating balance", ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Error resetting auto-commit");
            }
        }
    }

    private void rollbackConnection() throws SQLException {
        try {
            if (this.connection != null) {
                this.connection.rollback();
                logger.info("Transaction rolled back successfully");
            }
        } catch (SQLException e) {
            logger.error("Error during rollback");
            throw new DatabaseException("Error during rollback", e);
        }
    }
}
