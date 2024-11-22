import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.log4j.Logger;

public class Account {
    private static Logger logger = Logger.getLogger(Account.class);
    private static final String URL = "jdbc:mysql://localhost:3306/account";
    private static final String USER = "root";
    private static final String PASS = "rootroot";

    public static void main(String[] args) throws Exception {
        Connection connection = null;
        try {
            connection = getConnection();
            logger.info("Connection successful with DB");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Connection failure with DB");
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Error closing connection");
            }
        }
    }

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
