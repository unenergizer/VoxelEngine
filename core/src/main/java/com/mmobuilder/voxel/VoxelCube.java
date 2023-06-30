package com.mmobuilder.voxel;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import static com.mmobuilder.voxel.Constants.CHUNK_SIZE;
import static com.mmobuilder.voxel.Constants.TILE_SIZE;

public class VoxelCube {

    /**
     * A cube has 6 faces.
     */
    private static final int CUBE_FACES = 6;
    /**
     * A quad has 4 vertices, one at each corner.
     */
    private static final int QUAD_VERTICES = 4;

    /**
     * Generates a chunk landscape model.
     *
     * @param texture The texture to paint on this model.
     * @param color   The color we want to apply to the texture.
     * @return A mesh that represents this cube.
     */
    public Mesh generateChunkModel(Texture texture, Color color, Chunk chunk) {
        // Define the attributes for this model
        VertexAttribute position = new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE);
        VertexAttribute colorUnpacked = new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, 4, ShaderProgram.COLOR_ATTRIBUTE);
        VertexAttribute textureCoordinates = new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0");

        // Init vertices array
        float[] vertices = new float[((position.numComponents + colorUnpacked.numComponents + textureCoordinates.numComponents) * QUAD_VERTICES * CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * CUBE_FACES];

        // Populate the vertices array with data
        int vertexOffset = 0;
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                for (int y = 0; y < CHUNK_SIZE; y++) {
                    if (chunk.getBlocks()[x][y][z].getBlockType() == BlockType.AIR) continue;

                    // Check neighboring blocks to determine which faces to cull
                    boolean cullTop = shouldCullFace(chunk, x, y + 1, z);
                    boolean cullBottom = shouldCullFace(chunk, x, y - 1, z);
                    boolean cullLeft = shouldCullFace(chunk, x - 1, y, z);
                    boolean cullRight = shouldCullFace(chunk, x + 1, y, z);
                    boolean cullFront = shouldCullFace(chunk, x, y, z - 1);
                    boolean cullBack = shouldCullFace(chunk, x, y, z + 1);

                    if (!cullTop)
                        vertexOffset = createTop(vertices, vertexOffset, x, z, y, color, new TextureRegion(texture));
                    if (!cullBottom)
                        vertexOffset = createBottom(vertices, vertexOffset, x, z, y, color, new TextureRegion(texture));
                    if (!cullLeft)
                        vertexOffset = createLeft(vertices, vertexOffset, x, z, y, color, new TextureRegion(texture));
                    if (!cullRight)
                        vertexOffset = createRight(vertices, vertexOffset, x, z, y, color, new TextureRegion(texture));
                    if (!cullFront)
                        vertexOffset = createFront(vertices, vertexOffset, x, z, y, color, new TextureRegion(texture));
                    if (!cullBack)
                        vertexOffset = createBack(vertices, vertexOffset, x, z, y, color, new TextureRegion(texture));

                    System.out.println("XYZ: " + x + "/" + y + "/" + z);
                }
            }
        }

        // Generate the indices
        int size = (6 * CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * CUBE_FACES;
        short[] indices = new short[size];
        generateIndices(indices);

        // Create the mesh
        Mesh mesh = new Mesh(true, vertices.length, indices.length, position, colorUnpacked, textureCoordinates);
        mesh.setVertices(vertices);
        mesh.setIndices(indices);

        return mesh;
    }

    private boolean shouldCullFace(Chunk chunk, int x, int y, int z) {
        if (x < 0 || x >= CHUNK_SIZE || y < 0 || y >= CHUNK_SIZE || z < 0 || z >= CHUNK_SIZE) {
            // The neighboring block is outside the chunk, so we should not cull the face
            return false;
        }

        return chunk.getBlocks()[x][y][z].getBlockType() != BlockType.AIR;
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

    private int createTop(float[] vertices, int vertexOffset, float x, float z, float y, Color tileColor, TextureRegion textureRegion) {
        float u1 = textureRegion.getU();
        float v1 = textureRegion.getV();
        float u2 = textureRegion.getU2();
        float v2 = textureRegion.getV2();

        // Bottom Left [0,0]
        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;

        // Bottom Right [1,0]
        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v1;

        // Top Right [1,1]
        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z + TILE_SIZE;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v2;

        // Top Left [0,1]
        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z + TILE_SIZE;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v2;

        return vertexOffset;
    }

    private int createBottom(float[] vertices, int vertexOffset, float x, float z, float y, Color tileColor, TextureRegion textureRegion) {
        float u1 = textureRegion.getU();
        float v1 = textureRegion.getV();
        float u2 = textureRegion.getU2();
        float v2 = textureRegion.getV2();

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + TILE_SIZE;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + TILE_SIZE;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v2;

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v2;

        return vertexOffset;
    }

    private int createLeft(float[] vertices, int vertexOffset, float x, float z, float y, Color tileColor, TextureRegion textureRegion) {
        float u1 = textureRegion.getU();
        float v1 = textureRegion.getV();
        float u2 = textureRegion.getU2();
        float v2 = textureRegion.getV2();

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z + TILE_SIZE;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v2;

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + TILE_SIZE;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v2;

        return vertexOffset;
    }

    private int createRight(float[] vertices, int vertexOffset, float x, float z, float y, Color tileColor, TextureRegion textureRegion) {
        float u1 = textureRegion.getU();
        float v1 = textureRegion.getV();
        float u2 = textureRegion.getU2();
        float v2 = textureRegion.getV2();

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + TILE_SIZE;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v1;

        // Top Right
        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z + TILE_SIZE;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v2;

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v2;

        return vertexOffset;
    }

    private int createFront(float[] vertices, int vertexOffset, float x, float z, float y, Color tileColor, TextureRegion textureRegion) {
        float u1 = textureRegion.getU();
        float v1 = textureRegion.getV();
        float u2 = textureRegion.getU2();
        float v2 = textureRegion.getV2();

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v2;

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v2;

        return vertexOffset;
    }

    private int createBack(float[] vertices, int vertexOffset, float x, float z, float y, Color tileColor, TextureRegion textureRegion) {
        float u1 = textureRegion.getU();
        float v1 = textureRegion.getV();
        float u2 = textureRegion.getU2();
        float v2 = textureRegion.getV2();

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + TILE_SIZE;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z + TILE_SIZE;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z + TILE_SIZE;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v2;

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + TILE_SIZE;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v2;

        return vertexOffset;
    }
}
