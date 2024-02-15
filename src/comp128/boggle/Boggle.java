package comp128.boggle;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.awt.Color;
import java.util.Deque;
import java.util.Random;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.ui.Button;
import edu.macalester.graphics.ui.TextField;

/**
 * Welcome to Boggle! Boggle is a classic word game where players shake a board with dice 
 * that roll and land on random letters. The players then have a set amount of time to write down 
 * all the words that they find on the board by connecting letters on the adjacent dice (diagonally counts also) 
 * while never using the same dice twice per word.
 * Authors: Miri Leonard, May Kinnamon, Lucy Manalang 12/13/23
 */
public class Boggle {
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 750;
    private static final int DICE_SIZE = 100;

    private Board mainBoard;
    private Dice[][] diceArray;
    private Deque<Point> mousePointPath; 
    private Deque<Dice> selectedDice; // Saves the currently selected dice.
    private Score score;
    private Timer timer;
    private ArrayList<String> wordsArray;

    // Canvas and graphics for the game.
    private CanvasWindow canvas;
    private GraphicsGroup diceGroup;
    private GraphicsGroup dieHitBoxGraphicsGroup;
    private GraphicsGroup uiGroup;
    private GraphicsText wordInput;
    private TextField setSeed;
    private Button startButton;
    private Boolean gameInPlay;
    private Rectangle wordsBackground;
    private GraphicsText wordsList1;
    private GraphicsText wordsList2;



    /**
     * Creates a new game of boggle.
     */
    public Boggle() {
        setUpGameWindow();

        startButton.onClick(() -> {
            onStartButtonClickListener();
        });
        
        canvas.onMouseDown(mouse -> {
            Point position = mouse.getPosition();
            if (gameInPlay) {
                onMouseDownListener(position);
            }
        });

        canvas.onDrag(mouse -> {
            Point position = mouse.getPosition();
            if (gameInPlay) {
                onDragListener(position);
            }
        });

        canvas.onMouseUp(mouse -> {
            if (gameInPlay){
                onMouseUpListener();
            }
        });
    }

    /**
     * Initializes instance variables. This method exists for code organization.
     */
    public void initializeVariables() {
        mainBoard = new Board();
        diceArray = mainBoard.getDiceBoard();
        mousePointPath = new ArrayDeque<>();
        selectedDice = new ArrayDeque<>();
        score = new Score();
        wordsArray = new ArrayList<>();

        canvas = new CanvasWindow("Boggle", WINDOW_WIDTH, WINDOW_HEIGHT);
        diceGroup = new GraphicsGroup();
        dieHitBoxGraphicsGroup = new GraphicsGroup();
        uiGroup = new GraphicsGroup();
        gameInPlay = false; 
        
        timer = new Timer(this);
    }
 

    /**
     * Creates and draws the intial UI objects on the canvas window, and adds them to their 
     * respective UI graphics groups.
     */
    public void setUpGameWindow() {
        initializeVariables();
        
        Image rulesImage = new Image("Rules.png"); // Shows the game rules before the start of the game.
        rulesImage.setScale(1.0/2);
        rulesImage.setCenter(WINDOW_WIDTH * 2/5, WINDOW_HEIGHT * 23/40);
        canvas.add(rulesImage);
        
        Image title = new Image("BoggleTitleStretch.png");
        title.setScale(WINDOW_HEIGHT/10000.0);
        title.setCenter(WINDOW_WIDTH * 2/5, (WINDOW_HEIGHT * 1/10));
        canvas.add(title);
        uiGroup.add(title);

        wordInput = new GraphicsText("Word: "); // Shows the player's currently selected letters/word
        wordInput.setFontSize(WINDOW_WIDTH * 1/25);
        wordInput.setCenter(WINDOW_WIDTH * 9/32, WINDOW_HEIGHT * 13/16);
        uiGroup.add(wordInput);

        setSeed = new TextField();
        setSeed.setCenter(WINDOW_WIDTH * 1/12, WINDOW_HEIGHT * 7/40);
        setSeed.setBackground(Color.GRAY);
        setSeed.setText("Enter seed");
        canvas.add(setSeed);

        startButton = new Button("Start");
        startButton.setCenter(WINDOW_WIDTH * 1/12, WINDOW_HEIGHT * 9/40);
        canvas.add(startButton);

        wordsBackground = new Rectangle(0, 0, WINDOW_WIDTH * 1/4, WINDOW_HEIGHT * 3/4);
        wordsBackground.setCenter(WINDOW_WIDTH * 4/5, WINDOW_HEIGHT * 1/2);
        wordsBackground.setFillColor(Color.LIGHT_GRAY);
        canvas.add(wordsBackground);

        wordsList1 = new GraphicsText(""); //23 max
        wordsList1.setFontSize(WINDOW_WIDTH * 1/55);
        wordsList1.setPosition(WINDOW_WIDTH * 23/32, WINDOW_HEIGHT * 3/16);
        wordsList1.setFillColor(Color.BLACK);
        canvas.add(wordsList1);

        wordsList2 = new GraphicsText(""); //23 max
        wordsList2.setFontSize(WINDOW_WIDTH * 1/55);
        wordsList2.setPosition(WINDOW_WIDTH * 27/32, WINDOW_HEIGHT * 3/16);
        wordsList2.setFillColor(Color.BLACK);
        canvas.add(wordsList2);


        uiGroup.add(timer.getGraphics());

        diceGroup.setCenter(WINDOW_WIDTH * 2/5, WINDOW_HEIGHT * 2/5);
        canvas.draw();
    }


