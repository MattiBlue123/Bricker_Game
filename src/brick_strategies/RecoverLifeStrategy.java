package brick_strategies;

public class RecoverLifeStrategy extends BasicCollisionStrategy implements CollisionStrategy {
    public RecoverLifeStrategy(danogl.collisions.GameObjectCollection gameObjects, danogl.util.Counter brickCounter) {
        super(gameObjects, brickCounter);
    }

    @Override
    public void onCollision(danogl.GameObject thisObj, danogl.GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        // TODO: implement life recovery logic here
    }

}
