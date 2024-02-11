package comp128.boggle;

import java.util.HashSet;

/**
 * Handles the dictionary the program uses to determine words
 * Authors: Miri Leonard, May Kinnamon, Lucy Manalang 12/13/23
 */
public class Dictionary {
    private static HashSet<String> wordSet;
    
    public Dictionary() {
        wordSet = FileReader.readFile("/Dictionary.txt");
    }

    /**
     * Checks if the word is contained within the dictionary
     * Dictiornary text file taken from HW3.
     * @param word
     * @return
     */
    public static boolean containsWord(String word) {
        if (wordSet.contains(word)) {
            return true;
        }   
        return false;
    }

    public static HashSet<String> getDictionary(){
        return wordSet;
    }
}
