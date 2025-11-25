package bricker.main;

import brick_strategies.BasicCollisionStrategy;
import brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.Ball;
import gameobjects.Brick;
import gameobjects.Lives;
import gameobjects.Paddle;
// heyyy whats up?
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class BrickerGameManager extends GameManager {
    public static final float BRICK_PADDING = 10;
    public static final float WINDOW_WIDTH = 700;
    public static final float WINDOW_HEIGHT = 500;
    private static final String PLAY_AGAIN = " Play again?";
    private static final String YOU_WIN = "You win!";
    private static final String YOU_LOSE = "You Lose!";
    private static final float BALL_SPEED = 200;
    private static final float BALL_SIZE = 20;
    private static final float PADDLE_WIDTH = 100;
    private static final float PADDLE_HEIGHT = 15;
    private static final float PADDLE_OFFSET_FROM_BOTTOM = 30;
    private static final float HALF_FACTOR = 0.5f;
    private static final float LEFT_WALL_WIDTH = 15;
    private static final float LEFT_WALL_HEIGHT = 500;
    private static final float RIGHT_WALL_WIDTH = 15;
    private static final float RIGHT_WALL_HEIGHT = 500;
    private static final float TOP_WALL_WIDTH = 700;
    private static final float TOP_WALL_HEIGHT = 15;
    private static final float BRICK_HEIGHT = 15;
    private static final float[][] WALL_DIMENSIONS = {
            {LEFT_WALL_WIDTH, LEFT_WALL_HEIGHT},
            {RIGHT_WALL_WIDTH, RIGHT_WALL_HEIGHT},
            {TOP_WALL_WIDTH, TOP_WALL_HEIGHT}
    };
    private static final Vector2[] WALL_POSITIONS = {
            Vector2.ZERO,
            new Vector2(WINDOW_WIDTH - RIGHT_WALL_WIDTH+1, 0),
            Vector2.ZERO
    };
    private static final int DEFAULT_BRICK_ROWS=7;
    private static final int DEFAULT_BRICK_COLUMNS=8;
    public static final String ASSETS_BALL_PNG = "assets/ball.png";
    public static final String ASSETS_BLOP_WAV = "assets/blop.wav";
    public static final String ASSETS_PADDLE_PNG = "assets/paddle.png";
    public static final String ASSETS_DARK_BG_2_SMALL_JPEG = "assets/DARK_BG2_small.jpeg";
    public static final String ASSETS_BRICK_PNG = "assets/brick.png";
    public static final String BOUNCING_BALL = "Bouncing Ball";
    //    private static final int DEFAULT_BRICK_ROWS=1;
//    private static final int DEFAULT_BRICK_COLUMNS=1;
    private final int brickColumns;
    private final int brickRows;
    private Counter brickCounter;
    private Lives lives;
    private Ball ball;
    private WindowController windowController;
    private Vector2 windowDimensions;

    private static final int MAX_LIVES = 4;
    private static final Vector2 LIVES_POSITION = new Vector2(20, 450);
    private UserInputListener inputListener;

    // we notice that in this DanoGameLab there is a default constructor, we will do a regular
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions,int brickColumns, int brickRows) {
        super(windowTitle, windowDimensions);
        this.brickColumns=brickColumns;
        this.brickRows=brickRows;
        this.brickCounter = new Counter(brickRows * brickColumns);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.inputListener = inputListener;
        windowDimensions= windowController.getWindowDimensions();
        this.brickCounter = new Counter(brickRows * brickColumns);
        createBall(imageReader,soundReader,windowController);
        createPaddle(imageReader,windowController, inputListener);
        createWall(imageReader,windowController);
        createBackground(imageReader, windowController);
        createBricker(imageReader,windowController,new BasicCollisionStrategy(this.gameObjects(),this.brickCounter));
        createLives(imageReader); //TODO- ADD
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (inputListener.isKeyPressed(KeyEvent.VK_W)) {
            brickCounter.reset();
        }
        checkForGameEnd();
    }

    private void checkForGameEnd() {
        double ballHeight = ball.getCenter().y();
        String prompt = "";

        // checking if a ball fell
        if (ballHeight > windowDimensions.y()) {
            this.lives.removeLife();
            int currentLives = this.lives.getLives();

            //checking if we lost because the ball fell, and we have no lives left
            if (currentLives > 0) {
                ball.setCenter(windowDimensions.mult(HALF_FACTOR));
            }
            else {
                prompt = YOU_LOSE;
            }
        }
        // checking if all brick are removed
        else if (brickCounter.value() <= 0) {
            prompt = YOU_WIN;
        }

        //handling the dialog yes/no window
        if(!prompt.isEmpty()) {
            prompt += PLAY_AGAIN;
            if(windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }
    }

    private void createBall(ImageReader imageReader, SoundReader soundReader, WindowController windowController){
        //creating ball
        Renderable ballImage= imageReader.readImage(ASSETS_BALL_PNG,true);
        // isTopLeft above defines if we want the left top pixel to be in same color of window
        Sound collisionSound=soundReader.readSound(ASSETS_BLOP_WAV);
        GameObject ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE,BALL_SIZE),ballImage,collisionSound);
        ball.setVelocity(Vector2.DOWN.mult(BALL_SPEED)); //Down is (0,1)
        Vector2 windowDimensions= windowController.getWindowDimensions();
        ball.setCenter(windowDimensions.mult(HALF_FACTOR));
        this.gameObjects().addGameObject(ball,Layer.DEFAULT);
        this.ball = (Ball) ball;

        double ballVelX=BALL_SPEED;
        double ballVelY=BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()){
            ballVelX *=-1;
        }
        if(rand.nextBoolean()){
            ballVelY *=-1;
        }
        ball.setVelocity((new Vector2((float) ballVelX, (float) ballVelY)));

    }

    private void createPaddle(ImageReader imageReader,
                              WindowController windowController,
                              UserInputListener inputListener){
        // initializing local variables
        Renderable paddleImage= imageReader.readImage(ASSETS_PADDLE_PNG,true);
        Vector2 windowDimensions= windowController.getWindowDimensions();
        // creating user paddle
        GameObject userPaddle = new Paddle(Vector2.ZERO,
                new Vector2(PADDLE_WIDTH,PADDLE_HEIGHT),
                paddleImage,
                inputListener
        ,windowDimensions.x());
        userPaddle.setCenter(new Vector2(windowDimensions.x()*HALF_FACTOR , windowDimensions.y()-PADDLE_OFFSET_FROM_BOTTOM));
        this.gameObjects().addGameObject(userPaddle,Layer.DEFAULT);

    }

    private void createWall(ImageReader imageReader,WindowController windowController){
        for (int i=0;i<WALL_POSITIONS.length;i++) {
            float x= WALL_POSITIONS[i].x();
            float y= WALL_POSITIONS[i].y();
            float width= WALL_DIMENSIONS[i][0];
            float height= WALL_DIMENSIONS[i][1];
            GameObject wall = new GameObject(new Vector2(x,y), new Vector2(width, height), new RectangleRenderable(Color.DARK_GRAY));
            this.gameObjects().addGameObject(wall,Layer.STATIC_OBJECTS);
        }
    }

    private void createBackground(ImageReader imageReader, WindowController windowController) {
        Renderable backgroundImage = imageReader.readImage(ASSETS_DARK_BG_2_SMALL_JPEG, false);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions, backgroundImage);
        this.gameObjects().addGameObject(background, Layer.BACKGROUND); // Add with the lowest layer to ensure it's behind everything
