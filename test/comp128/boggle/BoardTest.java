package comp128.boggle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;

public class BoardTest {
    private Dice[][] diceBoard;
    private Board boardObject;

    public BoardTest() {
        new Dictionary();
        boardObject = new Board();
        boardObject.fillBoard(0);
        boardObject.randomizeBoard(0);
        diceBoard = boardObject.getDiceBoard();
        for (int i = 0; i < diceBoard.length; i++) {
            for (int j = 0; j < diceBoard[0].length; j++) {
                diceBoard[i][j].setLocation(i, j);
            }
        }
    }
    
    @Test
    public void testAdjacency() {
        ArrayList<Dice> adjacencyList;
        Dice currentDie = diceBoard[1][1];
        assertEquals(currentDie.toString(), "I");
        boardObject.findAdjacent(currentDie);
        adjacencyList = currentDie.getAdjacent();
        Dice[] testArray = {
            diceBoard[0][0],
            diceBoard[0][1],
            diceBoard[0][2],
            diceBoard[1][0],
            diceBoard[1][2],
            diceBoard[2][0],
            diceBoard[2][1],
            diceBoard[2][2]
        };
        assertArrayEquals(testArray, adjacencyList.toArray());
        currentDie = diceBoard[0][0];
        boardObject.findAdjacent(currentDie);
        adjacencyList = currentDie.getAdjacent();
        assertEquals(3, adjacencyList.size());
    }

    @Test
    public void testSolve() {
        HashSet<String> possibleWords;
        for (int i = 0; i < diceBoard.length; i++) {
            for (int j = 0; j < diceBoard[0].length; j++) {
                boardObject.findAdjacent(diceBoard[i][j]);
            }
        }
        boardObject.solve();
        possibleWords = boardObject.getSolvedBoard();
        assertEquals(77, possibleWords.size());
    }
}
