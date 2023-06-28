package com.mmobuilder.voxel;

/**
 * Constants here can be adjusted to changes various things about the game world.
 */
public class Constants {
    /**
     * The maximum number of chunks the camera can see at once.
     */
    public static final int CHUNK_VIEW_RADIUS = 6;
    /**
     * The X length of the world in chunks.
     */
    public static final int WORLD_X_LENGTH = 16;
    /**
     * The Z length of the world in chunks.
     */
    public static final int WORLD_Z_LENGTH = WORLD_X_LENGTH;
    /**
     * The length and width of a chunk. Min value 2.
     */
    public static final int CHUNK_SIZE = 8;
    /**
     * The length and width of a tile in meters.
     */
    public static final float TILE_SIZE = 1f;
    /**
     * A string used by the node system for naming nodes.
     */
    public static final String SLASH = "/";
}
