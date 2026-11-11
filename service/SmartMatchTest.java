package service;

import java.util.List;

public class SmartMatchTest {

    public static void main(String[] args) {

        SmartMatchService service =
                new SmartMatchService();

        List<String> matches =
                service.findMatches(
                        "Blue Water Bottle",
                        "Library"
                );

        if (matches.isEmpty()) {

            System.out.println(
                    "No matching items found."
            );

        } else {

            System.out.println(
                    "Possible Matches:"
            );

            for (String match : matches) {

                System.out.println(
                        "--------------------"
                );

                System.out.println(match);
            }
        }
    }
}