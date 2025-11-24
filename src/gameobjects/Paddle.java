package gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.event.KeyEvent;

public class Paddle extends GameObject {

    private static final float MOVEMENT_SPEED = 600;
    private final UserInputListener inputListener;
    private final float width;

    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener inputListener, float width) {
        // i added float width in the constructor
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.width=width;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir=Vector2.ZERO;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            movementDir = movementDir.add(Vector2.LEFT);

        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
        //TODO- add limits to movement of paddle
        boolean didReachLeftEdge=this.getTopLeftCorner().x() <= 0 ;
        boolean didReachRightEdge = this.getTopLeftCorner().x() >= this.width-this.getDimensions().x();
        if (didReachLeftEdge && movementDir.x()<0  || didReachRightEdge && movementDir.x()>0){
            setVelocity(Vector2.ZERO);
        }

    }





}
