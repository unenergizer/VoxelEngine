package com.mmobuilder.voxel.g2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.File;

public class CrosshairRenderer extends ApplicationAdapter {
    private Texture crosshairTexture;
    private SpriteBatch spriteBatch;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        crosshairTexture = new Texture(Gdx.files.internal("textures" + File.separator + "crosshair.png"));
    }

    @Override
    public void render() {
        spriteBatch.begin();
        if (Gdx.input.isCursorCatched()) {
            spriteBatch.setColor(Color.GREEN);
        } else {
            spriteBatch.setColor(Color.RED);
        }
        spriteBatch.draw(crosshairTexture, Gdx.graphics.getWidth() / 2f - crosshairTexture.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - crosshairTexture.getHeight() / 2f);
        spriteBatch.end();
    }

    @Override
    public void pause() {
        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void dispose() {
        crosshairTexture.dispose();
        spriteBatch.dispose();
    }
}
