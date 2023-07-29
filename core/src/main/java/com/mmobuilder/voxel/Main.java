package com.mmobuilder.voxel;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.mmobuilder.voxel.g2d.CrosshairRenderer;
import com.mmobuilder.voxel.g3d.Chunk;
import com.mmobuilder.voxel.g3d.ChunkHandler;
import com.mmobuilder.voxel.g3d.SceneHandler;
import com.mmobuilder.voxel.input.*;
import com.mmobuilder.voxel.ui.StageHandler;
import lombok.Getter;

public class Main extends ApplicationAdapter {

    private SceneHandler sceneHandler;
    private CrosshairRenderer crosshairRenderer;
    @Getter
    private ChunkHandler chunkHandler;
    private StageHandler stageHandler;

    private Model xyzModel;
    private ModelInstance xyzModelInstance;
    private FirstPersonMovementController movementController;

    private final ModelCache modelCache = new ModelCache();

    @Override
    public void create() {
        crosshairRenderer = new CrosshairRenderer();
        crosshairRenderer.create();

        // Init Camera
        PerspectiveCamera camera = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 10f, -5f);
        camera.lookAt(0, 0, 0);
        camera.up.set(Vector3.Y);
        camera.near = 0.01f;
        camera.far = 1000f;

        sceneHandler = new SceneHandler(camera);
        sceneHandler.create();

        // Init Models
        ModelBuilder modelBuilder = new ModelBuilder();
        xyzModel = modelBuilder.createXYZCoordinates(10, new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked);
        xyzModelInstance = new ModelInstance(xyzModel);
        xyzModelInstance.transform.translate(Vector3.Zero);
        sceneHandler.addRenderableProvider(xyzModelInstance);

        // Init the ChunkHandler
        chunkHandler = new ChunkHandler(camera);
        chunkHandler.create();

        // Init Scene2D and VisUI
        stageHandler = new StageHandler(this, camera);
        stageHandler.create();

        // Init Input
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        movementController = new FirstPersonMovementController(camera);
        movementController.setVelocity(8);
        inputMultiplexer.addProcessor(stageHandler.getInputAdapter());
        inputMultiplexer.addProcessor(new CursorCatchedController());
        inputMultiplexer.addProcessor(new BlockPickController(this, chunkHandler, camera));
        inputMultiplexer.addProcessor(new KeyboardInput(this));
        inputMultiplexer.addProcessor(movementController);
        inputMultiplexer.addProcessor(new FirstPersonCameraController(camera));
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void updateModelInstanceList(boolean bypassChunkCheck) {
        if (!bypassChunkCheck && !chunkHandler.hasLeftChunk()) return;

        modelCache.dispose();

        for (Chunk chunk : chunkHandler.getChunkHashMap().values()) {
            chunkHandler.getChunkMeshGenerator().generateChunkModel(chunk);
        }

        modelCache.begin();
        modelCache.add(xyzModelInstance);

        for (Chunk chunk : chunkHandler.getChunkHashMap().values()) {
            Model model = chunk.getModel();
            if (model != null) modelCache.add(new ModelInstance(model));
        }

        modelCache.end();
        sceneHandler.addRenderableProvider(modelCache);
    }

    @Override
    public void resize(int width, int height) {
        sceneHandler.resize(width, height);
    }

    @Override
    public void render() {
        movementController.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sceneHandler.render();
        crosshairRenderer.render();
        stageHandler.render();
    }

    @Override
    public void dispose() {
        sceneHandler.dispose();
        stageHandler.dispose();

        // models
        chunkHandler.dispose();
        xyzModel.dispose();

        // 2D
        crosshairRenderer.dispose();
    }
}
