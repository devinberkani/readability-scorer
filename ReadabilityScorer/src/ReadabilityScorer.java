import java.util.Scanner;

public class ReadabilityScorer {
    private final static Scanner scanner = new Scanner(System.in);

    public ReadabilityScorer() {
        System.out.println(easyOrHard());
    }

    private String easyOrHard() {

        // splits sentences by a punctuation mark followed by a space
        String[] sentences = scanner.nextLine().split("[.?!]\\s");

        double sentenceCount = 0;
        double wordCount = 0;
        for (String sentence : sentences) {
            sentenceCount++;
            String[] currentSentence = sentence.split("\\s");
            for (String currentWord : currentSentence) {
                wordCount++;
            }
        }

        double wordsInSentenceAverage = wordCount / sentenceCount;

        return wordsInSentenceAverage <= 10 ? "EASY" : "HARD";
    }
}
