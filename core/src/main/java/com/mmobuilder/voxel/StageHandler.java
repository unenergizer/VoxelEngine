package com.mmobuilder.voxel;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.StringBuilder;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import lombok.RequiredArgsConstructor;

import static com.mmobuilder.voxel.Constants.SLASH;

/**
 * Creates and renders the UI elements on the screen.
 */
@RequiredArgsConstructor
public class StageHandler extends ApplicationAdapter {

    private final StringBuilder stringBuilder;
    private final PerspectiveCamera camera;
    private final ChunkHandler chunkHandler;
    private Stage stage;
    private VisLabel fpsLabel;
    private VisLabel camLocation;
    private VisLabel chunkLocation;
    private VisLabel chunkTileLocation;

    @Override
    public void create() {
        VisUI.load(Gdx.files.internal("tixel/x1/tixel.json"));

        stage = new Stage();
        VisTable visTable = new VisTable(true);
        visTable.add(fpsLabel = new VisLabel("FPS: 99999999999999999999")).align(Alignment.LEFT.getAlignment()).row();
        visTable.add(camLocation = new VisLabel("CAM XYZ: 99999999999999999999")).align(Alignment.LEFT.getAlignment()).row();
        visTable.add(chunkLocation = new VisLabel("CHUNK XZ: 99999999999999999999")).align(Alignment.LEFT.getAlignment()).row();
        visTable.add(chunkTileLocation = new VisLabel("CHUNK TILE XZ: 99999999999999999999")).align(Alignment.LEFT.getAlignment()).row();
        visTable.pack();
        visTable.setPosition(10, Gdx.graphics.getHeight() - visTable.getHeight() - 20);
        stage.addActor(visTable);
    }

    private static final String FPS = "FPS: ";
    private static final String CAM_TILE = "CAM TILE XYZ: ";
    private static final String CHUNK_LOC = "CHUNK XZ: ";
    private static final String CHUNK_TILE = "CHUNK TILE XZ: ";
    public void updateDebugText() {
        // FPS
        stringBuilder.append(FPS);
        stringBuilder.append(Gdx.graphics.getFramesPerSecond());
        fpsLabel.setText(stringBuilder.toStringAndClear());

        // Camera tile location
        stringBuilder.append(CAM_TILE);
        stringBuilder.append((int) camera.position.x);
        stringBuilder.append(SLASH);
        stringBuilder.append((int) camera.position.y);
        stringBuilder.append(SLASH);
        stringBuilder.append((int) camera.position.z);
        camLocation.setText(stringBuilder.toStringAndClear());

        // Current camera chunk
        stringBuilder.append(CHUNK_LOC);
        stringBuilder.append(chunkHandler.getCurrentChunkX());
        stringBuilder.append(SLASH);
        stringBuilder.append(chunkHandler.getCurrentChunkZ());
        chunkLocation.setText(stringBuilder.toStringAndClear());

        // Current local chunk tile (0 - Constants.CHUNK_SIZE)
        stringBuilder.append(CHUNK_TILE);
        stringBuilder.append(chunkHandler.getChunkTileX());
        stringBuilder.append(SLASH);
        stringBuilder.append(chunkHandler.getChunkTileZ());
        chunkTileLocation.setText(stringBuilder.toStringAndClear());
    }

    @Override
    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        VisUI.dispose();
        stage.dispose();
    }
}
