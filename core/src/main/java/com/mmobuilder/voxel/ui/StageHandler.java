package com.mmobuilder.voxel.ui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.mmobuilder.voxel.Main;
import com.mmobuilder.voxel.ui.debug.FPS;
import com.mmobuilder.voxel.ui.menu.help.About;
import com.mmobuilder.voxel.ui.menu.help.Controls;
import com.mmobuilder.voxel.ui.menu.MainMenuBar;
import lombok.AllArgsConstructor;

public class StageHandler extends ApplicationAdapter {
    private final UpdateActorEvent updateActorEvent = new UpdateActorEvent();
    private final Main main;
    private final PerspectiveCamera gameCamera;
    private OrthographicCamera uiCamera;
    private Stage stage;

    public StageHandler(Main main, PerspectiveCamera gameCamera) {
        this.main = main;
        this.gameCamera = gameCamera;
    }

    @Override
    public void create() {
        VisUI.load(Gdx.files.internal("ui/tixel/x1/tixel.json"));

        // Setup UI Camera and Viewport
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera.update();
        ScreenViewport screenViewport = new ScreenViewport(uiCamera);
        screenViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Setup the stage
        stage = new Stage();
        stage.setViewport(screenViewport);
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // Add and build all the user interface elements
        MainMenuBar mainMenuBar = new MainMenuBar();
        addActor(mainMenuBar);
        addActor(new FileExplorer());
        addActor(new About());
        addActor(new FPS(mainMenuBar, gameCamera, main.getChunkHandler()));
        addActor(new Controls(mainMenuBar));
        addActor(new FileSelectHack(main));

        // Debug Input
        stage.addListener(new InputListener() {
            boolean debug = false;

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.F12) {
                    debug = !debug;
                    root.setDebug(debug, true);
                    for (Actor actor : stage.getActors()) {
                        if (actor instanceof Group) {
                            Group group = (Group) actor;
                            group.setDebug(debug, true);
                        }
                    }
                    return true;
                }

                return false;
            }
        });
    }

    /**
     * Adds an Actor to the stage and builds the UI.
     *
     * @param actor The buildable actor we want to add to the stage.
     */
    public void addActor(BuildActor actor) {
        stage.addActor(actor.buildActor(this));
    }

    /**
     * Easy way to fire an event for all actors.
     *
     * @param event The event we want to fire.
     */
    public void fireEvent(Event event) {
        for (Actor actor : stage.getActors()) actor.fire(event);
    }

    @Override
    public void render() {
        uiCamera.update();
        fireEvent(updateActorEvent);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        VisUI.dispose();
    }

    public InputAdapter getInputAdapter() {
        return stage;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        fireEvent(new ResizeWindowEvent(width, height));
    }

    @AllArgsConstructor
    public static class ResizeWindowEvent extends Event {
        private final float width, height;
    }

    public abstract static class ResizeWindowEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (!(event instanceof ResizeWindowEvent)) return false;
            handleEvent(((ResizeWindowEvent) event).width, ((ResizeWindowEvent) event).height);
            return true;
        }

        protected abstract void handleEvent(float width, float height);
    }


    @AllArgsConstructor
    public static class UpdateActorEvent extends Event {
    }

    public abstract static class UpdateActorEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (!(event instanceof UpdateActorEvent)) return false;
            updateActorEvent();
            return true;
        }

        protected abstract void updateActorEvent();
    }
}
