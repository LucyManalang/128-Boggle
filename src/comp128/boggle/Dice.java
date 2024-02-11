package comp128.boggle;

import java.util.Random;
import java.util.ArrayList;

import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.FontStyle;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;


/** 
 * The Dice class is used to represent the individual squares on the board. On a 4x4 board, there would be 
 * 16 dice objects initialized. Although "dice" is the plural version of the word, having a class called "Die"
 * could potentially be surprising for people reviewing the code.
 * Authors: Miri Leonard, May Kinnamon, Lucy Manalang 12/13/23
 */
public class Dice extends GraphicsGroup{
    private ArrayList<Dice> adjacent;
    private String currentFace;
    private String[] faces;
    private int xLocation;
    private int yLocation;
    private boolean selected;

    private Ellipse dieHitBox;
    private GraphicsText label;
    private Image dieOutline;
    
    /**
     * Creates a die which has a set of faces, a currently displayed face, and is a graphics group 
     * that can be added to a canvas.
     */
    public Dice() {
        super();
        faces = new String[6];
        selected = false;
        adjacent = new ArrayList<>(8);

        dieHitBox = new Ellipse(0, 0, 0, 0);
        dieOutline = new Image("BoggleDice.png");
        label = new GraphicsText();
    }


    /**
     * Draws a rectangle and a Text Field to represent a boggle die. Each die also has a hit box to test for hits.
     * @param point The point to draw the CENTER of the die at.
     * @param size The scaled size of the die.
     */
    public GraphicsGroup drawDie(Point point, int size) {     
        dieOutline.setScale(size/2900.0);
        dieOutline.setCenter(point);
        this.add(dieOutline);

        dieHitBox.setSize(size, size);
        dieHitBox.setStroked(false);
        dieHitBox.setCenter(dieOutline.getCenter());
        this.add(dieHitBox);

        label.setText(currentFace);
        label.setFont(FontStyle.BOLD, 50);
        label.setCenter(dieOutline.getCenter());
        this.add(label);

        return this;
    }


    /**
     * Sets all 6 faces of a die to a set configuration in DiceConfig file
     */
    public void setFaces(String faceValues) {
        faces = faceValues.split(" ");
    } 


    /**
     * Sets the face of this die randomly to one of it's six letters
     */
    public void setRandomFace(long seed) {
        Random rand = new Random();
        rand.setSeed(seed);
        currentFace = faces[rand.nextInt(5)];
        label.setText(currentFace);
    }


    public String toString() {
        return currentFace;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public boolean isSelected() {
        return selected;
    }

    public void addAdjacent(Dice die) {
        adjacent.add(die);
    }

    public void clearAdjacent() {
        adjacent = new ArrayList<>();
    }

    public ArrayList<Dice> getAdjacent() {
        return adjacent;
    }

    /**
     * Sets the location of where on the board the dice are as an attribute
     * @param y
     * @param x
     */
    public void setLocation(int y, int x) {
        xLocation = x;
        yLocation = y;
    }

    /**
     * @return x Location
     */
    public int getXLoc() {
        return xLocation;
    }

    /**
     * @return y Location
     */
    public int getYLoc() {
        return yLocation;
    }

    public Ellipse getDieHitBox() {
        return dieHitBox;
    }
}
