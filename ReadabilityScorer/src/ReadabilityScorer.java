import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadabilityScorer {
    private final static Scanner scanner = new Scanner(System.in);
    private String fileData;
    private double wordCount;
    private double sentenceCount;
    private double characterCount;
    private double score;

    public ReadabilityScorer(File file) {
        fileToString(file);
        readFile();
        printData();
    }

    private void printData() {
        // get counts
        countWords();
        countSentences();
        countCharacters();
        calculateScore();

        System.out.print("Words: ");
        System.out.println((int) getWordCount());

        System.out.print("Sentences: ");
        System.out.println((int) getSentenceCount());

        System.out.print("Characters: ");
        System.out.println((int) getCharacterCount());

        System.out.printf("The score is: %.2f", getScore());
        System.out.println();

        System.out.println("This text should be understood by " + getAgeRange() + " year-olds.");
    }

    private String getAgeRange() {

        int ageRangeOne = (int)Math.ceil(getScore()) + 4;
        int ageRangeTwo = (int)Math.ceil(getScore()) + 5;

        return ageRangeOne + "-" + ageRangeTwo;

    }

    // turn the file into a string and set fileData variable to its value
    private void fileToString(File file) {
        StringBuilder fileData = new StringBuilder();

        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String currentLine = reader.nextLine();
                fileData.append(currentLine);
            }
            reader.close();
        }
        catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File does not exist.");
        }

        setFileData(String.valueOf(fileData));
    }

    // print the text from the file
    private void readFile() {

        System.out.println("The text is:");
        System.out.println(getFileData());

    }

    // count words in the file
    private void countWords() {

        // splits sentences by a punctuation mark followed by a space
        String[] words = getFileData().split("\\s+");
        setWordCount(words.length);
    }

    // count sentences in the file
    private void countSentences() {

        // splits sentences by a punctuation mark followed by a space
        String[] sentences = getFileData().split("[.?!]\\s+");
        setSentenceCount(sentences.length);

    }

    // count characters in the file
    private void countCharacters() {

        // splits sentences by a punctuation mark followed by a space
        String[] characters = getFileData().split("");

        double characterCount = 0;
        for (String character : characters) {
            if (!character.matches("\\s")) {
                characterCount++;
            }
        }

        setCharacterCount(characterCount);

    }

    private void calculateScore() {
        setScore(4.71 * getCharacterCount() / getWordCount() + 0.5 * getWordCount() / getSentenceCount() - 21.43);
    }

    // getters and setters


    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    public double getWordCount() {
        return wordCount;
    }

    public void setWordCount(double wordCount) {
        this.wordCount = wordCount;
    }

    public double getSentenceCount() {
        return sentenceCount;
    }

    public void setSentenceCount(double sentenceCount) {
        this.sentenceCount = sentenceCount;
    }

    public double getCharacterCount() {
        return characterCount;
    }

    public void setCharacterCount(double characterCount) {
        this.characterCount = characterCount;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
