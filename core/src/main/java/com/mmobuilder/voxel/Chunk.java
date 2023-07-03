package com.mmobuilder.voxel;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import static com.mmobuilder.voxel.Constants.*;

/**
 * A chunk that is a part of a world of multiple chunks. This chunk is responsible
 * for holding the data needed to render one part of a landmass.
 */
@Getter
public class Chunk {
    private final int chunkX, chunkZ;
    private final Map<Integer, Block[][][]> blocks = new HashMap<>();
    private final Map<Integer, Mesh> meshMap = new HashMap<>();

    private Material material;

    @Setter
    private Texture texture;

    public Chunk(int chunkX, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;

        for (int sectionY = 0; sectionY < WORLD_Y_LENGTH; sectionY++) {
            blocks.put(sectionY, new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE]);
        }
    }

    public void setMesh(int sectionY, Mesh mesh) {
        meshMap.put(sectionY, mesh);

        material = new Material("texture", TextureAttribute.createDiffuse(texture));
    }

    @Override
    public String toString() {
        return "Chunk X/Z: " + chunkX + SLASH + chunkZ;
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
