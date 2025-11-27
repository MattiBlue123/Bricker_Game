package bricker.gameobjects;

import bricker.main.GameConstants;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A class representing a ball in the game.
 * Extends the GameObject class and includes collision handling with sound effect.
 * The parent class for all ball-like objects in the game - the standard ball and the puck.
 *
 * @author Amit Tzur and Zohar Mattatia
 */
public class Ball extends GameObject {
    //============================ fields =========================== //
    /* Sound effect for ball collision */
    private final Sound collisionSound;

    /**
     * Constructor for Ball.
     *
     * @param topLeftCorner  The top-left corner position of the ball.
     * @param renderable     The visual representation of the ball.
     * @param collisionSound The sound effect to play upon collision.
     */
    public Ball(Vector2 topLeftCorner,
                Renderable renderable,
                Sound collisionSound) {
        super(topLeftCorner, GameConstants.BALL_DIMENSIONS, renderable);
        this.collisionSound = collisionSound;
        // Set the ball tag for identification when hitting the extra paddle
        this.setTag(GameConstants.BALL_TAG);
    }

    /**
     * Handles the collision event by reflecting the ball's velocity and playing a sound effect.
     * Overrides the onCollisionEnter method from GameObject.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
    }

}
