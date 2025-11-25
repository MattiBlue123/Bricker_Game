package brick_strategies;

import danogl.util.Counter;
import gameobjects.ExtraPaddle;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class ExtraPaddleStrategy extends BasicCollisionStrategy  {

    private final GameObjectCollection gameObjects;
    private final ImageReader imageReader;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;

    public ExtraPaddleStrategy(GameObjectCollection gameObjects,
                               Counter brickCounter,
                               ImageReader imageReader,
                               UserInputListener inputListener,
                               Vector2 windowDimensions) {

        super(gameObjects, brickCounter);
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        // 1. Remove the brick
        // (Assuming your BasicCollisionStrategy logic or manual removal happens here)
        // Note: If you are using Decorator for Double Behavior later, be careful not to remove the brick twice.
        // Usually, the BasicCollisionStrategy handles removal, or you duplicate that line here.
        gameObjects.removeGameObject(thisObj, danogl.collisions.Layer.STATIC_OBJECTS);

        // 2. Delegate creation responsibility to the Class itself
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);

        ExtraPaddle.createExtraPaddle(
                gameObjects,
                windowDimensions,
                paddleImage,
                inputListener,
                windowDimensions.x()
        );
    }

    public static void resetCounter() {
        ExtraPaddle.resetCounter();
    }
}