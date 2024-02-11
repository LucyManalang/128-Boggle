package comp128.boggle;

import java.awt.Color;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsText;

/**
 * Inspiration taken from Macalester Kilt-Graphics CanvasWindow and AnimationTimer class.
 * Authors: Miri Leonard, May Kinnamon, Lucy Manalang 12/13/23
 */
public class Timer extends GraphicsText implements Runnable {
    private static final long TIMER_LENGTH = 180_000; // The time in milliseconds of a 3 minute timer is 180_000.
    private static final long ONE_SEC = 1000; // The time in millisecond of one second.
    private Thread thread;
    private Boggle boggle;
    private CanvasWindow canvas;
    private long startTime;
    private boolean timeUp;

    /**
     * A timer for a boggle board, which runs in a thread in the background while the game runs.
     * @param boggle
     */
    public Timer(Boggle boggle) {
        super();
        timeUp = false;
        this.boggle = boggle;
        canvas = boggle.getCanvas();
        
        thread = new Thread(this); // Creates a thread with this timer.
        thread.setName("Boggle_Timer");
        thread.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);

        this.setFontSize(canvas.getWidth() / 35);
        this.setCenter(Boggle.getWidth() * 1/25, Boggle.getHeight() * 1/15);
        this.setFillColor(Color.BLACK);
        if ((TIMER_LENGTH / 1000) % 60 < 10) {
            this.setText("Timer: \n" + (TIMER_LENGTH / 1000) / 60 + ":0" + (TIMER_LENGTH / 1000) % 60);
        }
        canvas.add(this);
    }
    
    /**
     * Starts the timer by starting the thread running in the background.
     */
    public void startTimer() {
        thread.start();
    }

    public GraphicsText getGraphics() {
        return this;
    }

    /**
     * Counts down from the timer length and returns the remaining time in a string.
     * @param startTime
     * @param currentTime
     * @return A formatted string of the time left on the timer.
     */
    private String countDown(long startTime, long currentTime) {
        long timeLeft = (TIMER_LENGTH - (currentTime - startTime) + 1000) / 1000; // Converted from ms to s
        if (timeLeft*1000 > TIMER_LENGTH) { // Makes sure the timer does not show greater than the timer length
            timeLeft = TIMER_LENGTH / 1000;
        }

        int minutes = (int) timeLeft / 60;
        int seconds = (int) timeLeft % 60;

        String time = minutes + ":" + seconds;
        if (seconds < 10) {
            time = minutes + ":0" + seconds;
        }
        if (seconds < 0 ) { 
            time = "TIME'S UP!";
            this.setFillColor(Color.RED);
            timeUp = true;
        }
        return time;
    }

    /**
     * FOR INTERNAL USE. What the thread does while the timer is running.
     */
    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        this.setText("Timer: \n" + countDown(startTime, System.currentTimeMillis())); // Do the thing (aka update the timer & graphics)
        
        while (!timeUp) {
            try {
                Thread.sleep(ONE_SEC);
            } catch (InterruptedException e) {
                System.err.println(getClass().getSimpleName() + " interrupted");
                return;
            }
            
            this.setText("Timer: \n" + countDown(startTime, System.currentTimeMillis())); // Do the thing
        }
        
        // Ends the game and draws the score page.
        boggle.setGameInPlay(false);
        boggle.drawEndGameGraphics();
    }
}
