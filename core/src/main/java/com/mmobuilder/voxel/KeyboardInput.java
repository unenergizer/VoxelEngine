package com.mmobuilder.voxel;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;

public class KeyboardInput extends InputAdapter {

    private final Main main;

    public KeyboardInput(Main main) {
        this.main = main;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.F1) {
            Block block = main.getChunkHandler().getBlock(0, 0, 0);
            block.setBlockType(BlockType.AIR);
            main.updateModelInstanceList(true);
            System.out.println("Setting air");
            System.out.println(block);
            return true;
        }

        if (keycode == Input.Keys.F2) {
            Block block = main.getChunkHandler().getBlock(0, 0, 0);
            block.setBlockType(BlockType.SOLID);
            main.updateModelInstanceList(true);
            System.out.println("Setting solid");
            System.out.println(block);
            return true;
        }

        if (keycode == Input.Keys.F3) {
            Block block = main.getChunkHandler().getBlock(0, 0, 0);
            block.setBlockColor(Color.BLUE);
            main.updateModelInstanceList(true);
            System.out.println("Setting color blue");
            System.out.println(block);
            return true;
        }

        if (keycode == Input.Keys.F4) {
            Block block = main.getChunkHandler().getBlock(0, 0, 0);
            block.setBlockColor(Color.WHITE);
            main.updateModelInstanceList(true);
            System.out.println("Setting color white");
            System.out.println(block);
            return true;
        }

        if (keycode == Input.Keys.NUM_1) {
            main.getChunkHandler().fillChunkData(10);
            main.updateModelInstanceList(true);
        }

        if (keycode == Input.Keys.NUM_2) {
            main.getChunkHandler().fillChunkData(20);
            main.updateModelInstanceList(true);
        }

        if (keycode == Input.Keys.NUM_3) {
            main.getChunkHandler().fillChunkData(30);
            main.updateModelInstanceList(true);
        }

        if (keycode == Input.Keys.NUM_4) {
            main.getChunkHandler().fillChunkData(40);
            main.updateModelInstanceList(true);
        }

        if (keycode == Input.Keys.NUM_5) {
            main.getChunkHandler().fillChunkData(50);
            main.updateModelInstanceList(true);
        }

        if (keycode == Input.Keys.NUM_6) {
            main.getChunkHandler().fillChunkData(60);
            main.updateModelInstanceList(true);
        }

        if (keycode == Input.Keys.NUM_7) {
            main.getChunkHandler().fillChunkData(70);
            main.updateModelInstanceList(true);
        }

        if (keycode == Input.Keys.NUM_8) {
            main.getChunkHandler().fillChunkData(80);
            main.updateModelInstanceList(true);
        }

        if (keycode == Input.Keys.NUM_9) {
            main.getChunkHandler().fillChunkData(90);
            main.updateModelInstanceList(true);
        }

        if (keycode == Input.Keys.NUM_0) {
            main.getChunkHandler().fillChunkData(0);
            main.updateModelInstanceList(true);
        }

        if (keycode == Input.Keys.MINUS) {
            main.getChunkHandler().fillChunkData(100);
            main.updateModelInstanceList(true);
        }

        return false;
    }
}
