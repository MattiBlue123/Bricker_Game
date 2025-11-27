package bricker.main;

import bricker.brick_strategies.BricksStrategyFactory;
import bricker.brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * The BrickerGameManager class manages the Bricker game.
 * It initializes game objects, runs it,  handles game updates, and checks for game end conditions.
 *
 * @author Amit Tzur and Zohar Mattatia
 */
public class BrickerGameManager extends GameManager {
    // =========================== private constants =========================== //
    /* initial string for game prompt */
    private static final String GAME_STATUS_PROMPT_INIT = "";
    /* string for game prompt */
    private static final String PLAY_AGAIN = " Play again?";
    /* string for game prompt */
    private static final String YOU_WIN = "You win!";
    /* string for game prompt */
    private static final String YOU_LOSE = "You Lose!";
    /* ball image asset path */
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    /* background image asset path */
    private static final String ASSETS_DARK_BG_2_SMALL_JPEG = "assets/DARK_BG2_small.jpeg";
    /* brick image asset path */
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    /* game title */
    private static final String BOUNCING_BALL = "Bouncing Ball";
    /* paddle offset from bottom of the window */
    private static final float PADDLE_OFFSET_FROM_BOTTOM = 30;
    /* left wall width which is its thickness */
    private static final float LEFT_WALL_WIDTH = 5;
    /* right wall height which means the coverage of the wall */
    private static final float LEFT_WALL_HEIGHT = GameConstants.WINDOW_HEIGHT;
    /* right wall width which is its thickness */
    private static final float RIGHT_WALL_WIDTH = 5;
    /* right wall height which means the coverage of the wall */
    private static final float RIGHT_WALL_HEIGHT = GameConstants.WINDOW_HEIGHT;
    /* top wall width which means the coverage of the ceiling */
    private static final float TOP_WALL_WIDTH = GameConstants.WINDOW_WIDTH;
    /* top wall height which is its thickness */
    private static final float TOP_WALL_HEIGHT = 5;
    /* brick height - its thickness */
    private static final float BRICK_HEIGHT = 15;
    /* wall overlap correction to calculate the wall's position */
    private static final float WALL_OVERLAP_CORRECTION = 1;
    /* default number of brick rows */
    private static final int DEFAULT_BRICK_ROWS = 7;
    /* default number of brick columns */
    private static final int DEFAULT_BRICK_COLUMNS = 8;
    /* strategy sample space for random selection */
    private static final int STRATEGY_SAMPLE_SPACE = 10;

    /* lives position in the window */
    private static final Vector2 LIVES_POSITION = new Vector2(20, 450);
    /* walls dimensions array */
    private static final float[][] WALLS_DIMENSIONS = {
            {LEFT_WALL_WIDTH, LEFT_WALL_HEIGHT},
            {RIGHT_WALL_WIDTH, RIGHT_WALL_HEIGHT},
            {TOP_WALL_WIDTH, TOP_WALL_HEIGHT}};
    /* wall positions array */
    /* positions are calculated according to wall dimensions and window dimensions */
    private static final Vector2[] WALL_POSITIONS = {
            Vector2.ZERO,
            new Vector2(GameConstants.WINDOW_WIDTH - RIGHT_WALL_WIDTH + WALL_OVERLAP_CORRECTION, 0),
            Vector2.ZERO};

    // =========================== fields =========================== //
    /* number of brick columns */
    private final int brickColumns;
    /* number of brick rows */
    private final int brickRows;
    /* counter for remaining bricks */
    private Counter brickCounter;
    /* lives manager to handle player lives */
    private LivesManager livesManager;
    /* the main ball object */
    private Ball ball;
    /* window controller for managing the game window */
    private WindowController windowController;
    /* dimensions of the game window */
    private Vector2 windowDimensions;
    /* user input listener for handling player input */
    private UserInputListener inputListener;

    /**
     * Constructor for the BrickerGameManager class.
     * Sets up the game window with the specified title and dimensions,
     * and initializes the brick layout based on the provided number of columns and rows.
     *
     * @param windowTitle      Title of the game window
     * @param windowDimensions Dimensions of the game window
     * @param brickColumns     Number of brick columns
     * @param brickRows        Number of brick rows
     */
    public BrickerGameManager(String windowTitle,
                              Vector2 windowDimensions,
                              int brickColumns,
                              int brickRows) {
        super(windowTitle, windowDimensions);
        this.brickColumns = brickColumns;
        this.brickRows = brickRows;
        this.brickCounter = new Counter(brickRows * brickColumns);
    }

