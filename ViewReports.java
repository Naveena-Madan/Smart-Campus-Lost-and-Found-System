import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewReports {

    public static void main(String[] args) {

        String url = database.DBConfig.DB_URL;
        String username = database.DBConfig.DB_USER;
        String password = database.DBConfig.DB_PASSWORD;

        String sql = "SELECT * FROM reports";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection =
                    DriverManager.getConnection(url, username, password);

            Statement statement = connection.createStatement();

            ResultSet result =
                    statement.executeQuery(sql);

            while (result.next()) {

                System.out.println(
                        "ID: " + result.getInt("id")
                );

                System.out.println(
                        "Item: " + result.getString("item_name")
                );

                System.out.println(
                        "Location: " + result.getString("location")
                );

                System.out.println(
                        "Type: " + result.getString("report_type")
                );

                System.out.println(
                        "Status: " + result.getString("status")
                );

                System.out.println("--------------------");
            }

            result.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            System.out.println("Failed to retrieve reports!");
            e.printStackTrace();
        }
    }
}
