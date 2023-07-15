package com.mmobuilder.voxel;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.mmobuilder.voxel.Constants.*;

/**
 * This is the class that creates, manages, and disposes of all chunks.
 * The landmass is created here. See the example method below.
 */
@RequiredArgsConstructor
public class ChunkHandler extends ApplicationAdapter implements RenderableProvider {
    @Getter
    private final Map<Chunk.Key, Chunk> chunkHashMap = new HashMap<>();
    private final PerspectiveCamera camera;
    private final Random random = new Random();
    @Getter
    private ChunkMeshGenerator chunkMeshGenerator;
    private int currentChunkX;
    private int currentChunkZ;

    private Texture texture;


    @Override
    public void create() {
        // Get the texture info ready
        texture = new Texture(Gdx.files.internal("VR010_tex01.png"), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);

        chunkMeshGenerator = new ChunkMeshGenerator(this, texture);

        fillChunkData(98);
    }

    public void fillChunkData(int percentage) {
        if (percentage < 0 || percentage >= 99) throw new RuntimeException("Percentage must be between 0 and 98");

        ///// Populate chunk data /////////////
        // Loop through all chunks
        int blocksCreated = 0;
        for (int chunkX = 0; chunkX < WORLD_X_LENGTH; chunkX++) {
            for (int chunkZ = 0; chunkZ < WORLD_Z_LENGTH; chunkZ++) {
                for (int section = 0; section < VERTICAL_CHUNK_SECTIONS; section++) {

                    System.out.println("--- [ CHUNK: " + chunkX + SLASH + section + SLASH + chunkZ + " ] -----------------------------------------------------");

                    // Loop through each section
                    for (int x = 0; x < CHUNK_SIZE; x++) {
                        for (int z = 0; z < CHUNK_SIZE; z++) {
                            for (int y = 0; y < CHUNK_SIZE; y++) {

                                int rand = random.nextInt(0, 100);

                                int worldX = x + chunkX * CHUNK_SIZE;
                                int worldY = y + section * CHUNK_SIZE;
                                int worldZ = z + chunkZ * CHUNK_SIZE;

                                Block block = new Block(worldX, worldY, worldZ); // TODO: This should be local coordinates...?
                                block.setTexture(texture);
                                block.setMaterial(new Material(TextureAttribute.createDiffuse((texture))));
                                block.setBlockColor(Color.WHITE);
                                block.setBlockType(BlockType.SOLID);
                                if (rand < percentage) {
                                    block.setBlockType(BlockType.AIR);
                                }

                                System.out.println("[BLOCK " + blocksCreated + "] BlockType: " + block.getBlockType());
                                System.out.println("[World Coordinates] XYZ: " + worldX + SLASH + worldY + SLASH + worldZ);
                                System.out.println("[Local Coordinates] XYZ: " + x + SLASH + y + SLASH + z);
                                setBlock(worldX, worldY, worldZ, block);
                                blocksCreated++;
                            }
                        }
                    }
                }
            }
        }

        // Print chunk data debug
        for (Chunk chunk : chunkHashMap.values()) {
            System.out.println("[CHUNK DATA] " + chunk);
        }
    }

    Block getBlock(int worldX, int worldY, int worldZ) {
        if (worldY < 0 || worldY >= WORLD_HEIGHT) throw new RuntimeException("Y value is invalid. Value: " + worldY);

        int chunkX = Math.floorDiv(worldX, CHUNK_SIZE);
        int chunkZ = Math.floorDiv(worldZ, CHUNK_SIZE);
        Chunk chunk = getChunk(chunkX, chunkZ, false);

        if (chunk == null) return null; // No blocks in this chunk

        int localX = worldX - chunkX * CHUNK_SIZE;
        int localZ = worldZ - chunkZ * CHUNK_SIZE;
        return chunk.getBlock(localX, worldY, localZ);
    }

    void setBlock(int worldX, int worldY, int worldZ, Block block) {
        if (worldY < 0 || worldY >= WORLD_HEIGHT) throw new RuntimeException("Y value is invalid. Value: " + worldY);

        int chunkX = Math.floorDiv(worldX, CHUNK_SIZE);
        int chunkZ = Math.floorDiv(worldZ, CHUNK_SIZE);
        Chunk chunk = getChunk(chunkX, chunkZ, true);

        assert chunk != null;
        int localX = worldX - chunkX * CHUNK_SIZE;
        int localZ = worldZ - chunkZ * CHUNK_SIZE;
        chunk.setBlock(localX, worldY, localZ, block);
    }

    /**
     * Gets a world chunk.
     *
     * @param chunkX      The x location of the chunk. Do not use world coordinates here!
     * @param chunkZ      The z location of the chunk. Do not use world coordinates here!
     * @param createChunk if true, we will create a chunk if it doesn't exist.
     * @return A world chunk.
     */
    private Chunk getChunk(int chunkX, int chunkZ, boolean createChunk) {
        int hashCode = chunkX * 31 + chunkZ;
        for (Map.Entry<Chunk.Key, Chunk> chunkEntry : chunkHashMap.entrySet()) {
            Chunk.Key key = chunkEntry.getKey();
            Chunk chunk = chunkEntry.getValue();
            if (key.hashCode() == hashCode) return chunk;
        }

        if (!createChunk) return null;
        // No chunk exists, create a new one
        Chunk chunk = new Chunk(this, chunkX, chunkZ);
        System.out.println("[NEW CHUNK] Location: " + chunkX + SLASH + chunkZ);
        chunkHashMap.put(new Chunk.Key(chunkX, chunkZ), chunk);
        return chunk;
    }

    @Override
    public void dispose() {
        for (Chunk chunk : chunkHashMap.values()) {
            Model model = chunk.getModel();
            if (model != null) model.dispose();
        }
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {

//        for (Chunk chunk : chunkHashMap.values()) {
//            MeshWrapper meshWrapper = generateChunkModel(chunk);
//            meshList.add(meshWrapper);
//        }
//        System.out.println("Mesh Count: " + meshList.size());
//
//        for (Chunk chunk : chunkHashMap.values()) {
//            Renderable renderable = pool.obtain();
//            renderable.material = meshWrapper.getMaterial();
//            renderable.meshPart.mesh = meshWrapper.getMesh();
//            renderable.meshPart.offset = 0;
//            renderable.meshPart.size = meshWrapper.getMesh().getNumIndices();
//            renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
//            renderable.meshPart.center.set(CHUNK_SIZE / 2f, CHUNK_SIZE / 2f, CHUNK_SIZE / 2f);
//            renderables.add(renderable);
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
