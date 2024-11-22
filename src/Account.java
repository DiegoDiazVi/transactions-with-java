import java.sql.Connection;
import java.sql.DriverManager;

public class Account {
    private static final String URL = "jdbc:mysql://localhost:3306/account";
    private static final String USER = "root";
    private static final String PASS = "rootroot";

    public static void main(String[] args) throws Exception {
        Connection connection = null;
        try {
            connection = getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
