package com.mmobuilder.voxel.g3d;

import static com.mmobuilder.voxel.Constants.CHUNK_SIZE;
import static com.mmobuilder.voxel.Constants.SLASH;

public class ChunkSection {
    private final Block[][][] blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];

    Block getBlock(int localX, int localY, int localZ) {
        if (localX < 0 || localX > CHUNK_SIZE || localY < 0 || localY > CHUNK_SIZE || localZ < 0 || localZ > CHUNK_SIZE) {
            throw new RuntimeException("Coordinates out of bounds for this ChunkSection. Coordinates: " + localX + SLASH + localY + SLASH + localZ);
        }
        return blocks[localX][localY][localZ];
    }

    void setBlock(int localX, int localY, int localZ, Block block) {
        if (localX < 0 || localX > CHUNK_SIZE || localY < 0 || localY > CHUNK_SIZE || localZ < 0 || localZ > CHUNK_SIZE) {
            throw new RuntimeException("Coordinates out of bounds for this ChunkSection. Coordinates: " + localX + SLASH + localY + SLASH + localZ);
        }
        blocks[localX][localY][localZ] = block;
    }
}
