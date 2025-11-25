package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class ExtraPaddle extends Paddle {

    private static final int MAX_COLLISIONS = 4;
    private int collisionCounter = 0;
    private final GameObjectCollection gameObjects;
    private static int activeExtraPaddles = 0;


    /** Constructor for the ExtraPaddle class.
     *  Overloaded to include GameObjectCollection for removal upon reaching max collisions.
     *  Works only through the static createExtraPaddle method to ensure single instance.
     *
     * @param topLeftCorner  The top-left corner of the paddle.
     * @param dimensions     The dimensions of the paddle.
     * @param renderable     The renderable object for the paddle.
     * @param inputListener  The user input listener.
     * @param windowWidth    The width of the game window.
     * @param gameObjects    The collection of game objects in the game.
     */
    private ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                        UserInputListener inputListener, float windowWidth,
                        GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowWidth);
        this.gameObjects = gameObjects;
    }

    // handles the logic of no more than one extra paddle at a time
    public static void createExtraPaddle(GameObjectCollection gameObjects,
                                         Vector2 windowDimensions,
                                         Renderable image,
                                         UserInputListener inputListener) {

        // Check if one already exists
        if (activeExtraPaddles > 0) {
            return; // Don't create a new one
        }

        // centering according to instruction
        Vector2 paddleSize = new Vector2(100, 15);
        Vector2 location = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2);

        ExtraPaddle extraPaddle = new ExtraPaddle(location, paddleSize, image,
                inputListener, windowDimensions.x(), gameObjects);

        extraPaddle.setCenter(location);
        gameObjects.addGameObject(extraPaddle, Layer.DEFAULT);
        activeExtraPaddles = 1;
    }

    //  remove itself after 4 collisions
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        collisionCounter++;
        if (collisionCounter >= MAX_COLLISIONS) {
            gameObjects.removeGameObject(this, Layer.DEFAULT);
            collisionCounter = 0; // TODO: is this needed?
            activeExtraPaddles = 0;
        }
    }
    public static void resetCounter() {
        activeExtraPaddles = 0;
    }
}