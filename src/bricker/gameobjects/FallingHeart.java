package bricker.gameobjects;

import bricker.main.GameConstants;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A class representing a falling heart in the game.
 * Extends the GameObject class and includes collision handling to grant an extra life to the player.
 * The heart falls down the screen and can be collected by the main paddle.
 *
 * @author Amit Tzur and Zohar Mattatia
 */
public class FallingHeart extends GameObject {

    //============================ constants =========================== //
    /* Speed at which the heart falls down the screen */
    private static final int HEART_SPEED = 100;

    //============================ fields =========================== //
    /* Collection of game objects in the game -
     used for removing the heart when collected or out of bounds */
    private final GameObjectCollection gameObjects;
    /* Lives manager to handle life recovery */
    private final LivesManager livesManager;

    /**
     * Constructor for FallingHeart.
     *
     * @param gameObjects   The collection of game objects in the game.
     * @param topLeftCorner The top-left corner position of the heart.
     * @param dimensions    The dimensions of the heart.
     * @param renderable    The visual representation of the heart.
     * @param livesManager  The lives manager to handle life recovery.
     */
    public FallingHeart(GameObjectCollection gameObjects,
                        Vector2 topLeftCorner,
                        Vector2 dimensions,
                        Renderable renderable,
                        LivesManager livesManager) {
        super(topLeftCorner, dimensions, renderable);

        this.gameObjects = gameObjects;
        this.livesManager = livesManager;
        this.setVelocity(Vector2.DOWN.mult(HEART_SPEED));
    }

    /**
     * Determines if the heart should collide with the given game object.
     * Only collides with the main paddle.
     * Overrides the shouldCollideWith method from GameObject.
     * It's not being called anywhere in OUR code,
     * but it's part of the danogl framework's collision handling.
     * By overriding it, we define custom collision behavior for the FallingHeart -
     * the hearts go through all objects except the main paddle!
     *
     * @param object The other game object to check for collision.
     * @return True if the heart should collide with the main paddle, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject object) {
        // This is the "Reference Comparison".
        // It checks if the object defined as 'other' is strictly the same object in memory
        // as the 'mainPaddle' we saved in the constructor.
        return object.getTag().equals(GameConstants.MAIN_PADDLE_TAG);
    }

    /**
     * Updates the heart's position and checks if it has fallen out of bounds.
     * Removes the heart from the game if it falls below the screen.
     * Overrides the update method from GameObject.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // If the heart falls below the screen, remove it from the game
        if (this.getTopLeftCorner().y() > GameConstants.WINDOW_DIMENSIONS.y()) {
            gameObjects.removeGameObject(this);
        }
    }


    /**
     * Handles the collision event by granting an extra life to the player
     * and removing the heart from the game.
     * Overrides the onCollisionEnter method from GameObject.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // Since shouldCollideWith returns false for everything else,
        // we know 'other' MUST be the mainPaddle here.
        livesManager.gainLife();
        gameObjects.removeGameObject(this);

    }
}