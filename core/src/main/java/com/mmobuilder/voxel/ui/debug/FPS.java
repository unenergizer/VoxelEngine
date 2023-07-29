package com.mmobuilder.voxel.ui.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.StringBuilder;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.mmobuilder.voxel.g3d.ChunkHandler;
import com.mmobuilder.voxel.ui.*;
import com.mmobuilder.voxel.ui.menu.MainMenuBar;

import static com.mmobuilder.voxel.g3d.ChunkConstants.SLASH;

public class FPS extends VisTable implements BuildActor, ForcePosition {

    private static final String FPS = "FPS: ";
    private static final String CAM_TILE = "CAM TILE XYZ: ";
    private static final String CHUNK_LOC = "CHUNK XZ: ";
    private static final String CHUNK_TILE = "CHUNK TILE XZ: ";
    private final StringBuilder stringBuilder = new StringBuilder();
    private final MainMenuBar mainMenuBar;
    private final PerspectiveCamera gameCamera;
    private final ChunkHandler chunkHandler;
    private VisLabel fpsLabel;
    private VisLabel camLocation;
    private VisLabel chunkLocation;
    private VisLabel chunkTileLocation;

    public FPS(MainMenuBar mainMenuBar, PerspectiveCamera gameCamera, ChunkHandler chunkHandler) {
        this.mainMenuBar = mainMenuBar;
        this.gameCamera = gameCamera;
        this.chunkHandler = chunkHandler;
    }

    @Override
    public Actor buildActor(StageHandler stageHandler) {
        VisTable visTable = new VisTable(true);
        visTable.add(fpsLabel = new VisLabel("FPS: 99999999999999999999")).align(Alignment.LEFT.getAlignment()).row();
        visTable.add(camLocation = new VisLabel("CAM XYZ: 99999999999999999999")).align(Alignment.LEFT.getAlignment()).row();
        visTable.add(chunkLocation = new VisLabel("CHUNK XZ: 99999999999999999999")).align(Alignment.LEFT.getAlignment()).row();
        visTable.add(chunkTileLocation = new VisLabel("CHUNK TILE XZ: 99999999999999999999")).align(Alignment.LEFT.getAlignment()).row();

        align(Alignment.LEFT.getAlignment());
        addListener(new StageHandler.UpdateActorEventListener() {
            @Override
            protected void updateActorEvent() {
                updateDebugText();
            }
        });

        add(visTable);
        pack();
        setPosition();
        return this;
    }

    public void updateDebugText() {
        // FPS
        stringBuilder.append(FPS);
        stringBuilder.append(Gdx.graphics.getFramesPerSecond());
        fpsLabel.setText(stringBuilder.toStringAndClear());

        // Camera tile location
        stringBuilder.append(CAM_TILE);
        stringBuilder.append((int) gameCamera.position.x);
        stringBuilder.append(SLASH);
        stringBuilder.append((int) gameCamera.position.y);
        stringBuilder.append(SLASH);
        stringBuilder.append((int) gameCamera.position.z);
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
    public void setPosition() {
        setPosition(UiConstants.PADDING, Gdx.graphics.getHeight() - mainMenuBar.getHeight() - getHeight() - UiConstants.PADDING);
    }
}
