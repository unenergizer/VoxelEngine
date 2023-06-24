package com.mmobuilder.voxel;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.StringBuilder;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.mmobuilder.voxel.Constants.*;

/**
 * This is the class that creates, manages, and disposes of all chunks.
 * The landmass is created here. See the example method below.
 */
@RequiredArgsConstructor
public class ChunkHandler extends ApplicationAdapter {
    private static final int CUBE_FACES = 6;
    /**
     * A quad has 4 vertices, one at each corner
     */
    private static final int QUAD_VERTICES = 4;
    private final ConcurrentMap<Chunk.Key, Chunk> chunkConcurrentMap = new ConcurrentHashMap<>();
    private final VoxelCube voxelCube = new VoxelCube();
    private final StringBuilder stringBuilder;
    private final ModelBuilder modelBuilder;
    private final PerspectiveCamera camera;

    private int currentChunkX;
    private int currentChunkZ;

    @Override
    public void create() {
        // Get the texture info ready
        Texture texture = new Texture(Gdx.files.internal("dirt.png"));
        Color color = Color.GRAY;

        // Generate chunk
        for (int chunkX = 0; chunkX < WORLD_X_LENGTH; chunkX++) {
            for (int chunkZ = 0; chunkZ < WORLD_Z_LENGTH; chunkZ++) {
                Chunk chunk = getChunk(chunkX, chunkZ, true);
                Model model = generateChunkModel(texture, color, chunkX, chunkZ);
                Objects.requireNonNull(chunk).setModel(model);
            }
        }

        // Print chunk data debug
        for (Chunk chunk : chunkConcurrentMap.values()) {
            System.out.println("[CHUNK DATA] " + chunk);
        }

//        exampleModifyChunkTile();
    }

    /**
     * Generates a chunk landscape model.
     *
     * @param texture The texture to paint on this model.
     * @param color   The color we want to apply to the texture.
     * @param chunkX  The X location of this chunk.
     * @param chunkZ  The Z location of this chunk.
     * @return A model that represents a landscape.
     */
    @SuppressWarnings("PointlessArithmeticExpression")
    private Model generateChunkModel(Texture texture, Color color, int chunkX, int chunkZ) {
        // Define the attributes for this model
        VertexAttribute position = new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE);
        VertexAttribute colorUnpacked = new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, 4, ShaderProgram.COLOR_ATTRIBUTE);
        VertexAttribute textureCoordinates = new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0");

        // Init vertices array
        float[] vertices = new float[((position.numComponents + colorUnpacked.numComponents + textureCoordinates.numComponents) * QUAD_VERTICES * CHUNK_SIZE * CHUNK_SIZE) * CUBE_FACES];

        // Populate the vertices array with data
        int vertexOffset = 0;
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                int y = 0;
                vertexOffset = voxelCube.createTop(vertices, vertexOffset, x, z, y, color, new TextureRegion(texture));
                vertexOffset = voxelCube.createBottom(vertices, vertexOffset, x, z, y, color, new TextureRegion(texture));
                vertexOffset = voxelCube.createLeft(vertices, vertexOffset, x, z, y, color, new TextureRegion(texture));
                vertexOffset = voxelCube.createRight(vertices, vertexOffset, x, z, y, color, new TextureRegion(texture));
                vertexOffset = voxelCube.createFront(vertices, vertexOffset, x, z, y, color, new TextureRegion(texture));
                vertexOffset = voxelCube.createBack(vertices, vertexOffset, x, z, y, color, new TextureRegion(texture));
            }
        }

        // Generate the indices
        int size = (6 * CHUNK_SIZE * CHUNK_SIZE) * CUBE_FACES;
        short[] indices = new short[size];
        short j = 0;
        for (int i = 0; i < indices.length; i += 6, j += 4) {
            indices[i + 0] = (short) (j + 2);
            indices[i + 1] = (short) (j + 1);
            indices[i + 2] = (short) (j + 3);
            indices[i + 3] = (short) (j + 0);
            indices[i + 4] = (short) (j + 3);
            indices[i + 5] = (short) (j + 1);
        }

        // Create the mesh
        Mesh mesh = new Mesh(true, vertices.length, indices.length, position, colorUnpacked, textureCoordinates);
        mesh.setVertices(vertices);
        mesh.setIndices(indices);

        // Create the MeshPart I'd
        stringBuilder.append(chunkX);
        stringBuilder.append(SLASH);
        stringBuilder.append(chunkZ);

        // Create the MeshPart
        MeshPart meshPart = new MeshPart(stringBuilder.toStringAndClear(), mesh, 0, size, GL30.GL_TRIANGLES);

        // Create a model out of the MeshPart
        modelBuilder.begin();
        modelBuilder.part(meshPart, new Material("texture", TextureAttribute.createDiffuse(texture)));
        return modelBuilder.end();
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
            if (chunk != null && chunk.getModel() != null) chunk.getModel().dispose();
        }
    }

    /**
     * This is going to get the nearby chunks and only render those. This isn't the best way to do this
     * and this should only be considered a hack.
     */
    public void getNearbyChunks(ModelCache cache) {

        int camX = (int) camera.position.x;
        int camZ = (int) camera.position.z;
        int chunkX = camX / CHUNK_SIZE;
        int chunkZ = camZ / CHUNK_SIZE;

        for (int x = chunkX - CHUNK_VIEW_RADIUS; x < chunkX + CHUNK_VIEW_RADIUS + TILE_SIZE; x++) {
            for (int z = chunkZ - CHUNK_VIEW_RADIUS; z < chunkZ + CHUNK_VIEW_RADIUS + TILE_SIZE; z++) {
                if (x < 0 || z < 0) continue; // No negative chunks or models instances exist here (in this project)...

                Chunk chunk = getChunk(x, z, false);
                if (chunk == null) continue;

                ModelCache modelCache = chunk.getModelCache();
                if (modelCache == null) continue;

                cache.add(modelCache);
            }
        }
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