    /**
     * Resets canvas and all temporary collections, while saving permanent UI objects like the timer. Idea taken from HW1
     */
    private void removeAllNonUIGraphicsObjects() {
        mousePointPath.clear();
        selectedDice.clear();
        diceGroup.removeAll();
        canvas.removeAll();
        canvas.add(uiGroup);
        drawBoggleBoard();
    }


    /**
     * Draws the boggle board on the canvas, randomizing each dice.
     * @return The boggle board graphics group.
     */
    public GraphicsGroup drawBoggleBoard() {
        for (int i = 0; i < diceArray.length; i++) {
            for (int j = 0; j < diceArray[i].length; j++) {
                diceGroup.add(diceArray[i][j].drawDie(new Point(i*DICE_SIZE, j*DICE_SIZE), DICE_SIZE));
                dieHitBoxGraphicsGroup.add(diceArray[i][j].getDieHitBox());
                diceArray[i][j].setLocation(i, j);
                mainBoard.findAdjacent(diceArray[i][j]);
            }
        }
        diceGroup.setCenter(WINDOW_WIDTH * 2/5, WINDOW_HEIGHT * 1/2);
        canvas.add(diceGroup);
        canvas.add(wordsBackground);
        canvas.add(wordsList1);
        canvas.add(wordsList2);
        return diceGroup;
    }

    /**
     * Adds graphics (like the score page) to the canvas after the game ends.
     * Do NOT call canvas.draw() in this method!
     */
    public void drawEndGameGraphics() {
        canvas.add(score.drawScorePage(WINDOW_WIDTH, WINDOW_HEIGHT));

        GraphicsText maxScore = new GraphicsText("Max possible score:\n" + mainBoard.getMaxScore(), WINDOW_WIDTH, WINDOW_HEIGHT);
        maxScore.setFontSize(WINDOW_WIDTH * 1/40);
        maxScore.setCenter(WINDOW_WIDTH * 4/5, WINDOW_HEIGHT * 19/20);
        maxScore.setFillColor(Color.BLACK);
        canvas.add(maxScore);
        System.out.println("All possible words:" + mainBoard.getSolvedBoard().toString());
    }


