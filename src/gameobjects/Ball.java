package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Ball extends GameObject {
    private int collisonCounter;
    private Sound collisionSound;

    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisonCounter = 0;
        this.collisionSound=collisionSound;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel= getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        this.collisonCounter++;
        collisionSound.play();
//        System.out.println("Ball collison counter: "+this.collisonCounter);
    }

    public int getCollisonCounter() {
        return collisonCounter;
    }
}
