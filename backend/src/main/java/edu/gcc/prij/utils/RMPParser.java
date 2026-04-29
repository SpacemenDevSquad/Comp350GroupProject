package edu.gcc.prij.utils;

import edu.gcc.prij.objects.professor.Professor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class RMPParser {
    private String path;
    private Repository<Professor, String> professorRepository;

    public RMPParser(String path, Repository<Professor, String> professorRepository) {
        this.path = path;
        this.professorRepository = professorRepository;
    }

    public void parse() {
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            // 1. Parse the JSON file directly into a JsonNode tree
            InputStream inputStream = RMPParser.class.getResourceAsStream(path);

            JsonNode rootNode = mapper.readTree(inputStream);

            // 2. Ensure the root is an array before iterating
            if (rootNode.isArray()) {
                for (JsonNode rmpNode : rootNode) {
                    String rmpName = rmpNode.get("name").asText();
                    Professor bestMatch = findBestMatch(rmpName);

                    if (bestMatch != null) {
                        // Bulletproof JsonNode traversal
                        bestMatch.setRmpId(rmpNode.path("id").asInt(0));
                        bestMatch.setRmpQualityRating((float) rmpNode.path("quality_rating").asDouble(0.0));
                        bestMatch.setRmpRatingCount(rmpNode.path("rating_count").asInt(0));

                        // Handle potentially missing percentages gracefully
                        double takeAgainPercent = rmpNode.path("would_take_again").asDouble(-1.0);
                        bestMatch.setRmpPercentWouldTakeAgain(takeAgainPercent);

                        bestMatch.setRmpDifficulty((float) rmpNode.path("difficulty").asDouble(0.0));
                        bestMatch.setRmpDepartment(rmpNode.path("department").asText("Unknown"));

                        // 4. Save the updated entity back to the repository
                        professorRepository.update(bestMatch.getKey(), bestMatch);
                        System.out.println("Successfully merged RMP data for: " + bestMatch.getName());
                        System.out.println(bestMatch);
                    } else {
                        System.out.println("Could not find a repository match for scraped name: " + rmpName);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Failed to read or parse the RMP JSON file at: " + path);
            e.printStackTrace();
        }
    }

    /**
     * Sophisticated matching algorithm to link "First Last" to "Last, First M."
     */
    private Professor findBestMatch(String rmpName) {
        String[] rmpParts = rmpName.trim().split("\\s+");
        if (rmpParts.length < 2) return null; // Safety check

        String rmpFirst = rmpParts[0].toLowerCase();
        String rmpLast = rmpParts[rmpParts.length - 1].toLowerCase();

        Professor bestMatch = null;
        int lowestDistance = Integer.MAX_VALUE;

        for (Professor repoProf : professorRepository.findAll()) {
            String repoName = repoProf.getName();
            
            // Expected repo format: "Last, First M."
            String[] repoParts = repoName.split(",\\s*");
            if (repoParts.length >= 2) {
                String repoLast = repoParts[0].trim().toLowerCase();
                // Get the first name, ignoring middle initials
                String repoFirst = repoParts[1].trim().split("\\s+")[0].toLowerCase(); 

                // 1. Check for exact Last Name match (High priority)
                if (repoLast.equals(rmpLast)) {
                    // 2. Check for exact First Name match
                    if (repoFirst.equals(rmpFirst)) {
                        return repoProf; // Perfect logical match, return immediately
                    }
                    
                    // 3. If first names differ, use Levenshtein distance 
                    int distance = calculateLevenshteinDistance(rmpFirst, repoFirst);
                    if (distance < lowestDistance && distance <= 3) { // Threshold of 3
                        lowestDistance = distance;
                        bestMatch = repoProf;
                    }
                }
            }
        }
        return bestMatch;
    }

    /**
     * Calculates the Levenshtein distance between two strings to account for typos or slight variations.
     */
    private int calculateLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1] 
                             + costOfSubstitution(s1.charAt(i - 1), s2.charAt(j - 1)), 
                             Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }

    private int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }
}