package com.mmobuilder.voxel.g3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import lombok.Getter;
import lombok.Setter;

import static com.mmobuilder.voxel.Constants.SLASH;

@Getter
@Setter

public class Block {
    private final int localX, localY, localZ;
    private Texture texture;
    private Color blockColor = Color.WHITE;
    private BlockType blockType = BlockType.AIR;
    private Material material;

    public Block(int localX, int localY, int localZ) {
        this.localX = localX;
        this.localY = localY;
        this.localZ = localZ;
    }

    public boolean isVisible() {
        return blockType != BlockType.AIR;
    }

    @Override
    public String toString() {
        return "Block: " + localX + SLASH + localY + SLASH + localZ + ", Color: " + blockColor + ", BlockType: " + blockType;
    }
}
