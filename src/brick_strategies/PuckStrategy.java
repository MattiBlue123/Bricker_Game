package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.Ball;
import gameobjects.Puck;

import java.util.Random;

public class PuckStrategy extends BasicCollisionStrategy{
    private static final String PUCK_IMAGE= "assets/mockBall.png";
    private static final String PUCK_SOUND= "assets/blop.wav";
    private static final float PUCK_RATIO_OF_BALL= 0.75f ;
    private static final float PUCK_SPEED= 200f;

    private final ImageRenderable puckImage;
    private final Sound collisionSound;
    private final float puckSize;
    private final Vector2 windowDimensions;


    public PuckStrategy(GameObjectCollection gameObjects, ImageReader imageReader, SoundReader soundReader,
                        Counter brickCounter,Vector2 windowDimensions, float puckSize) {
        super(gameObjects, brickCounter);
        this.windowDimensions=windowDimensions;
        this.puckImage = imageReader.readImage(PUCK_IMAGE, true);
        this.collisionSound = soundReader.readSound(PUCK_SOUND);
        this.puckSize = puckSize * PUCK_RATIO_OF_BALL;
    }



@Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);

        Vector2 brickCenter = thisObj.getCenter();
        Vector2 puckDimensions = new Vector2(this.puckSize, this.puckSize);
        Vector2 topLeftOfPuck = brickCenter.subtract(puckDimensions.mult(0.5f));

        for (int i = 0; i < 2; i++) {
            addPuck(topLeftOfPuck,puckDimensions);
        }
    }

    private void addPuck(Vector2 topLeftOfPuck,Vector2 puckDimensions) {

        Puck puck = new Puck(topLeftOfPuck, puckDimensions,this.puckImage,this.collisionSound,
                windowDimensions,gameObjects);
        puck.setVelocity(randomVelocityUpper());
        super.gameObjects.addGameObject(puck, Layer.DEFAULT);
    }

    private Vector2 randomVelocityUpper(){
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI;
        float velocityX = (float)Math.cos(angle) * PUCK_SPEED;
        float velocityY = (float)Math.sin(angle) * PUCK_SPEED;
        return new Vector2(-velocityX, -velocityY); //TODO ADDED MINUS
    }
}
