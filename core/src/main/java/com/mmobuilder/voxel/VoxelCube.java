package com.mmobuilder.voxel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.mmobuilder.voxel.Constants.TILE_SIZE;

public class VoxelCube {
    public int createTop(float[] vertices, int vertexOffset, float x, float z, float y, Color tileColor, TextureRegion textureRegion) {
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

    public int createBottom(float[] vertices, int vertexOffset, float x, float z, float y, Color tileColor, TextureRegion textureRegion) {
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

    public int createLeft(float[] vertices, int vertexOffset, float x, float z, float y, Color tileColor, TextureRegion textureRegion) {
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

    public int createRight(float[] vertices, int vertexOffset, float x, float z, float y, Color tileColor, TextureRegion textureRegion) {
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

    public int createFront(float[] vertices, int vertexOffset, float x, float z, float y, Color tileColor, TextureRegion textureRegion) {
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

    public int createBack(float[] vertices, int vertexOffset, float x, float z, float y, Color tileColor, TextureRegion textureRegion) {
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
