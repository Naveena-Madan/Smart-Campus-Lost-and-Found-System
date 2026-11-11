package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Admin-side AI Scanner.
 *
 * Scans one Found report against Lost reports.
 *
 * This uses the same text-similarity approach as SmartMatchService:
 * Item similarity  = 80%
 * Location         = 20%
 * Minimum item similarity = 60%
 *
 * The scanner is decision support only.
 * The final decision remains with the administrator.
 */
public class AIScannerService {

    private static final String DB_URL = database.DBConfig.DB_URL;

    private static final String DB_USER = database.DBConfig.DB_USER;

    private static final String DB_PASSWORD = database.DBConfig.DB_PASSWORD;

    private static final double ITEM_WEIGHT = 0.80;
    private static final double LOCATION_WEIGHT = 0.20;
    private static final double MINIMUM_ITEM_SCORE = 60.0;

    public List<MatchResult> scanFoundReport(
            int foundReportId
    ) throws Exception {

        List<MatchResult> matches =
                new ArrayList<>();

        Class.forName(
                "com.mysql.cj.jdbc.Driver"
        );

        String foundSQL =
                "SELECT item_name, location "
                        + "FROM reports "
                        + "WHERE id = ? "
                        + "AND LOWER(report_type) = 'found'";

        String lostSQL =
                "SELECT id, item_name, location, "
                        + "description, identifying_details "
                        + "FROM reports "
                        + "WHERE LOWER(report_type) = 'lost' "
                        + "AND (status IS NULL "
                        + "OR LOWER(status) <> 'returned')";

        try (
                Connection connection =
                        DriverManager.getConnection(
                                DB_URL,
                                DB_USER,
                                DB_PASSWORD
                        );

                PreparedStatement foundStatement =
                        connection.prepareStatement(
                                foundSQL
                        )
        ) {

            foundStatement.setInt(
                    1,
                    foundReportId
            );

            try (
                    ResultSet foundResult =
                            foundStatement.executeQuery()
            ) {

                if (!foundResult.next()) {
                    throw new IllegalArgumentException(
                            "Found report was not found."
                    );
                }

                String foundItem =
                        foundResult.getString(
                                "item_name"
                        );

                String foundLocation =
                        foundResult.getString(
                                "location"
                        );

                try (
                        PreparedStatement lostStatement =
                                connection.prepareStatement(
                                        lostSQL
                                );

                        ResultSet lostResult =
                                lostStatement.executeQuery()
                ) {

                    while (lostResult.next()) {

                        int lostReportId =
                                lostResult.getInt(
                                        "id"
                                );

                        String lostItem =
                                lostResult.getString(
                                        "item_name"
                                );

                        String lostLocation =
                                lostResult.getString(
                                        "location"
                                );

                        double itemScore =
                                calculateItemSimilarity(
                                        lostItem,
                                        foundItem
                                );

                        // Location must not rescue an
                        // unrelated item.
                        if (itemScore
                                < MINIMUM_ITEM_SCORE) {
                            continue;
                        }

                        double locationScore =
                                calculateLocationSimilarity(
                                        lostLocation,
                                        foundLocation
                                );

                        double finalScore =
                                (itemScore * ITEM_WEIGHT)
                                        +
                                        (locationScore
                                                * LOCATION_WEIGHT);

                        String confidence =
                                getConfidence(
                                        finalScore
                                );

                        matches.add(
                                new MatchResult(
                                        lostReportId,
                                        lostItem,
                                        lostLocation,
                                        lostResultDescription(
                                                lostResult,
                                                "description"
                                        ),
                                        lostResultDescription(
                                                lostResult,
                                                "identifying_details"
                                        ),
                                        finalScore,
                                        confidence
                                )
                        );
                    }
                }
            }
        }

        matches.sort(
                Comparator.comparingDouble(
                        MatchResult::getMatchScore
                ).reversed()
        );

        return matches;
    }

    private String lostResultDescription(
            ResultSet result,
            String column
    ) throws Exception {

        String value =
                result.getString(column);

        return value == null
                ? ""
                : value;
    }

