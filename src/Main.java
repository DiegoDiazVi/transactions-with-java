import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws Exception {
        Account myAccount = new Account();
        Connection connection = null;

        try {
            connection = myAccount.getConnection();
            myAccount.setConnection(connection);

            myAccount.createTable();
            myAccount.setTableValues(4, 991281271, "Ronny Diaz Hernandez", 3500.99);
            myAccount.getTableValues();
            myAccount.transactionUpdateBalance( 2, 1000.99);
            myAccount.getTableValues();
        } catch (Exception e) {
            throw new DatabaseException("Error connecting DB", e);
        } finally {
            myAccount.closeConnection();
        }
    }
}