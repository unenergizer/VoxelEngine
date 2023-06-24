package com.mmobuilder.voxel;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
    private Model model;
    private ModelInstance modelInstance;
    private ModelCache modelCache;

    public void setModel(Model model) {
        this.model = model;
        this.modelInstance = new ModelInstance(model);
        modelInstance.transform.setTranslation(chunkX * CHUNK_SIZE, 0, chunkZ * CHUNK_SIZE);

        // Set up the model cache for this model
        modelCache = new ModelCache();
        modelCache.begin();
        modelCache.add(modelInstance);
        modelCache.end();
    }

    @Override
    public String toString() {
        return "Chunk X/Z: " + chunkX + SLASH + chunkZ + ", Nodes: " + model.nodes.size + ", Meshes: " + model.meshes.size + ", MeshParts: " + model.meshParts.size;
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
