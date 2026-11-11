package service;

import database.DBConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SmartMatchService {

    // =========================================================
    // DATABASE
    // =========================================================

    private static final String DB_URL = DBConfig.DB_URL;

    private static final String DB_USER = DBConfig.DB_USER;

    private static final String DB_PASSWORD = DBConfig.DB_PASSWORD;


    // =========================================================
    // MATCHING SETTINGS
    // =========================================================

    /*
     * Item is much more important than location.
     *
     * Item     = 80%
     * Location = 20%
     *
     * An item must have at least 60% similarity
     * before it can appear as a possible match.
     */

    private static final double ITEM_WEIGHT = 0.80;

    private static final double LOCATION_WEIGHT = 0.20;

    private static final double MINIMUM_ITEM_SCORE = 60.0;


    // =========================================================
    // FIND MATCHES
    // =========================================================

    public List<String> findMatches(
            String lostItem,
            String lostLocation
    ) {

        List<String> matches =
                new ArrayList<>();


        // =====================================================
        // VALIDATION
        // =====================================================

        if (lostItem == null ||
                lostItem.trim().isEmpty()) {

            return matches;
        }


        if (lostLocation == null) {

            lostLocation = "";
        }


        // =====================================================
        // SQL
        // =====================================================

        String sql =
                "SELECT id, item_name, location " +
                        "FROM reports " +
                        "WHERE report_type = 'Found' " +
                        "AND (status IS NULL OR status <> 'Returned')";


        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );


            try (
                    Connection connection =
                            DriverManager.getConnection(
                                    DB_URL,
                                    DB_USER,
                                    DB_PASSWORD
                            );

                    PreparedStatement statement =
                            connection.prepareStatement(
                                    sql
                            );

                    ResultSet result =
                            statement.executeQuery()
            ) {


                // =================================================
                // CHECK EVERY FOUND ITEM
                // =================================================

                while (result.next()) {

                    int reportId =
                            result.getInt(
                                    "id"
                            );


                    String foundItem =
                            result.getString(
                                    "item_name"
                            );


                    String foundLocation =
                            result.getString(
                                    "location"
                            );


                    // =============================================
                    // 1. ITEM SIMILARITY
                    // =============================================

                    double itemScore =
                            calculateItemSimilarity(
                                    lostItem,
                                    foundItem
                            );


                    // =============================================
                    // IMPORTANT FILTER
                    // =============================================
                    //
                    // Location MUST NOT rescue an unrelated item.
                    //
                    // Example:
                    //
                    // Lost:
                    // Black Dell Laptop
                    //
                    // Found:
                    // Blue Water Bottle
                    //
                    // Both in Library.
                    //
                    // Item score = 0
                    //
                    // Therefore:
                    // DO NOT DISPLAY
                    //

                    if (itemScore <
                            MINIMUM_ITEM_SCORE) {

                        continue;
                    }


                    // =============================================
                    // 2. LOCATION SIMILARITY
                    // =============================================

                    double locationScore =
                            calculateLocationSimilarity(
                                    lostLocation,
                                    foundLocation
                            );


                    // =============================================
                    // 3. FINAL MATCH SCORE
                    // =============================================

                    double finalScore =
                            (itemScore * ITEM_WEIGHT)
                                    +
                                    (locationScore * LOCATION_WEIGHT);


                    int roundedScore =
                            (int) Math.round(
                                    finalScore
                            );


                    // =============================================
                    // 4. CREATE RESULT
                    // =============================================

                    String match =
                            "Report ID: "
                                    + reportId
                                    + "\n"
                                    + "Found Item: "
                                    + foundItem
                                    + "\n"
                                    + "Location: "
                                    + foundLocation
                                    + "\n"
                                    + "Match Score: "
                                    + roundedScore
                                    + "%";


                    matches.add(
                            match
                    );
                }
            }


        } catch (Exception e) {

            e.printStackTrace();
        }


        // =========================================================
        // SORT BY HIGHEST SCORE
        // =========================================================

        matches.sort(
                (first, second) ->
                        Integer.compare(
                                extractScore(second),
                                extractScore(first)
                        )
        );


        return matches;
    }


    // =========================================================
    // ITEM SIMILARITY
    // =========================================================

    private double calculateItemSimilarity(
            String lostItem,
            String foundItem
    ) {

        if (lostItem == null ||
                foundItem == null) {

            return 0;
        }


        String lost =
                normalize(
                        lostItem
                );


        String found =
                normalize(
                        foundItem
                );


        // =====================================================
        // EXACT MATCH
        // =====================================================

        if (lost.equals(found)) {

            return 100;
        }


        // =====================================================
        // GET WORDS
        // =====================================================

        Set<String> lostWords =
                getMeaningfulWords(
                        lost
                );


        Set<String> foundWords =
                getMeaningfulWords(
                        found
                );


        if (lostWords.isEmpty() ||
                foundWords.isEmpty()) {

            return 0;
        }


        // =====================================================
        // COUNT COMMON WORDS
        // =====================================================

        int commonWords = 0;


        for (String word : lostWords) {

            if (foundWords.contains(word)) {

                commonWords++;
            }
        }


        // =====================================================
        // NO COMMON WORD
        // =====================================================

        if (commonWords == 0) {

            return 0;
        }


        // =====================================================
        // TOKEN SCORE
        // =====================================================
        //
        // Example:
        //
        // Lost:
        // black dell laptop
        //
        // Found:
        // dell laptop
        //
        // Common = dell, laptop
        // Lost words = 3
        //
        // Score = 2/3 × 100 = 66.7%
        //

        double tokenScore =
                ((double) commonWords
                        /
                        lostWords.size())
                        * 100;


        // =====================================================
        // PARTIAL WORD SCORE
        // =====================================================

        double partialScore =
                calculatePartialWordScore(
                        lostWords,
                        foundWords
                );


        // =====================================================
        // USE THE STRONGER SCORE
        // =====================================================

        double itemScore =
                Math.max(
                        tokenScore,
                        partialScore
                );


        // =====================================================
        // CONTAINMENT MATCH
        // =====================================================
        //
        // Example:
        //
        // Lost:
        // Dell Laptop
        //
        // Found:
        // Black Dell Laptop
        //
        // This is considered a strong match.
        //

        if (lost.contains(found) ||
                found.contains(lost)) {

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


    // =========================================================
    // PARTIAL WORD SCORE
    // =========================================================

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

                // Exact word
                if (lostWord.equals(foundWord)) {

                    matchedWords++;

                    break;
                }


                // Small variations
                //
                // laptop / laptops
                // bottle / bottles

                if (lostWord.length() >= 5 &&
                        foundWord.length() >= 5) {

                    if (lostWord.startsWith(foundWord) ||
                            foundWord.startsWith(lostWord)) {

                        matchedWords++;

                        break;
                    }
                }
            }
        }


        return
                ((double) matchedWords
                        /
                        lostWords.size())
                        * 100;
    }


    // =========================================================
    // LOCATION SIMILARITY
    // =========================================================

    private double calculateLocationSimilarity(
            String lostLocation,
            String foundLocation
    ) {

        if (lostLocation == null ||
                foundLocation == null) {

            return 0;
        }


        String lost =
                normalize(
                        lostLocation
                );


        String found =
                normalize(
                        foundLocation
                );


        // =====================================================
        // EXACT LOCATION
        // =====================================================

        if (lost.equals(found)) {

            return 100;
        }


        // =====================================================
        // CONTAINMENT
        // =====================================================

        if (lost.contains(found) ||
                found.contains(lost)) {

            return 80;
        }


        // =====================================================
        // WORD COMPARISON
        // =====================================================

        Set<String> lostWords =
                getMeaningfulWords(
                        lost
                );


        Set<String> foundWords =
                getMeaningfulWords(
                        found
                );


        if (lostWords.isEmpty() ||
                foundWords.isEmpty()) {

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
                        /
                        lostWords.size())
                        * 100;
    }


    // =========================================================
    // NORMALIZE TEXT
    // =========================================================

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


    // =========================================================
    // GET MEANINGFUL WORDS
    // =========================================================

    private Set<String> getMeaningfulWords(
            String text
    ) {

        Set<String> words =
                new HashSet<>();


        if (text == null ||
                text.trim().isEmpty()) {

            return words;
        }


        String[] tokens =
                text.split(
                        "\\s+"
                );


        for (String token : tokens) {

            if (token.length() >= 2) {

                words.add(
                        token
                );
            }
        }


        return words;
    }


    // =========================================================
    // EXTRACT SCORE
    // =========================================================

    private int extractScore(
            String match
    ) {

        try {

            String[] lines =
                    match.split(
                            "\n"
                    );


            for (String line : lines) {

                if (line.startsWith(
                        "Match Score:"
                )) {

                    String score =
                            line.substring(
                                            "Match Score:"
                                                    .length()
                                    )
                                    .replace(
                                            "%",
                                            ""
                                    )
                                    .trim();


                    return Integer.parseInt(
                            score
                    );
                }
            }


        } catch (Exception e) {

            e.printStackTrace();
        }


        return 0;
    }
}