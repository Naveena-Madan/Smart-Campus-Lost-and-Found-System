package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * OwnershipVerificationService
 *
 * Beginner-friendly text similarity service for ownership verification.
 * It assists the administrator; it does NOT automatically approve/reject claims.
 */
public class OwnershipVerificationService {

    private static final String DB_URL = database.DBConfig.DB_URL;

    private static final String DB_USERNAME = database.DBConfig.DB_USER;

    private static final String DB_PASSWORD = database.DBConfig.DB_PASSWORD;

    public static class VerificationResult {
        private final double score;
        private final String confidence;
        private final String recommendation;

        public VerificationResult(double score, String confidence,
                                  String recommendation) {
            this.score = score;
            this.confidence = confidence;
            this.recommendation = recommendation;
        }

        public double getScore() {
            return score;
        }

        public String getConfidence() {
            return confidence;
        }

        public String getRecommendation() {
            return recommendation;
        }
    }

    /**
     * Compares the student's ownership answer with the original report.
     */
    public VerificationResult verifyOwnership(int reportId, String claimAnswer) {

        if (claimAnswer == null || claimAnswer.trim().isEmpty()) {
            return new VerificationResult(
                    0.0,
                    "LOW",
                    "No ownership evidence was provided."
            );
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String sql =
                    "SELECT item_name, description, identifying_details, "
                            + "additional_details FROM reports WHERE id = ?";

            try (Connection connection = DriverManager.getConnection(
                    DB_URL, DB_USERNAME, DB_PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, reportId);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (!resultSet.next()) {
                        return new VerificationResult(
                                0.0,
                                "LOW",
                                "Original report information was not found."
                        );
                    }

                    String referenceText = buildReferenceText(
                            resultSet.getString("item_name"),
                            resultSet.getString("description"),
                            resultSet.getString("identifying_details"),
                            resultSet.getString("additional_details")
                    );

                    double score = calculateSimilarity(
                            referenceText,
                            claimAnswer
                    );

                    String confidence = getConfidence(score);

                    return new VerificationResult(
                            score,
                            confidence,
                            getRecommendation(confidence)
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            return new VerificationResult(
                    0.0,
                    "LOW",
                    "Verification could not be completed."
            );
        }
    }

    private String buildReferenceText(String itemName,
                                      String description,
                                      String identifyingDetails,
                                      String additionalDetails) {

        return safeText(itemName) + " "
                + safeText(description) + " "
                + safeText(identifyingDetails) + " "
                + safeText(additionalDetails);
    }

    /**
     * Calculates the percentage of meaningful claim words
     * that are also present in the report information.
     */
    private double calculateSimilarity(String referenceText,
                                       String claimAnswer) {

        Set<String> referenceWords = tokenize(referenceText);
        Set<String> claimWords = tokenize(claimAnswer);

        if (referenceWords.isEmpty() || claimWords.isEmpty()) {
            return 0.0;
        }

        Set<String> matchingWords = new HashSet<>(claimWords);
        matchingWords.retainAll(referenceWords);

        double score =
                (matchingWords.size() * 100.0) / claimWords.size();

        return Math.min(100.0, Math.max(0.0, score));
    }

    private Set<String> tokenize(String text) {

        if (text == null) {
            return new HashSet<>();
        }

        String cleaned = text.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", " ");

        Set<String> stopWords = new HashSet<>(Arrays.asList(
                "the", "a", "an", "is", "it", "has", "have",
                "and", "or", "of", "to", "in", "on", "with",
                "my", "this", "that", "there", "was", "were",
                "i", "me", "its"
        ));

        return Arrays.stream(cleaned.split("\\s+"))
                .filter(word -> !word.isEmpty())
                .filter(word -> word.length() > 2)
                .filter(word -> !stopWords.contains(word))
                .collect(Collectors.toSet());
    }

    private String getConfidence(double score) {

        if (score >= 70) {
            return "HIGH";
        }

        if (score >= 40) {
            return "MEDIUM";
        }

        return "LOW";
    }

    private String getRecommendation(String confidence) {

        switch (confidence) {
            case "HIGH":
                return "Strong ownership evidence. Admin should review the claim.";

            case "MEDIUM":
                return "Some ownership evidence matches. Admin should verify additional details.";

            default:
                return "Weak ownership evidence. Admin should carefully review the claim.";
        }
    }

    private String safeText(String text) {
        return text == null ? "" : text;
    }

    /**
     * Standalone test. This does not insert or modify any claim.
     */
    public static void main(String[] args) {

        OwnershipVerificationService service =
                new OwnershipVerificationService();

        VerificationResult result = service.verifyOwnership(
                1,
                "black wallet with blue sticker"
        );

        System.out.println(
                "Similarity Score: "
                        + String.format("%.2f", result.getScore())
                        + "%"
        );

        System.out.println(
                "Confidence: " + result.getConfidence()
        );

        System.out.println(
                "Recommendation: " + result.getRecommendation()
        );
    }
}