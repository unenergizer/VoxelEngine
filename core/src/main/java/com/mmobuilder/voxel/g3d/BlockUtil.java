package com.mmobuilder.voxel.g3d;

import static com.mmobuilder.voxel.Constants.WORLD_HEIGHT;

public class BlockUtil {

    /**
     * Checks if a given block's y-coordinate is outside the world's vertical boundaries.
     *
     * @param y The y-coordinate of the block to check.
     * @return True if the y-coordinate is less than 0 (below the world) or greater or equal to WORLD_HEIGHT
     * (above the world). Otherwise, returns false.
     */
    public static boolean isBlockOutsideWorldY(int y) {
        return y < 0 || y >= WORLD_HEIGHT;
    }

}
