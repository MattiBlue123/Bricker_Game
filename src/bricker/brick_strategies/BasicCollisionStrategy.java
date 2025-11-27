package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * A basic collision strategy that removes the brick from the game upon collision.
 * Extends the CollisionStrategy interface.
 *
 * @author Amit Tzur and Zohar Mattatia
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    protected final GameObjectCollection gameObjects;
    private final Counter brickCounter;

    /**
     * Constructor for BasicCollisionStrategy.
     *
     * @param gameObjects  The collection of game objects in the game.
     * @param brickCounter A counter tracking the number of bricks remaining.
     */
    public BasicCollisionStrategy(GameObjectCollection gameObjects, Counter brickCounter) {
        this.gameObjects = gameObjects;
        this.brickCounter = brickCounter;
    }

    /**
     * Handles the collision event by removing the firstObject from the game
     * and decrementing the firstObject counter.
     *
     * @param firstObject  The firstObject game object that was collided with.
     * @param secondObject The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject firstObject, GameObject secondObject) {
        if (gameObjects.removeGameObject(firstObject, Layer.STATIC_OBJECTS)) {
            brickCounter.decrement();
        }
    }
}