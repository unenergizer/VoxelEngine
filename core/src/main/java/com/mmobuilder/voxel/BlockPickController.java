package com.mmobuilder.voxel;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.math.MathUtils.floor;

/**
 * Controller for selecting and interacting with blocks in the voxel world. This class extends {@link InputAdapter}
 * and handles mouse input events for the game.
 */
public class BlockPickController extends InputAdapter {

    /**
     * Maximum distance a block can be selected from.
     */
    private static final float MAX_LENGTH = 8.0f;
    /**
     * Increment size for ray casting.
     */
    private static final float STEP_SIZE = 0.05f;
    /**
     * Block coordinate outside the targeted block.
     */
    private final GridPoint3 outside = new GridPoint3();
    /**
     * Block coordinate inside the targeted block.
     */
    private final GridPoint3 inside = new GridPoint3();
    /**
     * Current position during ray casting.
     */
    private final Vector3 position = new Vector3();
    /**
     * Normalized direction of the camera.
     */
    private final Vector3 normalizedDirection = new Vector3();
    /**
     * Offset applied during ray casting.
     */
    private final GridPoint3 offset = new GridPoint3();
    /**
     * Main game instance.
     */
    private final Main main;
    /**
     * Camera instance for player view.
     */
    private final Camera camera;
    /**
     * Handler for all chunks in the game world.
     */
    private final ChunkHandler chunkHandler;

    /**
     * Creates a BlockPickController.
     *
     * @param main         The main game instance.
     * @param chunkHandler The handler for all chunks in the game world.
     * @param camera       The camera instance for player view.
     */
    public BlockPickController(Main main, ChunkHandler chunkHandler, Camera camera) {
        this.main = main;
        this.chunkHandler = chunkHandler;
        this.camera = camera;
    }

    /**
     * Called when a mouse button is pressed.
     *
     * @param screenX The x coordinate of the mouse click.
     * @param screenY The y coordinate of the mouse click.
     * @param pointer The pointer index.
     * @param button  The button index.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.LEFT || button == Input.Buttons.RIGHT) return updateBlocks(button);
        return false;
    }

    /**
     * Breaks or places a block depending on the mouse button pressed.
     *
     * @param button The index of the mouse button.
     * @return True if a block was broken or placed, false otherwise.
     */
    public boolean updateBlocks(int button) {
        // Checks if the raycast hits a block.
        if (!rayCastSuccess()) return false;

        // If left mouse button is clicked, break the block.
        if (button == Input.Buttons.LEFT) {
            Block block = chunkHandler.getBlock(inside.x, inside.y, inside.z);
            if (block == null) return false;
            block.setBlockType(BlockType.AIR);
        }

        // If right mouse button is clicked, place a block.
        if (button == Input.Buttons.RIGHT) {
            Block block = chunkHandler.getBlock(outside.x, outside.y, outside.z);
            if (block == null) return false;
            block.setBlockType(BlockType.SOLID);
        }

        // Update chunks after breaking or placing a block.
        main.updateModelInstanceList(true);
        return true;
    }

    /**
     * Casts a ray from the player's position along the direction the player is facing. This is used to
     * determine which block the player is looking at.
     *
     * @return True if a block was hit by the ray, false otherwise.
     */
    private boolean rayCastSuccess() {
        // Normalize and scale the direction of the camera.
        normalizedDirection.set(camera.direction).nor().scl(STEP_SIZE);

        final Vector3 cameraPosition = camera.position;

        // Calculate the offset for each block.
        offset.set(floor(cameraPosition.x), floor(cameraPosition.y), floor(cameraPosition.z));

        // Set the position to the current camera position.
        position.set(cameraPosition).sub(offset.x, offset.y, offset.z);

        int currentX, currentY, currentZ; // Current integer position.
        int lastX = 0, lastY = 0, lastZ = 0; // Last integer position.

        // Cast the ray until it hits a block or exceeds the max length.
        for (float i = 0; i < MAX_LENGTH; i += STEP_SIZE) {
            position.add(normalizedDirection);
            currentX = floor(position.x);
            currentY = floor(position.y);
            currentZ = floor(position.z);

            // If the position hasn't changed since the last step, continue.
            if (currentX == lastX && currentY == lastY && currentZ == lastZ) continue;

            // Calculate the real-world position of the block.
            int blockX = currentX + offset.x;
            int blockY = currentY + offset.y;
            int blockZ = currentZ + offset.z;

            // If the block is outside the world boundaries, continue.
            if (BlockUtil.isBlockOutsideWorldY(blockY)) continue;

            // Get the block at the calculated position.
            Block block = chunkHandler.getBlock(blockX, blockY, blockZ);

            // If there is no block, or the block is air, continue.
            if (block == null || block.getBlockType() == BlockType.AIR) {
                lastX = currentX;
                lastY = currentY;
                lastZ = currentZ;
                continue;
            }

            // Set the inside and outside positions.
            inside.set(currentX, currentY, currentZ).add(offset);
            outside.set(lastX, lastY, lastZ).add(offset);

            // A solid block was hit by the ray.
            return true;
        }

        // No solid block was hit by the ray.
        return false;
    }
}

