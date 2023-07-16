package com.mmobuilder.voxel;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import static com.mmobuilder.voxel.Constants.*;

public class ChunkMeshGenerator {

    private final ModelBuilder modelBuilder = new ModelBuilder();
    private final VoxelCube voxelCube = new VoxelCube();

    private final ChunkHandler chunkHandler;

    private final Texture texture;

    /**
     * A cube has 6 faces.
     */
    private static final int CUBE_FACES = 6;
    /**
     * A quad has 4 vertices, one at each corner.
     */
    private static final int QUAD_VERTICES = 4;

    public ChunkMeshGenerator(ChunkHandler chunkHandler, Texture texture) {
        this.chunkHandler = chunkHandler;
        this.texture = texture;
    }

    /**
     * Generates a chunk landscape model.
     */
    public void generateChunkModel(Chunk chunk) {
        // Define the attributes for this model
        VertexAttribute position = new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE);
        VertexAttribute colorUnpacked = new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, 4, ShaderProgram.COLOR_ATTRIBUTE);
        VertexAttribute textureCoordinates = new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0");

        // Init vertices array
        float[] vertices = new float[((position.numComponents + colorUnpacked.numComponents + textureCoordinates.numComponents) * QUAD_VERTICES * CHUNK_SIZE * CHUNK_SIZE * WORLD_HEIGHT) * CUBE_FACES];

        // Populate the vertices array with data
        int vertexOffset = 0;
        for (int y = 0; y < WORLD_HEIGHT; y++) {
            for (int x = 0; x < CHUNK_SIZE; x++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {

                    int worldX = x + CHUNK_SIZE * chunk.getChunkX();
                    int worldZ = z + CHUNK_SIZE * chunk.getChunkZ();

                    Block block = chunkHandler.getBlock(worldX, y, worldZ);
                    if (block == null || !block.isVisible()) continue;

                    System.out.println("[CreateMesh] Chunk: " + chunk.getChunkX() + SLASH + chunk.getChunkZ() + ", World: " + worldX + SLASH + y + SLASH + worldZ + ", Local: " + x + SLASH + z);
                    System.out.println("[CreateMesh] Block: " + block);

                    // Check neighboring blocks to determine which faces to cull
                    boolean top = isSolid(worldX, y + 1, worldZ);
                    boolean bot = isSolid(worldX, y - 1, worldZ);
                    boolean lef = isSolid(worldX - 1, y, worldZ);
                    boolean rig = isSolid(worldX + 1, y, worldZ);
                    boolean fro = isSolid(worldX, y, worldZ - 1);
                    boolean bac = isSolid(worldX, y, worldZ + 1);


                    if (!top) {
                        vertexOffset = voxelCube.createTop(vertices, vertexOffset, worldX, worldZ, y, block.getBlockColor(), new TextureRegion(block.getTexture()));
                    }
                    if (!bot) {
                        vertexOffset = voxelCube.createBottom(vertices, vertexOffset, worldX, worldZ, y, block.getBlockColor(), new TextureRegion(block.getTexture()));
                    }
                    if (!lef) {
                        vertexOffset = voxelCube.createLeft(vertices, vertexOffset, worldX, worldZ, y, block.getBlockColor(), new TextureRegion(block.getTexture()));
                    }
                    if (!rig) {
                        vertexOffset = voxelCube.createRight(vertices, vertexOffset, worldX, worldZ, y, block.getBlockColor(), new TextureRegion(block.getTexture()));
                    }
                    if (!fro) {
                        vertexOffset = voxelCube.createFront(vertices, vertexOffset, worldX, worldZ, y, block.getBlockColor(), new TextureRegion(block.getTexture()));
                    }
                    if (!bac) {
                        vertexOffset = voxelCube.createBack(vertices, vertexOffset, worldX, worldZ, y, block.getBlockColor(), new TextureRegion(block.getTexture()));
                    }
                }
            }
        }

        // Generate the indices
        int size = (6 * CHUNK_SIZE * CHUNK_SIZE * WORLD_HEIGHT) * CUBE_FACES;
        short[] indices = new short[size];
        generateIndices(indices);

        // Create the mesh
        Mesh mesh = new Mesh(true, vertices.length, indices.length, position, colorUnpacked, textureCoordinates);
        mesh.setVertices(vertices);
        mesh.setIndices(indices);

        // Create the MeshPart
        MeshPart meshPart = new MeshPart(null, mesh, 0, size, GL30.GL_TRIANGLES);

        // Create a model out of the MeshPart
        modelBuilder.begin();
        modelBuilder.part(meshPart, new Material("texture", TextureAttribute.createDiffuse(texture)));
        chunk.setModel(modelBuilder.end());
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    public void generateIndices(short[] indices) {
        short j = 0;
        for (int i = 0; i < indices.length; i += 6, j += 4) {
            indices[i + 0] = (short) (j + 2);
            indices[i + 1] = (short) (j + 1);
            indices[i + 2] = (short) (j + 3);
            indices[i + 3] = (short) (j + 0);
            indices[i + 4] = (short) (j + 3);
            indices[i + 5] = (short) (j + 1);
        }
    }

    private boolean isSolid(int worldX, int worldY, int worldZ) {
        if (BlockUtil.isBlockOutsideWorldY(worldY)) return false;
        Block block = chunkHandler.getBlock(worldX, worldY, worldZ);

        if (block == null) return false;
        return block.isVisible();
    }

}
