package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.Brick;

public class ExplodingBrickStrategy extends BasicCollisionStrategy {
    private static final String EXPLOSION_SOUND = "assets/explosion.wav";
    private final Sound explosionSound;
    private final Brick[][] bricksGrid;

    ExplodingBrickStrategy(GameObjectCollection gameObjects, Counter brickCounter, Brick[][] bricksGrid
            , SoundReader soundReader) {

        super(gameObjects, brickCounter);
        this.explosionSound = soundReader.readSound(EXPLOSION_SOUND);
        this.bricksGrid = bricksGrid;

    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        //handling current brick explosion
        super.onCollision(thisObj, otherObj);
        explosionSound.play();


        if (!(thisObj instanceof Brick)) return; //TODO IM NOT SURE IF THATS OK, BUT FOR SAFTEY

        Vector2 brickCoordinates = ((Brick) thisObj).getBrickCoordinateInBricksGrid();
        int row = (int) brickCoordinates.x();
        int col = (int) brickCoordinates.y();


        //mark that our brick is exploded
        if (bricksGrid[row][col] != null) {
            bricksGrid[row][col] = null;
        } else {
            return; //if already processed in another chain, we do nothing.
        }

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (isValidCoordinate(newRow, newCol)) {
                Brick neighborBrick = bricksGrid[newRow][newCol];
                if (neighborBrick != null) {
                    neighborBrick.onCollisionEnter(thisObj, null);
                }
            }
        }
    }

        private boolean isValidCoordinate ( int row, int col){
            return row >= 0 && row < bricksGrid.length && col >= 0 && col < bricksGrid[0].length;
        }
    }


