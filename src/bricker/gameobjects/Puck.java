package bricker.gameobjects;

import bricker.main.GameConstants;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A class representing a puck in the game.
 * Extends the Ball class and includes behavior to remove itself when it goes out of bounds.
 *
 * @author Amit Tzur and Zohar Mattatia
 */
public class Puck extends Ball {
    //============================ fields =========================== //
    /* Dimensions of the puck */
    private final Vector2 puckDimensions;
    /* Dimensions of the game window */
    private final Vector2 windowDimensions;
    /* Collection of game objects in the game */
    private final GameObjectCollection gameObjects;

    /**
     * Constructor for Puck.
     *
     * @param topLeftCorner  The top-left corner position of the puck.
     * @param renderable     The visual representation of the puck.
     * @param collisionSound The sound effect to play upon collision.
     * @param gameObjects    The collection of game objects in the game.
     */
    public Puck(Vector2 topLeftCorner,
                Renderable renderable,
                Sound collisionSound,
                GameObjectCollection gameObjects) {

        super(topLeftCorner, renderable, collisionSound);

        this.puckDimensions = GameConstants.BALL_DIMENSIONS;
        this.windowDimensions = GameConstants.WINDOW_DIMENSIONS;
        this.gameObjects = gameObjects;
    }

    /**
     * Updates the puck's state.
     * Removes the puck from the game if it goes out of bounds (below the window).
     * Overrides the update method from Ball.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (getCenter().y() > windowDimensions.y() + puckDimensions.y()) {
            gameObjects.removeGameObject(this);
        }
    }
}