//        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

    }

    private void createBricker(ImageReader imageReader, WindowController windowController, CollisionStrategy collisionStrategy) {
        float brickWidth=(WINDOW_WIDTH-(BRICK_PADDING*(brickColumns+1))-LEFT_WALL_WIDTH-RIGHT_WALL_WIDTH) / this.brickColumns;
        Renderable brickerImage = imageReader.readImage(ASSETS_BRICK_PNG,false);
        Vector2 brickDimensions = new Vector2(brickWidth,BRICK_HEIGHT);
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickColumns; j++) {
                float x = (j * brickWidth)+(BRICK_PADDING*(j+1))+LEFT_WALL_WIDTH;
                float y = (i *BRICK_HEIGHT)+(BRICK_PADDING*(i+1)+ TOP_WALL_HEIGHT);
                Vector2 brickPosition= new Vector2(x,y);
                GameObject brick = new Brick(brickPosition,brickDimensions,brickerImage,collisionStrategy);
                this.gameObjects().addGameObject(brick,Layer.STATIC_OBJECTS);
            }
        }

    }

    private void createLives(ImageReader imageReader) {
        this.lives = new Lives(this.gameObjects(), LIVES_POSITION, MAX_LIVES, imageReader);
    }

    public void removeBrick(GameObject brick) {
        if (this.gameObjects().removeGameObject(brick, Layer.STATIC_OBJECTS)) {
            brickCounter.decrement();
        }

    }

    private static int[] validateArgs(String[] args) {
        int columns = DEFAULT_BRICK_COLUMNS;
        int rows = DEFAULT_BRICK_ROWS;
        if (args.length >= 2){
            columns = Integer.parseInt(args[0]);
            rows = Integer.parseInt(args[1]);
        }
        return new int[]{columns, rows};
    }

    public static void main(String[] args) {
        int columns = BrickerGameManager.validateArgs(args)[0];
        int rows = BrickerGameManager.validateArgs(args)[1];
        BrickerGameManager game = new BrickerGameManager(BOUNCING_BALL, new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT), columns,rows);
        game.run();


    }
}
