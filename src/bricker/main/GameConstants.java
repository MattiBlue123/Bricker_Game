package bricker.main;

import danogl.util.Vector2;

/**
 * A class containing constant values used throughout the game.
 * Only constants that are used in multiple classes were placed here.
 * This includes asset paths, game object dimensions, tags, and other configuration values.
 *
 * @author Amit Tzur and Zohar Mattatia
 */
public class GameConstants {

    /* blop sound asset path */
    public static final String BALL_COLLISION_SOUND = "assets/blop.wav";
    /* path to the paddle image asset */
    public static final String PADDLE_IMAGE = "assets/paddle.png";
    /* path to the falling hearts and remaining lives image asset */
    public static final String HEART_IMAGE_PATH = "assets/heart.png";
    /* tag for the main paddle */
    public static final String MAIN_PADDLE_TAG = "MAIN_PADDLE";
    /* tag for the ball */
    public static final String BALL_TAG = "ball";
    /* heart width - used by RecoverLifeStrategy and LivesManager */
    public static final float HEART_WIDTH = 30;
    /* heart height - used by RecoverLifeStrategy and LivesManager */
    public static final float HEART_HEIGHT = 30;
    /* standard padding - space between bricks,
     also used to space the hearts in the remaining lives GUI */
    public static final float STANDARD_PADDING = 10;
    /* windows width - to avoid calculating it multiple times */
    public static final float WINDOW_WIDTH = 700;
    /* windows height - to avoid calculating it multiple times */
    public static final float WINDOW_HEIGHT = 500;
    /* half factor - used to calculate center positions */
    public static final float HALF_FACTOR = 0.5f;
    /* ball speed - used to set the initial ball velocity */
    public static final float BALL_SPEED = 200;
    /* ball size - width and height */
    public static final float BALL_SIZE = 20;
    /* paddle width */
    public static final float PADDLE_WIDTH = 100;
    /* paddle height - it's thickness */
    public static final float PADDLE_HEIGHT = 15;

    /* heart dimensions vector - used by RecoverLifeStrategy
    with parameters that used to be ONLY in LivesManager */
    public static final Vector2 HEART_DIMENSIONS = new Vector2(HEART_WIDTH, HEART_HEIGHT);
    /* window dimensions vector - to avoid creating it multiple times */
    public static final Vector2 WINDOW_DIMENSIONS = new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT);
    /* ball dimensions vector - width and height are equal */
    public static final Vector2 BALL_DIMENSIONS = new Vector2(BALL_SIZE, BALL_SIZE);
    /* paddle dimensions vector - width and height */
    public static final Vector2 PADDLE_DIMENSIONS = new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT);


}
