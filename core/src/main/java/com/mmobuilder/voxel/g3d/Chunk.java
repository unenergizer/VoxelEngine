package com.mmobuilder.voxel.g3d;

import com.badlogic.gdx.graphics.g3d.Model;
import lombok.Getter;

import static com.mmobuilder.voxel.g3d.ChunkConstants.*;

/**
 * A chunk that is a part of a world of multiple chunks. This chunk is responsible
 * for holding the data needed to render one part of a landmass.
 */
@Getter
public class Chunk {
    private final ChunkHandler chunkhandler;
    private final int chunkX, chunkZ;
    private final ChunkSection[] sections = new ChunkSection[VERTICAL_CHUNK_SECTIONS];

    private Model model;

    public Chunk(ChunkHandler chunkHandler, int chunkX, int chunkZ) {
        this.chunkhandler = chunkHandler;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    void setModel(Model model) {
//        if (this.model != null) model.dispose(); // Crashes game engine
        this.model = model;
    }

    Block getBlock(int localX, int worldY, int localZ) {
        if (BlockUtil.isBlockOutsideWorldY(worldY)) throw new IndexOutOfBoundsException("Y value out of range! Value: " + worldY);

        int sectionId = Math.floorDiv(worldY, CHUNK_SIZE);
        int localY = worldY - sectionId * CHUNK_SIZE;
        ChunkSection section = sections[sectionId];

        if (section == null) return null; // No blocks in this section
        return section.getBlock(localX, localY, localZ);
    }

    void setBlock(int localX, int worldY, int localZ, Block block) {
        if (BlockUtil.isBlockOutsideWorldY(worldY)) throw new IndexOutOfBoundsException("Y value out of range! Value: " + worldY);

        int sectionId = Math.floorDiv(worldY, CHUNK_SIZE);
        int localY = worldY - sectionId * CHUNK_SIZE;
        ChunkSection section = sections[sectionId];

        if (section == null) {
            if (block == null) return; // Don't need to create a section for a null block

            section = new ChunkSection();
            sections[sectionId] = section;
        }

        section.setBlock(localX, localY, localZ, block);
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
