package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A class representing a brick in the game.
 * Extends the GameObject class and includes collision handling.
 *
 * @author Amit Tzur and Zohar Mattatia
 */
public class Brick extends GameObject {
    //============================ fields =========================== //
    /* Collision strategy for handling brick collisions */
    private final CollisionStrategy collisionStrategy;
    /* The brick's coordinate in the bricks grid */
    private final Vector2 brickCoordinateInBricksGrid;
    /* Flag to indicate if the brick has been destroyed */
    private boolean isDestroyed = false;

    /**
     * Constructor for Brick.
     *
     * @param topLeftCorner               The top-left corner position of the brick.
     * @param dimensions                  The dimensions of the brick.
     * @param renderable                  The visual representation of the brick.
     * @param collisionStrategy           The strategy to handle collisions with this brick.
     * @param brickCoordinateInBricksGrid The brick's coordinate in the bricks grid.
     */
    public Brick(Vector2 topLeftCorner,
                 Vector2 dimensions,
                 Renderable renderable,
                 CollisionStrategy collisionStrategy,
                 Vector2 brickCoordinateInBricksGrid) {
        super(topLeftCorner, dimensions, renderable);
        this.brickCoordinateInBricksGrid = brickCoordinateInBricksGrid;
        this.collisionStrategy = collisionStrategy;
    }

    /**
     * Handles the collision event by delegating to the collision strategy.
     * Overrides the onCollisionEnter method from GameObject.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        // to prevent multiple collisions on the same brick and thus multiple strategy activations
        // where it is not intended
        if (this.isDestroyed) {
            return;
        }
        super.onCollisionEnter(other, collision);
        // mark the brick as destroyed after the basic collision handling, where it is removed from the game
        this.isDestroyed = true;
        collisionStrategy.onCollision(this, other);
    }

    /**
     * Gets the brick's coordinate in the bricks grid.
     * Used by the exploding brick strategy to identify neighboring bricks.
     * A Method for accessing a brick’s position in the grid (row and column)
     * - as instructed in the assignment.
     *
     * @return The brick's coordinate in the bricks grid.
     */
    public Vector2 getBrickCoordinateInBricksGrid() {
        return brickCoordinateInBricksGrid;
    }
}
