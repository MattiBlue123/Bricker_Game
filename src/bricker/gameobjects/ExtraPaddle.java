package bricker.gameobjects;

import bricker.main.GameConstants;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A class representing an extra paddle in the game.
 * Extends the Paddle class and includes collision handling to remove itself after a set number of collisions.
 * Ensures that only one extra paddle exists at a time.
 *
 * @author Amit Tzur and Zohar Mattatia
 */
public class ExtraPaddle extends Paddle {

    //============================ private constants =========================== //
    /* Tag for identifying the extra paddle */
    private static final String TAG = "extraPaddle";
    /* Maximum number of collisions before the extra paddle is removed */
    private static final int MAX_COLLISIONS = 4;

    //============================ fields =========================== //
    /* Static counter to track the number of active extra paddles */
    private static int activeExtraPaddles = 0;
    /* Counter to track the number of collisions for this extra paddle */
    private int collisionCounter = 0;
    /* Collection of game objects in the game */
    private final GameObjectCollection gameObjects;


    /**
     * Constructor for the ExtraPaddle class.
     * Overloaded to include GameObjectCollection for removal upon reaching max collisions.
     * Works only through the static createExtraPaddle method to ensure single instance.
     *
     * @param topLeftCorner The top-left corner of the paddle.
     * @param dimensions    The dimensions of the paddle.
     * @param renderable    The renderable object for the paddle.
     * @param inputListener The user input listener.
     * @param gameObjects   The collection of game objects in the game.
     */
    private ExtraPaddle(Vector2 topLeftCorner,
                        Vector2 dimensions,
                        Renderable renderable,
                        UserInputListener inputListener,
                        GameObjectCollection gameObjects) {
        super(topLeftCorner, renderable, inputListener);
        this.gameObjects = gameObjects;
    }

    /**
     * Static method to create and add an extra paddle to the game.
     * Ensures that only one extra paddle exists at a time.
     *
     * @param gameObjects      The collection of game objects in the game.
     * @param windowDimensions The dimensions of the game window.
     * @param image            The renderable image for the paddle.
     * @param inputListener    The user input listener.
     */
    public static void createExtraPaddle(GameObjectCollection gameObjects,
                                         Vector2 windowDimensions,
                                         Renderable image,
                                         UserInputListener inputListener) {

        // Check if one already exists
        if (activeExtraPaddles > 0) {
            return; // Don't create a new one
        }

        // creating and centering according to instruction
        Vector2 paddleSize = GameConstants.PADDLE_DIMENSIONS;
        Vector2 location = new Vector2(windowDimensions.x() * GameConstants.HALF_FACTOR,
                windowDimensions.y() * GameConstants.HALF_FACTOR);

        ExtraPaddle extraPaddle = new ExtraPaddle(location, paddleSize, image,
                inputListener, gameObjects);
        extraPaddle.setTag(TAG);
        extraPaddle.setCenter(location);
        gameObjects.addGameObject(extraPaddle, Layer.DEFAULT);
        activeExtraPaddles++;
    }

    /**
     * Handles collision events.
     * Increments the collision counter and removes the paddle after reaching the maximum allowed collisions.
     * Overrides the onCollisionEnter method from Paddle.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        // only count collisions with the ball (the puck also has the BALL_TAG)
        if (other.getTag().equals(GameConstants.BALL_TAG)) {
            collisionCounter++;
        }
        // remove the extra paddle after reaching max collisions
        if (collisionCounter >= MAX_COLLISIONS) {
            gameObjects.removeGameObject(this, Layer.DEFAULT);
            // reset the static counter to allow new extra paddles
            activeExtraPaddles = 0;
        }
    }

    /**
     * Resets the static counter for active extra paddles.
     * Useful for testing purposes to ensure a clean state.
     */
    public static void resetCounter() {
        activeExtraPaddles = 0;
    }
}