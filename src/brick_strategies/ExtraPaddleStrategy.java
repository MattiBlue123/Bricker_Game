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

    private final GameObjectCollection gameObjects; //TODO IS THERE A NEED
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
        super.onCollision(thisObj, otherObj);
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);

        ExtraPaddle.createExtraPaddle(
                gameObjects,
                windowDimensions,
                paddleImage,
                inputListener
        );
    }

    public static void resetCounter() {
        ExtraPaddle.resetCounter();

    }
}