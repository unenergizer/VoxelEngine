package com.mmobuilder.voxel.g3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.mmobuilder.voxel.g3d.ChunkConstants.TILE_SIZE;

public class VoxelCube {

    private final boolean enableNormals;
    private final boolean enableTangent;
    private final boolean enableColor;
    private final boolean enableTexture;

    public VoxelCube(boolean enableNormals, boolean enableTangent, boolean enableColor, boolean enableTexture) {
        this.enableNormals = enableNormals;
        this.enableTangent = enableTangent;
        this.enableColor = enableColor;
        this.enableTexture = enableTexture;
    }

    public int createTop(float[] vertices, int vertexOffset, float x, float y, float z, Color tileColor, TextureRegion textureRegion) {
        float u1 = 0, v1 = 0, u2 = 0, v2 = 0;
        if (textureRegion != null) u1 = textureRegion.getU();
        if (textureRegion != null) v1 = textureRegion.getV();
        if (textureRegion != null) u2 = textureRegion.getU2();
        if (textureRegion != null) v2 = textureRegion.getV2();

        // Bottom Left [0,0]
        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z;
        if (enableTexture) vertices[vertexOffset++] = u1;
        if (enableTexture) vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        // Bottom Right [1,0]
        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z;
        if (enableTexture) vertices[vertexOffset++] = u2;
        if (enableTexture) vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        // Top Right [1,1]
        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z + TILE_SIZE;
        if (enableTexture) vertices[vertexOffset++] = u2;
        if (enableTexture) vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        // Top Left [0,1]
        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z + TILE_SIZE;
        if (enableTexture) vertices[vertexOffset++] = u1;
        if (enableTexture) vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        return vertexOffset;
    }

    public int createBottom(float[] vertices, int vertexOffset, float x, float y, float z, Color tileColor, TextureRegion textureRegion) {
        float u1 = 0, v1 = 0, u2 = 0, v2 = 0;
        if (textureRegion != null) u1 = textureRegion.getU();
        if (textureRegion != null) v1 = textureRegion.getV();
        if (textureRegion != null) u2 = textureRegion.getU2();
        if (textureRegion != null) v2 = textureRegion.getV2();

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        if (enableTexture) vertices[vertexOffset++] = u1;
        if (enableTexture) vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + TILE_SIZE;
        if (enableTexture) vertices[vertexOffset++] = u2;
        if (enableTexture) vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + TILE_SIZE;
        if (enableTexture) vertices[vertexOffset++] = u2;
        if (enableTexture) vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        if (enableTexture) vertices[vertexOffset++] = u1;
        if (enableTexture) vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        return vertexOffset;
    }

    public int createLeft(float[] vertices, int vertexOffset, float x, float y, float z, Color tileColor, TextureRegion textureRegion) {
        float u1 = 0, v1 = 0, u2 = 0, v2 = 0;
        if (textureRegion != null) u1 = textureRegion.getU();
        if (textureRegion != null) v1 = textureRegion.getV();
        if (textureRegion != null) u2 = textureRegion.getU2();
        if (textureRegion != null) v2 = textureRegion.getV2();

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        if (enableTexture) vertices[vertexOffset++] = u1;
        if (enableTexture) vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z;
        if (enableTexture) vertices[vertexOffset++] = u1;
        if (enableTexture) vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z + TILE_SIZE;
        if (enableTexture) vertices[vertexOffset++] = u2;
        if (enableTexture) vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + TILE_SIZE;
        if (enableTexture) vertices[vertexOffset++] = u2;
        if (enableTexture) vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        return vertexOffset;
    }

    public int createRight(float[] vertices, int vertexOffset, float x, float y, float z, Color tileColor, TextureRegion textureRegion) {
        float u1 = 0, v1 = 0, u2 = 0, v2 = 0;
        if (textureRegion != null) u1 = textureRegion.getU();
        if (textureRegion != null) v1 = textureRegion.getV();
        if (textureRegion != null) u2 = textureRegion.getU2();
        if (textureRegion != null) v2 = textureRegion.getV2();

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        if (enableTexture) vertices[vertexOffset++] = u2;
        if (enableTexture) vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + TILE_SIZE;
        if (enableTexture) vertices[vertexOffset++] = u1;
        if (enableTexture) vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        // Top Right
        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z + TILE_SIZE;
        if (enableTexture) vertices[vertexOffset++] = u1;
        if (enableTexture) vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z;
        if (enableTexture) vertices[vertexOffset++] = u2;
        if (enableTexture) vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        return vertexOffset;
    }

    public int createFront(float[] vertices, int vertexOffset, float x, float y, float z, Color tileColor, TextureRegion textureRegion) {
        float u1 = 0, v1 = 0, u2 = 0, v2 = 0;
        if (textureRegion != null) u1 = textureRegion.getU();
        if (textureRegion != null) v1 = textureRegion.getV();
        if (textureRegion != null) u2 = textureRegion.getU2();
        if (textureRegion != null) v2 = textureRegion.getV2();

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        if (enableTexture) vertices[vertexOffset++] = u2;
        if (enableTexture) vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        if (enableTexture) vertices[vertexOffset++] = u1;
        if (enableTexture) vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z;
        if (enableTexture) vertices[vertexOffset++] = u1;
        if (enableTexture) vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z;
        if (enableTexture) vertices[vertexOffset++] = u2;
        if (enableTexture) vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        return vertexOffset;
    }

    public int createBack(float[] vertices, int vertexOffset, float x, float y, float z, Color tileColor, TextureRegion textureRegion) {
        float u1 = 0, v1 = 0, u2 = 0, v2 = 0;
        if (textureRegion != null) u1 = textureRegion.getU();
        if (textureRegion != null) v1 = textureRegion.getV();
        if (textureRegion != null) u2 = textureRegion.getU2();
        if (textureRegion != null) v2 = textureRegion.getV2();

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + TILE_SIZE;
        if (enableTexture) vertices[vertexOffset++] = u1;
        if (enableTexture) vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z + TILE_SIZE;
        if (enableTexture) vertices[vertexOffset++] = u1;
        if (enableTexture) vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y + TILE_SIZE;
        vertices[vertexOffset++] = z + TILE_SIZE;
        if (enableTexture) vertices[vertexOffset++] = u2;
        if (enableTexture) vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        vertices[vertexOffset++] = x + TILE_SIZE;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + TILE_SIZE;
        if (enableTexture) vertices[vertexOffset++] = u2;
        if (enableTexture) vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);
        vertexOffset = addTangent(vertices, vertexOffset);
        vertexOffset = addColor(vertices, vertexOffset, tileColor);

        return vertexOffset;
    }

    private int addNormals(float[] vertices, int vertexOffset) {
        if (!enableNormals) return vertexOffset;
        for (int i = 0; i < 3; i++) vertices[vertexOffset++] = 1f;
        return vertexOffset;
    }

    private int addTangent(float[] vertices, int vertexOffset) {
        if (!enableTangent) return vertexOffset;
        for (int i = 0; i < 4; i++) vertices[vertexOffset++] = 0f;
        return vertexOffset;
    }

    private int addColor(float[] vertices, int vertexOffset, Color tileColor) {
        if (!enableColor) return vertexOffset;
        vertices[vertexOffset++] = tileColor.r;
        vertices[vertexOffset++] = tileColor.g;
        vertices[vertexOffset++] = tileColor.b;
        vertices[vertexOffset++] = tileColor.a;
        return vertexOffset;
    }
}
