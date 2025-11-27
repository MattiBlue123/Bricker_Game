package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import bricker.gameobjects.Brick;
import bricker.gameobjects.LivesManager;

import java.util.Random;

public class DoubleStrategy extends BasicCollisionStrategy implements CollisionStrategy {
    // =========================== private constats =========================== //
    /* minimum new strategies to create */
    private static final int MIN_NEW_STRATEGIES = 2;
    /* maximum new strategies to create */
    private static final int MAX_NEW_STRATEGIES = 3;
    /* maximum double strategies allowed */
    private static final int MAX_DOUBLE_STRATEGIES = 2;
    /* exclude double and basic strategies index */
    private static final int EXCLUDE_DOUBLE_AND_BASIC = 4;
    /* exclude basic strategy index */
    private static final int EXCLUDE_BASIC = 5;
    /* index for double strategy to be chosen in 1/10 chance */
    private static final int DOUBLE_STRATEGY_INDEX = 5;

    // =========================== fields =========================== //
    /* random number generator for strategy selection */
    private static final Random RANDOM = new Random();

    /* array of collision strategies to be executed */
    private final CollisionStrategy[] strategies;
    /* factory to create various brick strategies */
    private final BricksStrategyFactory bricksStrategyFactory;
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
    /* counter for the number of double strategies applied */
    private final int doubleStrategyCounter;

    /**
     * Constructor for DoubleStrategy.
     * All of these parameters are needed to create new strategies within the double strategy,
     * using the BricksStrategyFactory -
     * which requires all of them to run all the different strategies.
     *
     * @param gameObjects           The collection of game objects in the game.
     * @param brickCounter          Counter to keep track of remaining bricks.
     * @param imageReader           ImageReader for loading images.
     * @param soundReader           SoundReader for loading sounds.
     * @param inputListener         UserInputListener for handling user inputs.
     * @param bricksGrid            2D array representing the grid of bricks.
     * @param livesManager          LivesManager to manage player's lives.
     * @param doubleStrategyCounter Counter for the number of double strategies applied.
     */
    public DoubleStrategy(GameObjectCollection gameObjects,
                          Counter brickCounter,
                          ImageReader imageReader,
                          SoundReader soundReader,
                          UserInputListener inputListener,
                          Brick[][] bricksGrid, LivesManager livesManager,
                          final int doubleStrategyCounter) {

        super(gameObjects, brickCounter);
        this.doubleStrategyCounter = doubleStrategyCounter;
        this.gameObjects = gameObjects;
        this.brickCounter = brickCounter;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.bricksGrid = bricksGrid;
        this.livesManager = livesManager;
        this.bricksStrategyFactory = new BricksStrategyFactory(
                gameObjects,
                brickCounter,
                imageReader,
                soundReader,
                inputListener,
                bricksGrid,
                livesManager);

        // initialize the strategies array based on the double strategy counter
        // if we haven't reached the max, we can have 2 or 3 strategies
        if (this.doubleStrategyCounter < MAX_DOUBLE_STRATEGIES) {
            this.strategies = new CollisionStrategy[MAX_NEW_STRATEGIES];
        } else {
            this.strategies = new CollisionStrategy[MIN_NEW_STRATEGIES];
        }
    }

    /**
     * Handles the collision event by executing multiple strategies.
     * Chooses new strategies based on random selection and the current
     * double strategy counter.
     * Overrides the onCollision method from BasicCollisionStrategy.
     *
     * @param firstObject  The brick game object that was collided with.
     * @param secondObject The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject firstObject, GameObject secondObject) {
        super.onCollision(firstObject, secondObject);

        for (int i = 0; i < strategies.length; i++) {
            // to get a 1-based index for strategy selection
            int chosenStrategy = RANDOM.nextInt(EXCLUDE_BASIC) + 1;

            // decide which strategy to assign based on the chosen index
            // makes sure we don't exceed the max double strategies allowed, which is 2
            if (chosenStrategy == DOUBLE_STRATEGY_INDEX && doubleStrategyCounter < MAX_DOUBLE_STRATEGIES) {
                strategies[i] = new DoubleStrategy(
                        gameObjects,
                        brickCounter,
                        imageReader,
                        soundReader,
                        inputListener,
                        bricksGrid,
                        livesManager,
                        doubleStrategyCounter + 1);
                // if we reached the max double strategies, exclude double (and basic strategies which
                // aren't allowed anyway here)
            } else {
                // if we can still have double strategies
                if (doubleStrategyCounter < MAX_DOUBLE_STRATEGIES) {
                    strategies[i] = bricksStrategyFactory.getStrategy(
                            EXCLUDE_BASIC);
                    // else, we have to exclude both double and basic strategies
                } else {
                    strategies[i] = bricksStrategyFactory.getStrategy(
                            EXCLUDE_DOUBLE_AND_BASIC);
                }

            }
        }

        // execute all chosen strategies
        for (CollisionStrategy strategy : strategies) {
            if (strategy != null) { // defensive
                strategy.onCollision(firstObject, secondObject);
            }

        }

    }
}