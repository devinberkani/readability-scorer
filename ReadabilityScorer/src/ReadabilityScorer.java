import java.util.Scanner;

public class ReadabilityScorer {
    private final static Scanner scanner = new Scanner(System.in);

    public ReadabilityScorer() {
        System.out.println(easyOrHard());
    }

    private String easyOrHard() {
        int inputLength = scanner.nextLine().split("").length;

        return inputLength <= 100 ? "EASY" : "HARD";
    }
}
