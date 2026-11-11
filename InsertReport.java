import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class InsertReport {

    public static void main(String[] args) {

        String url = database.DBConfig.DB_URL;
        String username = database.DBConfig.DB_USER;
        String password = database.DBConfig.DB_PASSWORD;

        String itemName = "Black Wallet";
        String location = "CSE Block";
        String reportType = "Lost";

        String sql = "INSERT INTO reports " +
                "(item_name, location, report_type) " +
                "VALUES (?, ?, ?)";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection =
                    DriverManager.getConnection(url, username, password);

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(1, itemName);
            statement.setString(2, location);
            statement.setString(3, reportType);

            statement.executeUpdate();

            System.out.println("Report inserted successfully!");

            statement.close();
            connection.close();

        } catch (Exception e) {

            System.out.println("Failed to insert report!");
            e.printStackTrace();
        }
    }
}