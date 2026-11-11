package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportService {

    private static final String DB_URL = database.DBConfig.DB_URL;

    private static final String DB_USER = database.DBConfig.DB_USER;

    private static final String DB_PASSWORD = database.DBConfig.DB_PASSWORD;

    /**
     * Saves a Lost or Found report into the reports table.
     *
     * If the report is FOUND, the system checks existing LOST
     * reports and creates notifications for matching users.
     *
     * @return generated report ID
     */
    public int saveReport(
            String itemName,
            String location,
            String reportType,
            String username,
            String eventTime,
            String description,
            String identifyingDetails,
            String currentLocation,
            String additionalDetails,
            String imagePath
    ) throws SQLException {

        String sql =
                "INSERT INTO reports "
                        + "(item_name, location, report_type, status, username, "
                        + "event_time, description, identifying_details, "
                        + "current_location, additional_details, image_path) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL JDBC Driver not found.",
                    e
            );
        }

        int reportId;

        try (
                Connection connection =
                        DriverManager.getConnection(
                                DB_URL,
                                DB_USER,
                                DB_PASSWORD
                        );

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                java.sql.Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(1, itemName);
            statement.setString(2, location);
            statement.setString(3, reportType);
            statement.setString(4, "Pending");
            statement.setString(5, username);
            statement.setString(6, eventTime);
            statement.setString(7, description);
            statement.setString(8, identifyingDetails);
            statement.setString(9, currentLocation);
            statement.setString(10, additionalDetails);
            statement.setString(11, imagePath);

            int rowsAffected =
                    statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException(
                        "Report could not be saved."
                );
            }

            try (
                    ResultSet generatedKeys =
                            statement.getGeneratedKeys()
            ) {

                if (generatedKeys.next()) {

                    reportId =
                            generatedKeys.getInt(1);

                } else {

                    throw new SQLException(
                            "Report was saved, but its ID could not be retrieved."
                    );
                }
            }
        }

        /*
         * Only FOUND reports trigger automatic matching.
         *
         * LOST reports remain stored in MySQL until a matching
         * FOUND report is submitted later.
         */
        if ("FOUND".equalsIgnoreCase(reportType)) {

            try {

                createNotificationsForMatchingLostReports(
                        reportId,
                        itemName,
                        location,
                        username
                );

            } catch (Exception e) {

                /*
                 * The report itself has already been saved.
                 * Therefore, notification failure should not
                 * make the user think that the report failed.
                 */
                System.out.println(
                        "Report saved successfully, "
                                + "but automatic notification processing failed."
                );

                e.printStackTrace();
            }
        }

        return reportId;
    }

    /**
     * Checks existing LOST reports against the newly submitted
     * FOUND report.
     */
    private void createNotificationsForMatchingLostReports(
            int foundReportId,
            String foundItemName,
            String foundLocation,
            String foundUsername
    ) throws SQLException {

        String sql =
                "SELECT id, item_name, location, username "
                        + "FROM reports "
                        + "WHERE report_type = 'LOST' "
                        + "AND (status IS NULL OR status <> 'Returned')";

        try (
                Connection connection =
                        DriverManager.getConnection(
                                DB_URL,
                                DB_USER,
                                DB_PASSWORD
                        );

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                int lostReportId =
                        resultSet.getInt("id");

                String lostItemName =
                        resultSet.getString("item_name");

                String lostLocation =
                        resultSet.getString("location");

                String lostUsername =
                        resultSet.getString("username");

                /*
                 * Do not notify the person who submitted
                 * the FOUND report about their own report.
                 */
                if (lostUsername == null
                        || lostUsername.equalsIgnoreCase(foundUsername)) {

                    continue;
                }

                /*
                 * Use the existing SmartMatchService so the
                 * same matching logic is used throughout the app.
                 */
                SmartMatchService smartMatchService =
                        new SmartMatchService();

                java.util.List<String> matches =
                        smartMatchService.findMatches(
                                lostItemName,
                                lostLocation
                        );

                /*
                 * Check whether the newly submitted FOUND
                 * report appears in the match results.
                 */
                boolean matched =
                        containsReportId(
                                matches,
                                foundReportId
                        );

                if (matched) {

                    String message =
                            "Possible match found for your lost item: "
                                    + lostItemName
                                    + ". A new found-item report may match it.";

                    createNotificationIfNeeded(
                            lostUsername,
                            foundReportId,
                            message
                    );

                    System.out.println(
                            "Notification created for user: "
                                    + lostUsername
                                    + " | Lost Report ID: "
                                    + lostReportId
                                    + " | Found Report ID: "
                                    + foundReportId
                    );
                }
            }
        }
    }

    /**
     * Checks whether the Smart Match results contain
     * the specified FOUND report ID.
     */
    private boolean containsReportId(
            java.util.List<String> matches,
            int reportId
    ) {

        if (matches == null || matches.isEmpty()) {
            return false;
        }

        String requiredText =
                "Report ID: " + reportId;

        for (String match : matches) {

            if (match != null
                    && match.contains(requiredText)) {

                return true;
            }
        }

        return false;
    }

    /**
     * Creates a notification containing the matched
     * FOUND report ID.
     *
     * Duplicate unread notifications are avoided.
     */
    private void createNotificationIfNeeded(
            String username,
            int reportId,
            String message
    ) throws SQLException {

        String checkSql =
                "SELECT COUNT(*) "
                        + "FROM notifications "
                        + "WHERE username = ? "
                        + "AND report_id = ? "
                        + "AND message = ? "
                        + "AND status = 'UNREAD'";

        String insertSql =
                "INSERT INTO notifications "
                        + "(username, report_id, message, status) "
                        + "VALUES (?, ?, ?, 'UNREAD')";

        try (
                Connection connection =
                        DriverManager.getConnection(
                                DB_URL,
                                DB_USER,
                                DB_PASSWORD
                        );

                PreparedStatement checkStatement =
                        connection.prepareStatement(checkSql)
        ) {

            checkStatement.setString(1, username);
            checkStatement.setInt(2, reportId);
            checkStatement.setString(3, message);

            try (
                    ResultSet resultSet =
                            checkStatement.executeQuery()
            ) {

                if (resultSet.next()) {

                    int count =
                            resultSet.getInt(1);

                    /*
                     * Avoid duplicate unread notifications
                     * for the same matched report.
                     */
                    if (count > 0) {
                        return;
                    }
                }
            }

            try (
                    PreparedStatement insertStatement =
                            connection.prepareStatement(insertSql)
            ) {

                insertStatement.setString(1, username);
                insertStatement.setInt(2, reportId);
                insertStatement.setString(3, message);

                insertStatement.executeUpdate();
            }
        }
    }
}