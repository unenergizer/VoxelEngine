package com.mmobuilder.voxel.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.VisTable;
import com.mmobuilder.voxel.Main;
import net.mgsx.gltf.scene3d.scene.Scene;

public class FileSelectHack extends VisTable implements BuildActor {

    private final Main main;

    public FileSelectHack(Main main) {
        this.main = main;
    }

    @Override
    public Actor buildActor(StageHandler stageHandler) {

        // Listen for file selection events and then open the model
        addListener(new FileExplorer.FileSelectedEventListener() {
            @Override
            protected void handleFileSelectionEvent(FileHandle fileHandle) {
                Scene scene = main.getSceneHandler().loadModel(fileHandle);
                main.getSceneHandler().addModelToScene(scene);
            }
        });

        return this;
    }
}
