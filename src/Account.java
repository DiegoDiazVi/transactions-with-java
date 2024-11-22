import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class Account {
    private static Logger logger = Logger.getLogger(Account.class);
    private static final String URL = "jdbc:mysql://localhost:3306/account";
    private static final String USER = "root";
    private static final String PASS = "rootroot";
    private static Connection connection = null;

    public static void main(String[] args) throws Exception {
        try {
            connection = getConnection();
            logger.info("Connection successful with DB");
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
}
