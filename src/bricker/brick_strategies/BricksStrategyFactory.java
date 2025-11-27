package bricker.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import bricker.gameobjects.Brick;
import bricker.gameobjects.LivesManager;

import java.util.Random;

/**
 * Factory class to generate different brick collision strategies.
 * This class randomly selects and returns a collision strategy
 * based on the provided sample space.
 *
 * @author Amit Tzur and Zohar Mattatia
 */
public class BricksStrategyFactory {

    // =========================== private constats =========================== //
    /* double strategies counter to avoid more than 2 for the same brick */
    private static final int SET_DOUBLE_STRATEGY_COUNTER = 1;
    /* index for pucks strategy to be chosen 1/10 chance */
    private static final int PUCKS_STRATEGY_INDEX = 1;
    /* index for extra paddle strategy to be chosen in 1/10 chance */
    private static final int EXTRA_PADDLE_STRATEGY_INDEX = 2;
    /* index for explode strategy to be chosen in 1/10 chance */
    private static final int EXPLODE_STRATEGY_INDEX = 3;
    /* index for extra life strategy to be chosen in 1/10 chance */
    private static final int LIVES_STRATEGY_INDEX = 4;
    /* index for double strategy to be chosen in 1/10 chance */
    private static final int DOUBLE_STRATEGY_INDEX = 5;

    // =========================== fields =========================== //
    /* game objects is needed to add/remove objects from the game */
    private final GameObjectCollection gameObjects;
    /* brick counter to keep track of remaining bricks */
    private final Counter brickCounter;
    /* image reader to read images for the strategies */
    private final ImageReader imageReader;
    /* sound reader to read sounds for the strategies */
    private final SoundReader soundReader;
    /* user input listener to handle user inputs */
    private final UserInputListener inputListener;
    /* 2D array representing the grid of bricks */
    private final Brick[][] bricksGrid; //
    /* lives manager to manage player's lives */
    private final LivesManager livesManager;
    /* random number generator for strategy selection */
    private static final Random RANDOM = new Random();

    /**
     * Constructor for BricksStrategyFactory.
     *
     * @param gameObjects   The collection of game objects in the game.
     * @param brickCounter  Counter to keep track of remaining bricks.
     * @param imageReader   ImageReader for loading images.
     * @param soundReader   SoundReader for loading sounds.
     * @param inputListener UserInputListener for handling user inputs.
     * @param bricksGrid    2D array representing the grid of bricks.
     */
    public BricksStrategyFactory(GameObjectCollection gameObjects,
                                 Counter brickCounter,
                                 ImageReader imageReader,
                                 SoundReader soundReader,
                                 UserInputListener inputListener,
                                 Brick[][] bricksGrid,
                                 LivesManager livesManager) {

        this.gameObjects = gameObjects;
        this.brickCounter = brickCounter;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.bricksGrid = bricksGrid;
        this.livesManager = livesManager;
    }

    /**
     * Generates and returns a collision strategy based on a random selection
     * from the provided strategy sample space.
     * The sample space defines the range of strategies to choose from.
     * Since double strategy can be set only twice per brick, the factory
     * ensures that the double strategy is only included in the selection
     * when appropriate by setting the strategySampleSpace parameter to be 4 (excluding double)
     * or 10 when including double and also the basic strategy.
     *
     * @param strategySampleSpace The range of strategies to choose from.
     * @return A CollisionStrategy instance.
     */
    public CollisionStrategy getStrategy(final int strategySampleSpace) {
        /* randomly choose a strategy from the sample space */
        int chosenStrategy = RANDOM.nextInt(strategySampleSpace) + 1; // to ensure 1-based indexing

        return switch (chosenStrategy) {
            case PUCKS_STRATEGY_INDEX -> // 1. Puck
                    new PuckStrategy(gameObjects, brickCounter, imageReader, soundReader);

            case EXTRA_PADDLE_STRATEGY_INDEX -> // 2. Extra Paddle
                    new ExtraPaddleStrategy(gameObjects, brickCounter, imageReader, inputListener);

            case EXPLODE_STRATEGY_INDEX -> // 3. Exploding
                    new ExplodingBrickStrategy(gameObjects, brickCounter, bricksGrid, soundReader);

            case LIVES_STRATEGY_INDEX -> // 4. Extra Life
                    new RecoverLifeStrategy(gameObjects, brickCounter, imageReader, livesManager);

            case DOUBLE_STRATEGY_INDEX -> // 5. Double Strategy
                    new DoubleStrategy(gameObjects, brickCounter, imageReader, soundReader,
                            inputListener, bricksGrid, livesManager, SET_DOUBLE_STRATEGY_COUNTER);

            default -> new BasicCollisionStrategy(gameObjects, brickCounter); // 6-10. Basic strategy
        };
    }

}



