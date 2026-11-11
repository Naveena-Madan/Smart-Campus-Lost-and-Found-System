package database;

public class DBConfig {

    public static final String DB_URL =
            "jdbc:mysql://localhost:3306/lost_found_db";

    public static final String DB_USER = "root";

    public static final String DB_PASSWORD =
            System.getenv("LOST_FOUND_DB_PASSWORD");
}
