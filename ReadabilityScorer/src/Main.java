import java.io.File;

public class Main {
    public static void main(String[] args) {
        String fileName = args[0];
        File file = new File(fileName);
        ReadabilityScorer readabilityScorer = new ReadabilityScorer(file);
    }
}