    /**
     * Initializes the game by creating game objects such as the ball, paddle, walls, background,
     * lives manager, and bricks. Also sets up the input listener and window controller.
     *
     * @param imageReader      ImageReader for loading images
     * @param soundReader      SoundReader for loading sounds
     * @param inputListener    UserInputListener for handling player input
     * @param windowController WindowController for managing the game window
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.windowController = windowController;
        this.inputListener = inputListener;
        this.windowDimensions = windowController.getWindowDimensions();
        this.brickCounter = new Counter(brickRows * brickColumns);

        createBall(imageReader, soundReader);
        createPaddle(imageReader, inputListener);
        createWall();
        createBackground(imageReader);
        createLives(imageReader);
        createBricker(imageReader, soundReader);
        // resetting extra paddle counter since it's a static counter, and we are starting a new game
        ExtraPaddle.resetCounter();
    }

    /**
     * Updates the game state, checks for game end conditions,
     * and resets the brick counter if the 'W' key is pressed.
     *
     * @param deltaTime Time elapsed since the last update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (inputListener.isKeyPressed(KeyEvent.VK_W)) {
            brickCounter.reset();
        }
        checkForGameEnd();
    }

    /**
     * Checks for game end conditions such as the ball falling below the window
     * or all bricks being removed. Displays a prompt to play again or exit the game.
     */
    private void checkForGameEnd() {
        double ballHeight = ball.getCenter().y();
        String prompt = GAME_STATUS_PROMPT_INIT;

        // checking if a ball fell
        if (ballHeight > windowDimensions.y()) {
            this.livesManager.removeLife();
            int currentLives = this.livesManager.getLives();

            // checking if we lost because the ball fell, and we have no lives left
            if (currentLives > 0) {
                ball.setCenter(windowDimensions.mult(GameConstants.HALF_FACTOR));
                setRandomBallVelocity(ball);
            } else {
                prompt = YOU_LOSE;
            }
        }

        // checking if all brick are removed
        else if (brickCounter.value() <= 0) {
            prompt = YOU_WIN;
        }

        // handling the dialog yes/no window
        if (!prompt.isEmpty()) {
            prompt += PLAY_AGAIN;
            if (windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }
    }

    /**
     * Creates the ball game object, sets its initial position and velocity,
     * and adds it to the game object collection.
     *
     * @param imageReader ImageReader for loading images
     * @param soundReader SoundReader for loading sounds
     */
    private void createBall(ImageReader imageReader,
                            SoundReader soundReader) {
        // creating ball
        // isTopLeft above defines if we want the left top pixel to be in same color of window
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(GameConstants.BALL_COLLISION_SOUND);
        GameObject ball = new Ball(Vector2.ZERO, ballImage, collisionSound);
        ball.setVelocity(Vector2.DOWN.mult(GameConstants.BALL_SPEED)); // Down is (0,1)
        ball.setCenter(windowDimensions.mult(GameConstants.HALF_FACTOR));
        this.gameObjects().addGameObject(ball, Layer.DEFAULT);
        this.ball = (Ball) ball;
        setRandomBallVelocity(this.ball);

    }

    /**
     * Creates the paddle game object, sets its initial position,
     * and adds it to the game object collection.
     *
     * @param imageReader   ImageReader for loading images
     * @param inputListener UserInputListener for handling player input
     */
    private void createPaddle(ImageReader imageReader,
                              UserInputListener inputListener) {
        // initializing local variables
        Renderable paddleImage = imageReader.readImage(GameConstants.PADDLE_IMAGE, true);
        // creating the main paddle object - the user's paddle
        Paddle mainPaddle = new Paddle(Vector2.ZERO,
                paddleImage,
                inputListener);
        mainPaddle.setCenter(new Vector2(windowDimensions.x() * GameConstants.HALF_FACTOR,
                windowDimensions.y() - PADDLE_OFFSET_FROM_BOTTOM));
        mainPaddle.setTag(GameConstants.MAIN_PADDLE_TAG);
        this.gameObjects().addGameObject(mainPaddle, Layer.DEFAULT);

    }

    /**
     * Creates the walls (left, right, top) game objects and adds them to the game object collection.
     * The walls are represented as dark gray rectangles.
     */
    private void createWall() {
        for (int i = 0; i < WALL_POSITIONS.length; i++) {
            float x = WALL_POSITIONS[i].x();
            float y = WALL_POSITIONS[i].y();
            float width = WALLS_DIMENSIONS[i][0];
            float height = WALLS_DIMENSIONS[i][1];
            GameObject wall = new GameObject(new Vector2(x, y),
                    new Vector2(width, height),
                    new RectangleRenderable(Color.DARK_GRAY));
            this.gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);
        }
    }

