package com.mmobuilder.voxel;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static com.mmobuilder.voxel.Constants.CHUNK_SIZE;
import static com.mmobuilder.voxel.Constants.SLASH;

/**
 * A chunk that is a part of a world of multiple chunks. This chunk is responsible
 * for holding the data needed to render one part of a landmass.
 */
@Getter
@RequiredArgsConstructor
public class Chunk {
    private final int chunkX, chunkZ;
    private final Block[][][] blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
    private Mesh mesh;

    private Material material;

    @Setter
    private Texture texture;

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;

        material = new Material("texture", TextureAttribute.createDiffuse(texture));
    }

    @Override
    public String toString() {
        return "Chunk X/Z: " + chunkX + SLASH + chunkZ + ", NumVertices: " + mesh.getNumVertices() + ", NumIndices: " + mesh.getNumIndices();
    }

    /**
     * The key that can be used to identify a chunk.
     */
    public static class Key {
        private final int chunkX, chunkZ;

        private final int hashCode;

        public Key(int chunkX, int chunkZ) {
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.hashCode = 31 * chunkX + chunkZ;
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Key)) return false;
            Key compareKey = (Key) object;
            return chunkX == compareKey.chunkX && chunkZ == compareKey.chunkZ;
        }
    }
}
