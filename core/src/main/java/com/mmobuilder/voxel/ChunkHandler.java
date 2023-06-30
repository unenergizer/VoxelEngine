package com.mmobuilder.voxel;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.mmobuilder.voxel.Constants.*;

/**
 * This is the class that creates, manages, and disposes of all chunks.
 * The landmass is created here. See the example method below.
 */
@RequiredArgsConstructor
public class ChunkHandler extends ApplicationAdapter implements RenderableProvider {
    private final ConcurrentMap<Chunk.Key, Chunk> chunkConcurrentMap = new ConcurrentHashMap<>();
    private final VoxelCube voxelCube = new VoxelCube();
    private final PerspectiveCamera camera;
    private int currentChunkX;
    private int currentChunkZ;

    @Override
    public void create() {
        // Get the texture info ready
        Texture texture = new Texture(Gdx.files.internal("dirt.png"), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);
        Color color = Color.WHITE;

        // Populate chunk data
        for (int chunkX = 0; chunkX < WORLD_X_LENGTH; chunkX++) {
            for (int chunkZ = 0; chunkZ < WORLD_Z_LENGTH; chunkZ++) {
                Chunk chunk = getChunk(chunkX, chunkZ, true);
                chunk.setTexture(texture);
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    for (int z = 0; z < CHUNK_SIZE; z++) {
                        for (int y = 0; y < CHUNK_SIZE; y++) {
                            Random random = new Random();
                            int rand = random.nextInt(0, 10);

                            Block block = new Block();
                            assert chunk != null;
                            chunk.getBlocks()[x][y][z] = block;
                            block.setX(x);
                            block.setY(y);
                            block.setZ(z);

                            if (rand < 6) {
                                block.setBlockType(BlockType.SOLID);
                            } else {
                                block.setBlockType(BlockType.AIR);
                            }
                        }
                    }
                }
            }
        }

        // Generate chunk
        for (int chunkX = 0; chunkX < WORLD_X_LENGTH; chunkX++) {
            for (int chunkZ = 0; chunkZ < WORLD_Z_LENGTH; chunkZ++) {
                Chunk chunk = getChunk(chunkX, chunkZ, true);
                Mesh mesh = voxelCube.generateChunkModel(texture, color, chunk);
                Objects.requireNonNull(chunk).setMesh(mesh);
            }
        }

        // Print chunk data debug
        for (Chunk chunk : chunkConcurrentMap.values()) {
            System.out.println("[CHUNK DATA] " + chunk);
        }
    }


    /**
     * Gets a world chunk.
     *
     * @param x           the x location of the chunk
     * @param z           the z location of the chunk
     * @param createChunk if true, we will create a chunk if it doesn't exist
     * @return A world chunk.
     */
    private Chunk getChunk(int x, int z, boolean createChunk) {
        int hashCode = x * 31 + z;
        for (Map.Entry<Chunk.Key, Chunk> chunkEntry : chunkConcurrentMap.entrySet()) {
            Chunk.Key key = chunkEntry.getKey();
            Chunk chunk = chunkEntry.getValue();
            if (key.hashCode() == hashCode) return chunk;
        }

        if (!createChunk) return null;
        // No chunk exists, create a new one
        Chunk chunk = new Chunk(x, z);
        System.out.println("[NEW CHUNK] Location: " + x + SLASH + z);
        chunkConcurrentMap.put(new Chunk.Key(x, z), chunk);
        return chunk;
    }

    @Override
    public void dispose() {
        for (Chunk chunk : chunkConcurrentMap.values()) {
            if (chunk != null && chunk.getMesh() != null) chunk.getMesh().dispose();
        }
    }


    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        if (chunkConcurrentMap.isEmpty()) return;
        for (Chunk chunk : chunkConcurrentMap.values()) {
            Renderable renderable = pool.obtain();
            renderable.material = chunk.getMaterial();
            renderable.meshPart.mesh = chunk.getMesh();
            renderable.meshPart.offset = 0;
            renderable.meshPart.size = chunk.getMesh().getNumVertices();
            renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
            renderables.add(renderable);
        }
    }

    /**
     * This is going to get the nearby chunks and only render those. This isn't the best way to do this
     * and this should only be considered a hack.
     */
    public void getNearbyChunks(ModelCache cache) {
//
//        int camX = (int) camera.position.x;
//        int camZ = (int) camera.position.z;
//        int chunkX = camX / CHUNK_SIZE;
//        int chunkZ = camZ / CHUNK_SIZE;
//
//        for (int x = chunkX - CHUNK_VIEW_RADIUS; x < chunkX + CHUNK_VIEW_RADIUS + TILE_SIZE; x++) {
//            for (int z = chunkZ - CHUNK_VIEW_RADIUS; z < chunkZ + CHUNK_VIEW_RADIUS + TILE_SIZE; z++) {
//                if (x < 0 || z < 0) continue; // No negative chunks or models instances exist here (in this project)...
//
//                Chunk chunk = getChunk(x, z, false);
//                if (chunk == null) continue;
//
//                ModelCache modelCache = chunk.getModelCache();
//                if (modelCache == null) continue;
//
//                cache.add(modelCache);
//            }
//        }
    }

    /**
     * Checks to see if the camera has moved out of their current chunk.
     *
     * @return True if the camera has moved to a new chunk, false otherwise.
     */
    public boolean hasLeftChunk() {
        int newChunkX = getChunkTileX();
        int newChunkZ = getChunkTileZ();

        if (currentChunkX == newChunkX && currentChunkZ == newChunkZ) return false;
        currentChunkX = newChunkX;
        currentChunkZ = newChunkZ;
        return true;
    }

    /**
     * Used for debug statements.
     */
    public int getCurrentChunkX() {
        return (int) camera.position.x / CHUNK_SIZE;
    }

    /**
     * Used for debug statements.
     */
    public int getCurrentChunkZ() {
        return (int) camera.position.z / CHUNK_SIZE;
    }

    /**
     * Used for debug statements.
     */
    public int getChunkTileX() {
        int camX = (int) camera.position.x;
        int chunkX = camX / CHUNK_SIZE;
        return camX - chunkX * CHUNK_SIZE;
    }

    /**
     * Used for debug statements.
     */
    public int getChunkTileZ() {
        int camZ = (int) camera.position.z;
        int chunkZ = camZ / CHUNK_SIZE;
        return camZ - chunkZ * CHUNK_SIZE;
    }
}
