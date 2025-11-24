package gameobjects;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;


import java.awt.*;

/**
 * This class is responsible for managing the player's lives in the game.
 * It handles the display of remaining lives using heart images and a textual representation.
 * It allows for removing lives when the player loses and updating the GUI accordingly.
 * It also provides functionality to gain lives.
 * @author Amit Tzur and Zohar Mattatia
 */
public class Lives {
    // the color settings for 3 lives left
    private static final int GREEN_HEART_TEXT_VALUE = 3;
    // the color settings for 2 lives left. 1 life left will be red by default
    private static final int YELLOW_HEART_TEXT_VALUE = 2;

    // the heart image properties
    private static final float HEART_WIDTH = 30;
    private static final float HEART_HEIGHT = 30;

    // the heart image path
    private static final String HEART_IMAGE_PATH = "assets/heart.png";

    // initial number of lives - according to the assignment's description
    private static final int INITIAL_LIVES = 3;

    // the gap between hearts in the gui - set to be the same as the brick padding gap
    private static final float HEARTS_GAP = BrickerGameManager.BRICK_PADDING;

    // initial lives text - the coloring function gets a String input
    private static final String INITIAL_LIVES_TEXT = "3";

    // array to hold the hearts. It's initialized at the size of max lives,
    // but only the current lives are added to the game
    private final GameObject[] heartsArray;

    // the text renderable instance for the textual representation of remaining lives
    private final TextRenderable textRenderable;

    // the maximum number of lives allowed - its set by the BrickerGameManager and given to this class
    private final int maxLives;

    // the current number of lives left counter
    private int livesLeft;

    // the game object collection to which we add/remove the hearts and text gui elements
    // it's given to us by the BrickerGameManager so we can modify it
    // and by that keep the code in BrickerGameManager cleaner
    private final GameObjectCollection gameObjects;


    /**
     * Constructor for the Lives class.
     * Initializes the lives GUI elements and sets up the initial state.
     * Then, it adds the hearts and text to the provided game object collection.
     *
     * @param gameObjects   The collection of game objects to which the lives GUI elements will be added.
     * @param topLeftCorner The top-left corner position where the lives GUI will be displayed.
     * @param maxLives      The maximum number of lives the player can have.
     * @param imageReader   The ImageReader instance used to load the heart image.
     */
    public Lives(GameObjectCollection gameObjects, Vector2 topLeftCorner, int maxLives, ImageReader imageReader) {
        {
            this.gameObjects = gameObjects;
            this.maxLives = maxLives;
            livesLeft = INITIAL_LIVES;
            this.textRenderable = new TextRenderable(INITIAL_LIVES_TEXT);

            // setting the initial text gui of remaining lives (so that it's not displayed black)
            updateLivesText();
            this.heartsArray = new GameObject[maxLives];
            // setting the textual gui of remaining lives properties
            GameObject remainingLivesText = new GameObject(
                    topLeftCorner,
                    new Vector2(HEART_WIDTH, HEART_HEIGHT),
                    textRenderable);

            gameObjects.addGameObject(remainingLivesText, Layer.UI);

            Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH,true);


            // creating hearts enough for max lives, but adding only the initial lives to the game
            // from here we set the hearts gui of the remaining lives
            for (int i = 0; i < maxLives; i++) {
                Vector2 heartPos =
                        topLeftCorner.add(new Vector2((i + 1) * (HEART_WIDTH + HEARTS_GAP), 0));

                GameObject heart = new GameObject(
                        heartPos,
                        new Vector2(HEART_WIDTH, HEART_HEIGHT),
                        heartImage);

                heartsArray[i] = heart;
                if (i < INITIAL_LIVES) {
                    gameObjects.addGameObject(heart, Layer.UI);
                }
            }
        }


    }

    /**
     * Updates the textual representation of remaining lives on the GUI.
     * Changes the text color based on the number of lives left - according to predefined thresholds.
     */
    private void updateLivesText() {
        // here we are updating the string that's showing on the gui
        textRenderable.setString(String.valueOf(livesLeft));

        // here we are updating the color of the text according to the number of lives left
        if (livesLeft >= GREEN_HEART_TEXT_VALUE) {
            textRenderable.setColor(Color.GREEN);
        } else if (livesLeft == YELLOW_HEART_TEXT_VALUE) {
            textRenderable.setColor(Color.YELLOW);
        } else {
            textRenderable.setColor(Color.RED);
        }
    }

    /**
     * Removes a life from the player, called when the ball hits the "ground" (bottom of the screen).
     * Updates the GUI by removing the last heart added and updating the textual representation.
     * Ensures that lives do not go below zero to avoid out of bounds errors.
     */
    public void removeLife() {
        if (livesLeft > 0) {
            // removing a heart from the gui - the last one that was added
            GameObject heartToRemove = heartsArray[livesLeft - 1];
            gameObjects.removeGameObject(heartToRemove, Layer.UI);


            livesLeft--;

            updateLivesText();
        }
    }

    /**
     * Increases the player's lives by one, called when the player gains an extra life.
     * Updates the GUI by adding a heart back and updating the textual representation.
     * Ensures that lives do not exceed the maximum allowed.
     */
    public void gainLife() {
        if (livesLeft >= maxLives) {
            return;
        }
        livesLeft++;
    }


    /**
     * Getter for the current number of lives left.
     *
     * @return The current number of lives left.
     */
    public int getLives() {
        return livesLeft;
    }

}
