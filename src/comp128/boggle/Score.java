package comp128.boggle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Rectangle;

/*
 * Class that extends Graphics Group to create a score screen that display's the users words guessed and the score
 * Authors: Miri Leonard, May Kinnamon, Lucy Manalang 12/13/23
 */
public class Score extends GraphicsGroup{
    private Map<String, Integer> wordList;
    private String wordText1;
    private String wordText2;
    private String wordText3;
    private Integer totalScore;

    private Rectangle background;
    private GraphicsText wordDisplay1;
    private GraphicsText wordDisplay2;
    private GraphicsText wordDisplay3;
    private GraphicsText totalScoreDisplay;


    /*
     * Initializes vairables
     */
    public Score() {
        super();
        wordList = new HashMap<>();
        wordText1 = "";
        wordText2 = "";
        wordText3 = "";
        totalScore = 0;

        background = new Rectangle(0, 0, 0, 0);
        wordDisplay1 = new GraphicsText("");
        wordDisplay2 = new GraphicsText("");
        wordDisplay3 = new GraphicsText("");
        totalScoreDisplay = new GraphicsText("Total: ");

    }
    
    /*
     * takes in window width and height and draws all graphics objects and places 
     * score next to each word found by user as well as the total score
     */
    public GraphicsGroup drawScorePage(double width, double height) {
        background.setSize(width * 1/2, height * 3/4);
        background.setFillColor(Color.LIGHT_GRAY);
        background.setCenter(width * 2/5, height * 11/20);
        this.add(background);

        wordDisplay1.setFontSize(width * 1/45);
        wordDisplay1.setPosition(width * 6/32, height * 1/4);
        wordDisplay1.setFillColor(Color.BLACK);

        wordDisplay2.setFontSize(width * 1/45);
        wordDisplay2.setCenter(width * 11/32, height * 1/4);
        wordDisplay2.setFillColor(Color.BLACK);

        wordDisplay3.setFontSize(width * 1/45);
        wordDisplay3.setCenter(width * 16/32, height * 1/4);
        wordDisplay3.setFillColor(Color.BLACK);

        totalScoreDisplay.setFontSize(width * 1/30);
        totalScoreDisplay.setCenter(width * 1/4, height * 17/20);
        totalScoreDisplay.setFillColor(Color.BLACK);

        ArrayList<String> wordLines = new ArrayList<>();

        // Adds keys from the graph wordList to an array, also formatting by adding the score
        for (String word : wordList.keySet()) { 
            wordLines.add(wordList.get(word) + "\t \t" + word + "\n"); 
            totalScore += wordList.get(word);
        }

        Collections.sort(wordLines, new LengthAlphabetComparator());

        // formats the words on the score page into two columns.
        if (wordLines.size() > 32) {
            for (int i = 0; i <16; i++) {
                wordText1 += wordLines.get(i);
            }
            for (int i = 16; i < 32; i++) {
                wordText2 += wordLines.get(i);
            }
            for (int i = 32; i < wordLines.size(); i++) {
                wordText3 += wordLines.get(i);
            }
        } else if (wordLines.size() > 16) {
            for (int i = 0; i <16; i++) {
                wordText1 += wordLines.get(i);
            }
            for (int i = 16; i < wordLines.size(); i++) {
                wordText2 += wordLines.get(i);
            }
        } else {
            for (int i = 0; i < wordLines.size(); i++) {
                wordText1 += wordLines.get(i);
            }
        }

        wordDisplay1.setText(wordText1 );
        wordDisplay2.setText(wordText2);
        wordDisplay3.setText(wordText3);
        this.add(wordDisplay1);
        this.add(wordDisplay2);
        this.add(wordDisplay3);

        totalScoreDisplay.setText("Total: " + totalScore.toString());
        this.add(totalScoreDisplay);

        return this;
    }

    /*
     * takes in a word and adds word and respective score to the wordList map
     */
    public void addWord(String word) {
        if (!contains(word)) {
            wordList.put(word, getWordScore(word));
        }
    }

    /*
     * takes in a String words and checks the wordlist map for the word
     */
    public boolean contains(String word) {
        return wordList.containsKey(word);
    }

    /*
     * takes in a String word and returns an integer score as intended by the official Boggle rules
     */
    public int getWordScore(String word) {
        int length = word.length();
        if (length <= 4) {
            return 1;
        } else if (length <= 5) {
            return 2;
        } else if (length <= 6) {
            return 3;
        } else if (length <= 7) {
            return 4;
        } else {
            return 11;
        }
    }

}
