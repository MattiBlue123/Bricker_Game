package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.Brick;

/**
 * A collision strategy that causes a brick to explode,
 * affecting its neighboring bricks upon collision.
 * Extends the BasicCollisionStrategy.
 *
 * @author Amit Tzur and Zohar Mattatia
 */
public class ExplodingBrickStrategy extends BasicCollisionStrategy implements CollisionStrategy {
    // =========================== private constants =========================== //
    /* Path to the explosion sound asset */
    private static final String EXPLOSION_SOUND = "assets/explosion.wav";
    // =========================== fields =========================== //
    /* Sound effect for the explosion */
    private final Sound explosionSound;
    /* 2D array representing the grid of bricks */
    private final Brick[][] bricksGrid;

    /**
     * Constructor for ExplodingBrickStrategy.
     * Here we initialize the explosion sound and the bricks grid.
     *
     * @param gameObjects  The collection of game objects in the game.
     * @param brickCounter Counter to keep track of remaining bricks.
     * @param bricksGrid   2D array representing the grid of bricks.
     * @param soundReader  SoundReader for loading sounds.
     */
    ExplodingBrickStrategy(GameObjectCollection gameObjects,
                           Counter brickCounter,
                           Brick[][] bricksGrid,
                           SoundReader soundReader) {

        super(gameObjects, brickCounter);
        this.explosionSound = soundReader.readSound(EXPLOSION_SOUND);
        this.bricksGrid = bricksGrid;
    }

    /**
     * Handles the collision event by causing the brick to explode,
     * affecting its neighboring bricks.
     *
     * @param firstObject  The brick game object that was collided with.
     * @param secondObject The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject firstObject, GameObject secondObject) {
        // handling current brick explosion
        super.onCollision(firstObject, secondObject);
        explosionSound.play();

        // handling neighboring bricks explosion -
        // THIS IS ACCORDING TO THE ASSIGNMENT DESCRIPTION
        if (!(firstObject instanceof Brick)) return;

        // get the brick's coordinates in the grid
        Vector2 brickCoordinates = ((Brick) firstObject).getBrickCoordinateInBricksGrid();
        int row = (int) brickCoordinates.x();
        int col = (int) brickCoordinates.y();

        //mark that our brick is exploded
        if (bricksGrid[row][col] != null) {
            bricksGrid[row][col] = null;
        } else {
            return; //if already processed in another chain, we do nothing.
        }

        // check all four neighboring bricks (up, down, left, right)
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // if the neighbor brick exists, trigger its collision
            if (isValidCoordinate(newRow, newCol)) {
                Brick neighborBrick = bricksGrid[newRow][newCol];
                if (neighborBrick != null) {
                    neighborBrick.onCollisionEnter(firstObject, null);
                }
            }
        }
    }

    /**
     * Helper method to check if the given coordinates are valid in the bricks grid.
     *
     * @param row The row index to check.
     * @param col The column index to check.
     * @return True if the coordinates are valid, false otherwise.
     */
    private boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < bricksGrid.length && col >= 0 && col < bricksGrid[0].length;
    }
}


