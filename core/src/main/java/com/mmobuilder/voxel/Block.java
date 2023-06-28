package com.mmobuilder.voxel;

import com.badlogic.gdx.graphics.Texture;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Block {
    private int x, y, z;
    private Texture texture;
    private BlockType blockType;
}
