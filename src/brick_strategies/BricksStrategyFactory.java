package brick_strategies;

import brick_strategies.ExtraPaddleStrategy;
import brick_strategies.PuckStrategy;
import brick_strategies.CollisionStrategy;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import danogl.util.Vector2;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;

import java.util.Random;

public class BricksStrategyFactory {
    private static final int SPECIAL_BRICKS_COUNT = 5;
    private static final int PUCKS_STRATEGY_INDEX = 1;
    private static final int EXTRA_PADDLE_STRATEGY_INDEX = 2;
    private static final int EXPLODE_STRATEGY_INDEX = 3;
    private static final int LIVES_STRATEGY_INDEX = 4;
    private static final int DOUBLE_STRATEGY_INDEX = 5;
    private static final int STRATEGY

    private final GameObjectCollection gameObjects;
    private final Counter brickCounter;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final float puckSize; // Needed for PuckStrategy

    public BricksStrategyFactory(GameObjectCollection gameObjects,
                                 Counter brickCounter,
                                 ImageReader imageReader, SoundReader soundReader,
                                 UserInputListener inputListener,
                                 Vector2 windowDimensions, float puckSize){

        this.gameObjects = gameObjects;
        this.brickCounter = brickCounter;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;




    }

    public CollisionStrategy getStrategy() {
        Random random = new Random();
        // 50% chance for basic, 50% chance for special
        if (random.nextBoolean()) {
            return new BasicCollisionStrategy(gameObjects, brickCounter);
        } else {
            // It's a special strategy (1/5 chance for each of the 5 types)
            // 0=Puck, 1=Paddle, 2=Exploding, 3=Lives, 4=Double
            int type = random.nextInt(5);
            return buildSpecialStrategy(type);
        }
    }


    private CollisionStrategy buildSpecialStrategy(int type) {
        return switch (type) {
            case 0 -> // Puck
                    new PuckStrategy(gameObjects, imageReader, soundReader, brickCounter, windowDimensions, puckSize);
            case 1 -> // Extra Paddle
                    new ExtraPaddleStrategy(gameObjects, brickCounter, imageReader, inputListener, windowDimensions);
            case 2 -> // Exploding
                    new ExplodingBrickStrategy(gameObjects, brickCounter, soundReader);
            case 3 -> // Lives
                    new RecoverLifeStrategy(gameObjects, brickCounter, imageReader, windowDimensions);
            case 4 -> // Double
                    null; // TODO: Implement Double Strategy
            default -> new BasicCollisionStrategy(gameObjects, brickCounter);
        };
    }

    public static void resetStrategies() {
        ExtraPaddleStrategy.resetCounter();
        // Reset other strategy-related counters if needed in the future
    }
}

