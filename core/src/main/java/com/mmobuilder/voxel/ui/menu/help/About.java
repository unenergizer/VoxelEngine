package com.mmobuilder.voxel.ui.menu.help;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.mmobuilder.voxel.ui.BuildActor;
import com.mmobuilder.voxel.ui.HideableVisWindow;
import com.mmobuilder.voxel.ui.StageHandler;

public class About extends HideableVisWindow implements BuildActor {

    public About() {
        super("About VoxelEngine");
    }

    @Override
    public Actor buildActor(StageHandler stageHandler) {

        TableUtils.setSpacingDefaults(this);
        VisLabel textArea = new VisLabel("This is a work in progress voxel engine with gltf and bullet support.");
        textArea.setWrap(true);

        VisTable infoTable = new VisTable(true);
        infoTable.add(textArea).grow().expand().padTop(5).row();
        infoTable.add(new VisLabel("Code:")).padTop(8).row();
        infoTable.add(new VisLabel("unenergizer")).row();
        infoTable.add(new VisLabel("3D Models:")).padTop(8).row();
        infoTable.add(new VisLabel("deaDParrot")).row();

        VisTextButton licenseButton = new VisTextButton("License");
        VisTextButton gitHubButton = new VisTextButton("GitHub");
        VisTextButton discordButton = new VisTextButton("Discord");

        licenseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://github.com/unenergizer/VoxelEngine/blob/master/LICENSE");
            }
        });
        gitHubButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://github.com/unenergizer/VoxelEngine");
            }
        });
        discordButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://libgdx.com/community/discord/");
            }
        });


        VisTable linkTable = new VisTable(true);
        linkTable.add(licenseButton);
        linkTable.addSeparator(true);
        linkTable.add(gitHubButton);
        linkTable.addSeparator(true);
        linkTable.add(discordButton);

        add(infoTable).align(Alignment.LEFT.getAlignment()).growX();
        add(new VisTable()).row();
        add(linkTable).colspan(2).padTop(20);
        addListener(new OpenAboutWindowEventListener() {
            @Override
            protected void handleEvent() {
                fadeIn();
                setVisible(true);
            }
        });
        addListener(new StageHandler.ResizeWindowEventListener() {
            @Override
            protected void handleEvent(float width, float height) {
                centerWindow();
            }
        });

        addCloseButton();
        setVisible(false);
        centerWindow();
        pack();
        stopWindowClickThrough();
        return this;
    }

    public static class OpenAboutWindowEvent extends Event {
    }

    public abstract static class OpenAboutWindowEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (!(event instanceof OpenAboutWindowEvent)) return false;
            handleEvent();
            return true;
        }

        protected abstract void handleEvent();
    }
}
