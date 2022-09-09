import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadabilityScorer {
    private final static Scanner scanner = new Scanner(System.in);
    private String fileData;
    private double wordCount;
    private double sentenceCount;
    private double characterCount;
    private double syllableCount;
    private double polySyllableCount;
    private final ArrayList<String> SCORE_TYPES = new ArrayList<>();
    {
        SCORE_TYPES.add("ARI");
        SCORE_TYPES.add("FK");
        SCORE_TYPES.add("SMOG");
        SCORE_TYPES.add("CL");
        SCORE_TYPES.add("all");
    }
    private double ariScore;
    private double fkScore;
    private double smogScore;
    private double clScore;
    private double ariAge;
    private double fkAge;
    private double smogAge;
    private double clAge;

    public ReadabilityScorer(File file) {
        fileToString(file);
        readFile();
        printData();
        getScoreTypeFromUser();
    }

    private void printData() {
        // get counts and calculations
        countWords();
        countSentences();
        countCharacters();
        countSyllables();
        calculateAriScore();
        calculateFkScore();
        calculateSmogScore();
        calculateClScore();

        System.out.print("Words: ");
        System.out.println((int) getWordCount());

        System.out.print("Sentences: ");
        System.out.println((int) getSentenceCount());

        System.out.print("Characters: ");
        System.out.println((int) getCharacterCount());

        System.out.print("Syllables: ");
        System.out.println((int) getSyllableCount());

        System.out.print("Polysyllables: ");
        System.out.println((int) getPolySyllableCount());
    }

    private void getScoreTypeFromUser() {
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String scoreType = scanner.nextLine();

        if (SCORE_TYPES.contains(scoreType)) {
            printScore(scoreType);
        }
    }

    private void printScore(String scoreType) {
        System.out.println();

        switch(scoreType) {
            case "ARI":
                printAri();
                break;

            case "FK":
                printFk();
                break;

            case "SMOG":
                printSmog();
                break;

            case "CL":
                printCl();
                break;

            case "all":
                printAri();
                printFk();
                printSmog();
                printCl();
                printAverageAge();
                break;
        }
    }

    private void printAri() {

        setAriAge(getAge(getAriScore()));

        System.out.printf("Automated Readability Index: %.2f", getAriScore());
        System.out.println(" (about " + (int)getAriAge() + "-year-olds)");
    }

    private void printFk() {

        setFkAge(getAge(getFkScore()));

        System.out.printf("Flesch–Kincaid readability tests: %.2f", getFkScore());
        System.out.println(" (about " + (int)getFkAge() + "-year-olds)");
    }

    private void printSmog() {

        setSmogAge(getAge(getSmogScore()));

        System.out.printf("Simple Measure of Gobbledygook: %.2f", getSmogScore());
        System.out.println(" (about " + (int)getSmogAge() + "-year-olds)");
    }

    private void printCl() {

        setClAge(getAge(getClScore()));

        System.out.printf("Coleman–Liau index: %.2f", getClScore());
        System.out.println(" (about " + (int)getClAge() + "-year-olds)");
    }

    private void printAverageAge() {

        double averageAge = (getAriAge() + getFkAge() + getSmogAge() + getClAge()) / 4;

        System.out.println();
        System.out.printf("This text should be understood in average by %.2f-year-olds.", averageAge);
    }

    private int getAge(double score) {

        int age;

        if (Math.ceil(score) < 14) {
            age = (int)Math.ceil(score) + 5;
        } else {
            age = (int)Math.ceil(score) + 8;
        }

        return age;
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
        System.out.println();

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

        String[] characters = getFileData().split("");

        double characterCount = 0;
        for (String character : characters) {
            if (!character.matches("\\s")) {
                characterCount++;
            }
        }

        setCharacterCount(characterCount);

    }

    private void countSyllables() {

        // separate words whether there is punctuation or not
        String[] words = getFileData().split("[!-/]*\\s+[!-/]*");

        int totalSyllableCount = 0;
        int polySyllableCount = 0;

        for (String word : words) {
            int currentWordSyllableCount = Math.max(1, word.toLowerCase()
                    //in words that end with "e" replace
                    //"e" with ""
                    .replaceAll("e$", "") //e.g base=bas
                    //when two vowels appear together,
                    //replace them with "a"
                    .replaceAll("[aeiouy]{2}", "a") //e.g you == au,
                    //beautiful==bautiful
                    //again, when two vowels appear together,
                    //replace them with "a"
                    .replaceAll("[aeiouy]{2}", "a") //e.g au == a,
                    //bautiful==batiful
                    //replace any character that isn't aeiouy with ""
                    .replaceAll("[^aeiouy]", "") //e.g, batiful==aiu,
                    //a == a
                    .length() //aiu == 3 syllables, a == 1 syllable
            );
//            System.out.println(word + ": " + currentWordSyllableCount);
            if (currentWordSyllableCount > 2) {
                polySyllableCount++;
            }
            totalSyllableCount += currentWordSyllableCount;
        }

        setSyllableCount(totalSyllableCount);
        setPolySyllableCount(polySyllableCount);

    }

    private void calculateAriScore() {
        setAriScore(4.71 * getCharacterCount() / getWordCount() + 0.5 * getWordCount() / getSentenceCount() - 21.43);
    }

    private void calculateFkScore() {
        setFkScore(0.39 * getWordCount() / getSentenceCount() + 11.8 * getSyllableCount() / getWordCount() - 15.59);
    }

    private void calculateSmogScore() {
        setSmogScore(1.043 * (Math.sqrt(getPolySyllableCount() * 30 / getSentenceCount())) + 3.1291);
    }

    private void calculateClScore() {
        double s = getSentenceCount() / getWordCount() * 100;
        double l = getCharacterCount() / getWordCount() * 100;

        setClScore(0.0588 * l - 0.296 * s - 15.8);
    }

    // getters and setters


    private String getFileData() {
        return fileData;
    }

    private void setFileData(String fileData) {
        this.fileData = fileData;
    }

    private double getWordCount() {
        return wordCount;
    }

    private void setWordCount(double wordCount) {
        this.wordCount = wordCount;
    }

    private double getSentenceCount() {
        return sentenceCount;
    }

    private void setSentenceCount(double sentenceCount) {
        this.sentenceCount = sentenceCount;
    }

    private double getCharacterCount() {
        return characterCount;
    }

    private void setCharacterCount(double characterCount) {
        this.characterCount = characterCount;
    }

    private double getSyllableCount() {
        return syllableCount;
    }

    private void setSyllableCount(double syllableCount) {
        this.syllableCount = syllableCount;
    }

    private double getPolySyllableCount() {
        return polySyllableCount;
    }

    private void setPolySyllableCount(double polySyllableCount) {
        this.polySyllableCount = polySyllableCount;
    }

    private double getAriScore() {
        return ariScore;
    }

    private void setAriScore(double ariScore) {
        this.ariScore = ariScore;
    }

    public double getFkScore() {
        return fkScore;
    }

    public void setFkScore(double fkScore) {
        this.fkScore = fkScore;
    }

    public double getSmogScore() {
        return smogScore;
    }

    public void setSmogScore(double smogScore) {
        this.smogScore = smogScore;
    }

    public double getClScore() {
        return clScore;
    }

    public void setClScore(double clScore) {
        this.clScore = clScore;
    }

    public double getAriAge() {
        return ariAge;
    }

    public void setAriAge(double ariAge) {
        this.ariAge = ariAge;
    }

    public double getFkAge() {
        return fkAge;
    }

    public void setFkAge(double fkAge) {
        this.fkAge = fkAge;
    }

    public double getSmogAge() {
        return smogAge;
    }

    public void setSmogAge(double smogAge) {
        this.smogAge = smogAge;
    }

    public double getClAge() {
        return clAge;
    }

    public void setClAge(double clAge) {
        this.clAge = clAge;
    }
}