package com.mmobuilder.voxel;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.StringBuilder;
import com.mmobuilder.voxel.input.*;
import lombok.Getter;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

public class Main extends ApplicationAdapter {
    /**
     * One StringBuilder to rule them all, one StringBuilder to find them,
     * One StringBuilder to bring them all, and in the darkness bind them;
     * In the Land of Gdx where the triangles lie. ~ Future Tolkien (probably)
     */
    private final StringBuilder stringBuilder = new StringBuilder();
    private SceneManager sceneManager;
    private SceneAsset sceneAsset;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private SceneSkybox skybox;

    private Texture crosshairTexture;
    private SpriteBatch batch;

    private Model xyzModel;
    private ModelInstance xyzModelInstance;
    private FirstPersonMovementController camController;

    private final ModelCache modelCache = new ModelCache();
    @Getter
    private ChunkHandler chunkHandler;
    private StageHandler stageHandler;

    @Override
    public void create() {
        batch = new SpriteBatch();
        crosshairTexture = new Texture(Gdx.files.internal("crosshair.png"));

        // create scene
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("models/BoomBox/glTF/BoomBox.gltf"));
        Scene scene = new Scene(sceneAsset.scene);
        sceneManager = new SceneManager();
        sceneManager.addScene(scene);

        // Init Camera
        PerspectiveCamera camera = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 10f, -5f);
        camera.lookAt(0, 0, 0);
        camera.up.set(Vector3.Y);
        camera.near = 0.01f;
        camera.far = 1000f;
        sceneManager.setCamera(camera);

        // setup light
        DirectionalLightEx light = new DirectionalLightEx();
        sceneManager.setAmbientLight(0.01f);
        sceneManager.environment.add(new DirectionalLightEx().set(Color.WHITE, new Vector3(-1, -4, -2), 5));

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(1024);
        diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        // This texture is provided by the library, no need to have it in your assets.
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // setup skybox
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);

        // Init Models
        ModelBuilder modelBuilder = new ModelBuilder();
        xyzModel = modelBuilder.createXYZCoordinates(10, new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked);
        xyzModelInstance = new ModelInstance(xyzModel);
        xyzModelInstance.transform.translate(Vector3.Zero);
        sceneManager.getRenderableProviders().add(xyzModelInstance);

        // Init the ChunkHandler
        chunkHandler = new ChunkHandler(camera);
        chunkHandler.create();

        // Init Scene2D and VisUI
        stageHandler = new StageHandler(stringBuilder, camera, chunkHandler);
        stageHandler.create();

        // Init Input
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        camController = new FirstPersonMovementController(camera);
        camController.setVelocity(8);
        inputMultiplexer.addProcessor(new CursorCatchedController());
        inputMultiplexer.addProcessor(new BlockPickController(this, chunkHandler, camera));
        inputMultiplexer.addProcessor(new KeyboardInput(this));
        inputMultiplexer.addProcessor(camController);
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
        sceneManager.getRenderableProviders().add(modelCache);
    }

    @Override
    public void resize(int width, int height) {
        sceneManager.updateViewport(width, height);
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        camController.update();
        stageHandler.updateDebugText();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sceneManager.update(deltaTime);
        sceneManager.render();

        // Render Crosshair
        batch.begin();
        batch.draw(crosshairTexture, Gdx.graphics.getWidth() / 2f - crosshairTexture.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - crosshairTexture.getHeight() / 2f);
        batch.end();

        // Render UI
        stageHandler.render();
    }

    @Override
    public void dispose() {
        sceneManager.dispose();
        sceneAsset.dispose();
        environmentCubemap.dispose();
        diffuseCubemap.dispose();
        specularCubemap.dispose();
        brdfLUT.dispose();
        skybox.dispose();

        stageHandler.dispose();

        // models
        chunkHandler.dispose();
        xyzModel.dispose();

        // 2D
        batch.dispose();
        crosshairTexture.dispose();
    }
}
