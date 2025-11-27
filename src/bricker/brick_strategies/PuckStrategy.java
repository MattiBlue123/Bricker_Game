package bricker.brick_strategies;

import bricker.main.GameConstants;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.Puck;

import java.util.Random;

/**
 * A collision strategy that spawns pucks upon brick collision.
 * Extends the BasicCollisionStrategy.
 *
 * @author Zohar Mattatia and Amit Tzur
 */
public class PuckStrategy extends BasicCollisionStrategy implements CollisionStrategy {
    // =========================== private constants =========================== //
    /* Path to the puck image asset */
    private static final String PUCK_IMAGE = "assets/mockBall.png";
    /* Size of the puck */
    private static final float PUCK_SIZE = GameConstants.BALL_SIZE * 0.75f;
    /* Number of pucks to spawn upon collision */
    private static final int NUMBER_OF_PUCKS = 2;

    // =========================== fields =========================== ///
    /* Image for the puck */
    private final Renderable puckImage;
    /* Sound effect for puck collision */
    private final Sound collisionSound;


    /**
     * Constructor for PuckStrategy.
     * Here we initialize the puck image, collision sound, and puck size.
     *
     * @param gameObjects  The collection of game objects in the game.
     * @param brickCounter Counter to keep track of remaining bricks.
     * @param imageReader  ImageReader for loading images.
     * @param soundReader  SoundReader for loading sounds.
     */
    public PuckStrategy(GameObjectCollection gameObjects,
                        Counter brickCounter,
                        ImageReader imageReader,
                        SoundReader soundReader) {
        super(gameObjects, brickCounter);
        this.puckImage = imageReader.readImage(PUCK_IMAGE, true);
        this.collisionSound = soundReader.readSound(GameConstants.BALL_COLLISION_SOUND);
    }

    /**
     * Handles the collision event by spawning pucks at the brick's location.
     *
     * @param firstObject  The brick game object that was collided with.
     * @param secondObject The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject firstObject, GameObject secondObject) {
        super.onCollision(firstObject, secondObject);

        Vector2 brickCenter = firstObject.getCenter();
        Vector2 puckDimensions = new Vector2(PUCK_SIZE, PUCK_SIZE);
        Vector2 topLeftOfPuck =
                brickCenter.subtract(puckDimensions.mult(GameConstants.HALF_FACTOR));

        for (int i = 0; i < NUMBER_OF_PUCKS; i++) {
            addPuck(topLeftOfPuck);
        }
    }

    /**
     * Adds a puck to the game at the specified position with random velocity.
     *
     * @param topLeftOfPuck The top-left position where the puck will be placed.
     */
    private void addPuck(Vector2 topLeftOfPuck) {

        Puck puck = new Puck(topLeftOfPuck, puckImage, collisionSound, gameObjects);
        puck.setVelocity(randomVelocityUpper());
        super.gameObjects.addGameObject(puck, Layer.DEFAULT);
    }

    /**
     * Generates a random velocity vector for the puck in the upper half-plane.
     *
     * @return A Vector2 representing the random velocity.
     */
    private Vector2 randomVelocityUpper() {
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI;
        // Calculate velocity components based on the angle, which should be upwards
        float velocityX = (float) Math.cos(angle) * GameConstants.BALL_SPEED;
        float velocityY = (float) Math.sin(angle) * GameConstants.BALL_SPEED;
        // Math.sin(angle) in our range (0..π) is non-negative,
        // so we must negate the Y component to make the puck go up
        return new Vector2(velocityX, -velocityY);
    }
}