    private double calculateItemSimilarity(
            String lostItem,
            String foundItem
    ) {

        if (lostItem == null
                || foundItem == null) {
            return 0;
        }

        String lost =
                normalize(lostItem);

        String found =
                normalize(foundItem);

        if (lost.equals(found)) {
            return 100;
        }

        Set<String> lostWords =
                getMeaningfulWords(lost);

        Set<String> foundWords =
                getMeaningfulWords(found);

        if (lostWords.isEmpty()
                || foundWords.isEmpty()) {
            return 0;
        }

        int commonWords = 0;

        for (String word : lostWords) {

            if (foundWords.contains(word)) {
                commonWords++;
            }
        }

        double tokenScore =
                ((double) commonWords
                        / lostWords.size())
                        * 100;

        double partialScore =
                calculatePartialWordScore(
                        lostWords,
                        foundWords
                );

        double itemScore =
                Math.max(
                        tokenScore,
                        partialScore
                );

        if (lost.contains(found)
                || found.contains(lost)) {

            itemScore =
                    Math.max(
                            itemScore,
                            100
                    );
        }

        return Math.min(
                itemScore,
                100
        );
    }

    private double calculatePartialWordScore(
            Set<String> lostWords,
            Set<String> foundWords
    ) {

        if (lostWords.isEmpty()) {
            return 0;
        }

        int matchedWords = 0;

        for (String lostWord : lostWords) {

            for (String foundWord : foundWords) {

                if (lostWord.equals(foundWord)) {

                    matchedWords++;
                    break;
                }

                if (lostWord.length() >= 5
                        && foundWord.length() >= 5) {

                    if (lostWord.startsWith(foundWord)
                            || foundWord.startsWith(lostWord)) {

                        matchedWords++;
                        break;
                    }
                }
            }
        }

        return
                ((double) matchedWords
                        / lostWords.size())
                        * 100;
    }

    private double calculateLocationSimilarity(
            String lostLocation,
            String foundLocation
    ) {

        if (lostLocation == null
                || foundLocation == null) {
            return 0;
        }

        String lost =
                normalize(lostLocation);

        String found =
                normalize(foundLocation);

        if (lost.equals(found)) {
            return 100;
        }

        if (lost.contains(found)
                || found.contains(lost)) {
            return 80;
        }

        Set<String> lostWords =
                getMeaningfulWords(lost);

        Set<String> foundWords =
                getMeaningfulWords(found);

        if (lostWords.isEmpty()
                || foundWords.isEmpty()) {
            return 0;
        }

        int commonWords = 0;

        for (String word : lostWords) {

            if (foundWords.contains(word)) {
                commonWords++;
            }
        }

        return
                ((double) commonWords
                        / lostWords.size())
                        * 100;
    }

    private String normalize(
            String text
    ) {

        return text
                .toLowerCase()
                .replaceAll(
                        "[^a-z0-9 ]",
                        " "
                )
                .replaceAll(
                        "\\s+",
                        " "
                )
                .trim();
    }

    private Set<String> getMeaningfulWords(
            String text
    ) {

        Set<String> words =
                new HashSet<>();

        if (text == null
                || text.trim().isEmpty()) {
            return words;
        }

        String[] tokens =
                text.split("\\s+");

        for (String token : tokens) {

            if (token.length() >= 2) {
                words.add(token);
            }
        }

        return words;
    }

    private String getConfidence(
            double score
    ) {

        if (score >= 80) {
            return "HIGH";
        }

        if (score >= 60) {
            return "MEDIUM";
        }

        return "LOW";
    }

    public static class MatchResult {

        private final int lostReportId;
        private final String lostItem;
        private final String lostLocation;
        private final String description;
        private final String identifyingDetails;
        private final double matchScore;
        private final String confidence;

        public MatchResult(
                int lostReportId,
                String lostItem,
                String lostLocation,
                String description,
                String identifyingDetails,
                double matchScore,
                String confidence
        ) {

            this.lostReportId = lostReportId;
            this.lostItem = lostItem;
            this.lostLocation = lostLocation;
            this.description = description;
            this.identifyingDetails =
                    identifyingDetails;
            this.matchScore = matchScore;
            this.confidence = confidence;
        }

        public int getLostReportId() {
            return lostReportId;
        }

        public String getLostItem() {
            return lostItem;
        }

        public String getLostLocation() {
            return lostLocation;
        }

        public String getDescription() {
            return description;
        }

        public String getIdentifyingDetails() {
            return identifyingDetails;
        }

        public double getMatchScore() {
            return matchScore;
        }

        public String getConfidence() {
            return confidence;
        }
    }
}