    /**
     * Adds dice to the dice path and draws the path line as the mouse is dragged on the canvas.
     * @param position Mouse position.
     */
    private void onDragListener(Point position) {
        Dice lastDice = selectedDice.peekLast();
        Line drawnLine = new Line(mousePointPath.peekLast(), position);
        drawnLine.setStrokeColor(Color.ORANGE);
        drawnLine.setStrokeWidth(2);
        double xPos = position.getX();
        double yPos = position.getY();

        canvas.add(drawnLine);
        mousePointPath.addLast(position);

        if (selectedDice.isEmpty()) { // If it's the first die selected, just add it to the dice path
            for (int i = 0; i < diceArray.length; i++) {
                for (int j = 0; j < diceArray[0].length; j++) {
                    Dice currentDie = diceArray[i][j];
                    if (currentDie.isSelected()) {
                        return;
                    }
                    if (currentDie.getDieHitBox().testHit(xPos - diceGroup.getX(), yPos - diceGroup.getY())) {
                        selectedDice.addLast(currentDie);
                        currentDie.setSelected(true);
                        wordInput.setText(wordInput.getText() + selectedDice.peekLast());
                        return;
                    }
                }
            }
            return;
        } 
        for (Dice currentDie : lastDice.getAdjacent()) { // Prevents players going around the board or other dice
            if (currentDie.isSelected()) {
                continue;
            }
            if (currentDie.getDieHitBox().testHit(xPos - diceGroup.getX(), yPos - diceGroup.getY())) {
                selectedDice.addLast(currentDie);
                currentDie.setSelected(true);
                lastDice = currentDie;
                wordInput.setText(wordInput.getText() + selectedDice.peekLast());
                break;
            }
        }
    }

    /**
     * Checks if the word made by the player (and saved in the dice path) is a valid word in the dictionary.
     * If it is, saves it to the score.
     * Turns the word input red if it is not a valid word, orange if the word has already been used, and green 
     * if it's a new and valid word.
     */
    private void onMouseUpListener() {
        String word = "";

        while (!selectedDice.isEmpty()) {
            Dice tempDice = selectedDice.pollFirst();
            tempDice.setSelected(false);
            word += tempDice.toString();
        }

        if (score.contains(word)) {
            wordInput.setFillColor(Color.ORANGE);
        } else if (Dictionary.containsWord(word) && word.length() >= 3) {
            wordInput.setFillColor(Color.GREEN);
            score.addWord(word);
            wordsArray.add(word);
            formatWords(word);
        } else {
            wordInput.setFillColor(Color.RED);
        }
    }


    /**
     * Clears temp UI objects like the drawn line, and resets the wordInput text and color.
     * @param position Mouse position.
     */
    private void onMouseDownListener(Point position) {
        removeAllNonUIGraphicsObjects();
        wordInput.setText("Word: ");
        wordInput.setFillColor(Color.BLACK);
        mousePointPath.add(position);
    }

    /**
     * Try's randomizing the board with the input seed, otherwise randomizes the board with a new seed.
     * Starts the timer and game.
     */
    private void onStartButtonClickListener() {
        Long seed;

        canvas.remove(startButton);
        gameInPlay = true;
        timer.startTimer();

        try {
            seed = Long.parseLong(setSeed.getText());
            mainBoard.randomizeBoard(seed);
        } catch (Exception E) {
            Random random = new Random();
            seed = Long.valueOf(random.nextInt(100000));
            mainBoard.randomizeBoard(seed);
            setSeed.setText(seed.toString());
        }
        
        uiGroup.add(setSeed);
        removeAllNonUIGraphicsObjects();
        mainBoard.solve();
    }

    /**
     * Formats the inputted words to fit on the score page when the game is completed
     * @param word
     */
    private void formatWords(String word) {
        if (wordsArray.indexOf(word) < 22) {
            if (wordsList1.getText() == "") {
                wordsList1.setText(word + "\n");
            } else {
                wordsList1.setText(wordsList1.getText() + word + "\n");
            }
        } else {
            if (wordsList2.getText() == "") {
                wordsList2.setText(word + "\n");
            } else {
                wordsList2.setText(wordsList2.getText() + word + "\n");
            }
        }
    }

    /**
     * Set to false when the timer finishes, and the game is not longer in play.
     * @param isInPlay
     */
    public void setGameInPlay(boolean isInPlay) {
        gameInPlay = isInPlay;
    }

    public Score getScore() {
        return score;
    }

    public static int getWidth() {
        return WINDOW_WIDTH;
    }

    public static int getHeight() {
        return WINDOW_HEIGHT;
    }

    public CanvasWindow getCanvas() {
        return canvas;
    }
 
    public static void main(String[] args) {
        new Dictionary();
        new Boggle();
    }
}