    /**
     * Creates the background game object and adds it to the game object collection.
     * The background is set to the BACKGROUND layer to ensure it is behind all other objects.
     *
     * @param imageReader ImageReader for loading images
     */
    private void createBackground(ImageReader imageReader) {
        Renderable backgroundImage = imageReader.readImage(ASSETS_DARK_BG_2_SMALL_JPEG, false);
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions, backgroundImage);
        // Add with the lowest layer to ensure it is behind everything
        this.gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     * Creates the grid of bricks for the game, initializes their positions and collision strategies,
     * and adds them to the game object collection.
     *
     * @param imageReader ImageReader for loading images
     * @param soundReader SoundReader for loading sounds
     */
    private void createBricker(ImageReader imageReader, SoundReader soundReader) {
        // grid of bricks in the game
        Brick[][] bricksGrid = new Brick[brickRows][brickColumns];

        // calculating brick width according to number of bricks in a row
        float windowSize = GameConstants.WINDOW_WIDTH;
        float totalPadding = GameConstants.STANDARD_PADDING * (brickColumns + 1);
        float totalWalls = LEFT_WALL_WIDTH + RIGHT_WALL_WIDTH;
        float spaceForBricks = windowSize - totalPadding - totalWalls;
        float brickWidth = spaceForBricks / this.brickColumns;

        Renderable brickerImage = imageReader.readImage(BRICK_IMAGE_PATH, false);
        Vector2 brickDimensions = new Vector2(brickWidth, BRICK_HEIGHT);

        // for each brick in the grid, create it and add it to the game
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickColumns; j++) {
                CollisionStrategy collisionStrategy = new BricksStrategyFactory(
                        this.gameObjects(),
                        this.brickCounter,
                        imageReader,
                        soundReader,
                        inputListener,
                        bricksGrid,
                        this.livesManager).getStrategy(STRATEGY_SAMPLE_SPACE);

                // calculating brick position according to its row and column
                float x = (j * brickWidth) + (GameConstants.STANDARD_PADDING * (j + 1)) + LEFT_WALL_WIDTH;
                float y = (i * BRICK_HEIGHT) + (GameConstants.STANDARD_PADDING * (i + 1) + TOP_WALL_HEIGHT);
                Vector2 brickPosition = new Vector2(x, y);

                Brick brick = new Brick(brickPosition, brickDimensions, brickerImage, collisionStrategy,
                        new Vector2(i, j));
                bricksGrid[i][j] = brick;
                this.gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);

            }
        }

    }

    /**
     * Creates the LivesManager to handle player lives and adds it to the game object collection.
     *
     * @param imageReader ImageReader for loading images
     */
    private void createLives(ImageReader imageReader) {
        this.livesManager = new LivesManager(this.gameObjects(), LIVES_POSITION, imageReader);
    }


    /**
     * Validates and parses command-line arguments for brick columns and rows.
     * If arguments are not provided, default values are used.
     * It's static so it can be used in main method.
     *
     * @param args Command-line arguments
     * @return An array containing the number of columns and rows
     */
    private static int[] validateArgs(String[] args) {
        int columns = DEFAULT_BRICK_COLUMNS;
        int rows = DEFAULT_BRICK_ROWS;
        if (args.length >= 2) {
            columns = Integer.parseInt(args[0]);
            rows = Integer.parseInt(args[1]);
        }
        return new int[]{columns, rows};
    }

    /**
     * Sets a random initial velocity for the ball in both x and y directions.
     *
     * @param ball The ball game object
     */
    private void setRandomBallVelocity(Ball ball) {
        double ballVelX = GameConstants.BALL_SPEED;
        double ballVelY = GameConstants.BALL_SPEED;
        Random rand = new Random();
        // randomly choosing the direction of the ball's velocity
        // Inverting the velocity component with 50% probability for both x and y
        if (rand.nextBoolean()) {
            ballVelX *= -1;
        }
        if (rand.nextBoolean()) {
            ballVelY *= -1;
        }
        ball.setVelocity((new Vector2((float) ballVelX, (float) ballVelY)));
    }

    /**
     * The main method to start the Bricker game.
     * It validates command-line arguments for brick columns and rows,
     * initializes the BrickerGameManager, and runs the game.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        int columns = BrickerGameManager.validateArgs(args)[0];
        int rows = BrickerGameManager.validateArgs(args)[1];
        BrickerGameManager game = new BrickerGameManager(BOUNCING_BALL,
                GameConstants.WINDOW_DIMENSIONS,
                columns, rows);
        game.run();

    }
}
