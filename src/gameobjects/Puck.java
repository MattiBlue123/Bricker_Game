package gameobjects;

import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Puck extends Ball {
    private final Vector2 puckDimensions;
    private final Vector2 windowDimensions;
    private final GameObjectCollection gameObjects;
    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound,
                Vector2 windowDimensions, GameObjectCollection gameObjects) {

        super(topLeftCorner, dimensions, renderable, collisionSound);
        this.puckDimensions = dimensions;
        this.windowDimensions = windowDimensions;
        this.gameObjects = gameObjects;
    }
    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        if (getCenter().y()> windowDimensions.y()+ puckDimensions.y()){
            gameObjects.removeGameObject(this);
        }
    }

}
