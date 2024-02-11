package comp128.boggle;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


/** 
* Board class handles the back-end of the game. This class mainly represents and contains methods to handle a 2D array 
* filled with Objects of the Dice class and contains the possible words of each board.
* Authors: Miri Leonard, May Kinnamon, Lucy Manalang 12/13/23
*/
public class Board {
    private final int BOARD_WIDTH = 4;
    private final int BOARD_HEIGHT = 4;
    private final long INITIAL_SEED = 0;

    private Dice[][] board;
    private HashSet<String> possibleWords;
    private DictionaryTree dictionaryTree;
    private Score score;
    private Integer maxScore;


    public Board() { 
        score = new Score();
        maxScore = 0;
        dictionaryTree = new DictionaryTree(Dictionary.getDictionary());
        board = new Dice[BOARD_HEIGHT][BOARD_WIDTH];
        fillBoard(INITIAL_SEED);
    }

    /**
     * Returns 2D array of Dice that represents the board that is played on.
     */
    public Dice[][] getDiceBoard() {
        return board;
    }


    /**
     * Randomizes the board by randomizing the columns, rows, and the face of each die.
     * @param seed The seed used for randomization, allows for repeatability for multiplayer and testing.
     */
    public void randomizeBoard(long seed) { 
        Random random = new Random(seed);
        Dice[] tempArr;
        Dice tempDice;
        int randInt;
        
        for (int i = board.length - 1; i >= 0; i--) {
            randInt = random.nextInt(i + 1);
            tempArr = board[randInt];
            board[randInt] = board[i];
            board[i] = tempArr;
            for (int j = board[i].length - 1; j >= 0; j--) {
                randInt = random.nextInt(i + 1);
                tempDice = board[i][randInt];
                board[i][randInt] = board[i][j];
                board[i][j] = tempDice;
                board[i][j].setRandomFace(seed);
            }        
        }
    }

    /**
     * Fills the board array with separate objects in Dice class. Used only when initializing.
     */
    public void fillBoard(long seed) {
        HashSet<String> stringSet = FileReader.readFile("/DiceConfig.txt");
        Iterator<String> stringSetIterator = stringSet.iterator();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Dice();
                board[i][j].setFaces(stringSetIterator.next());
                board[i][j].setRandomFace(seed);
                board[i][j].setLocation(i, j);
            }
        }
    }


    /**
     * Finds all adjacent dice to inputted die, edits ArrayList adjacent in the inputted die object
     * @param die
     */
    public void findAdjacent(Dice InputDie) {
        int xLocation = InputDie.getXLoc();
        int yLocation = InputDie.getYLoc();

        InputDie.clearAdjacent();
        for (int i = yLocation - 1; i <= yLocation + 1; i++) {
            for (int j = xLocation - 1; j <= xLocation + 1; j++) {
                if (i < 0 || i > 3) {
                    break;
                }
                if (j < 0 || j > 3) {
                    continue;
                }
                if (i == yLocation && j == xLocation) {
                    continue;
                }
                InputDie.addAdjacent(board[i][j]);
            }
        }
    }


    /**
     * Finds all possible words that can be found in a board
     */
    public void solve() {
        TreeNode currentNode;
        Deque<Dice> selectedDice;
        Dice currentDie;

        possibleWords = new HashSet<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                currentDie = board[i][j];
                currentNode = getNextNode(dictionaryTree.getRoot(), currentDie, 0);
                if (currentDie.toString().length() == 2) {
                    currentNode = getNextNode(currentNode, currentDie, 1);
                } 
                selectedDice = new ArrayDeque<>();
                selectedDice.addLast(currentDie);
                solveRecursiveHelper(currentNode, selectedDice);
            }
        }
    }


    /**
     * Helper method for solve() that uses recursion
     * @param currentNode the treenode that the algorithm is currently viewing
     * @param selectedDice the path of the dice that has been traveled
     */
    public void solveRecursiveHelper(TreeNode currentNode, Deque<Dice> selectedDice) {
        Map<Character, TreeNode> children = currentNode.children;
        Dice currentDie = selectedDice.peekLast();
        ArrayList<Dice> adjacent = currentDie.getAdjacent();
        String word = selectedDice.toString();
        String dieFace; 
        Character tempCharacter;
        boolean selectedDiceContains;
        
        word = word.substring(1, word.length() - 1);
        word = word.replace(", ", "");
        if (selectedDice.size() > 2 && dictionaryTree.contains(word)) {
            possibleWords.add(word);
            maxScore += score.getWordScore(word);
        }
        for (Dice die : adjacent) {
            selectedDiceContains = false;
            dieFace = die.toString();
            tempCharacter = dieFace.charAt(0);
            for (Dice dice : selectedDice) {
                if (dice == die) {
                    selectedDiceContains = true;
                    break;
                }
            }
            if (selectedDiceContains) {
                continue;
            }
            
            if (!children.containsKey(tempCharacter)) {
                continue;
            }
            if (dieFace.length() == 2 && children.containsKey(dieFace.charAt(1))) {
                currentNode = children.get(tempCharacter);
                children = currentNode.children;
                word += tempCharacter.toString();
                tempCharacter = dieFace.charAt(1);
            }
            selectedDice.addLast(die);
            solveRecursiveHelper(children.get(tempCharacter), selectedDice);
            selectedDice.removeLast();
        }
    }


    /**
     * Get method for the solved board
     * @return possibleBoard
     */
    public HashSet<String> getSolvedBoard() {
        return possibleWords;
    }

    public Integer getMaxScore() {
        return maxScore;
    }


    /**
     * Helper method for solve() to improve readability
     * @param node the current node
     * @param die the current die
     * @param index the index of the letter in the die the user wants to return, 
     *              this is important considering some dice have multi-letter faces
     * @return the next node
     */
    private TreeNode getNextNode(TreeNode node, Dice die, int index) {
        return node.children.get(die.toString().charAt(index));
    }
}
