package com.mmobuilder.voxel.ui.menu.help;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.mmobuilder.voxel.ui.*;
import com.mmobuilder.voxel.ui.menu.MainMenuBar;

public class Controls extends HideableVisWindow implements BuildActor, ForcePosition {

    private final MainMenuBar mainMenuBar;

    public Controls(MainMenuBar mainMenuBar) {
        super("Controls");
        this.mainMenuBar = mainMenuBar;
    }

    @Override
    public Actor buildActor(StageHandler stageHandler) {

        TableUtils.setSpacingDefaults(this);
        VisTable infoTable = new VisTable(true);
        infoTable.add(new VisLabel("Move: W,S,A & D Keys")).row();
        infoTable.add(new VisLabel("Fly Up/Down: space & left-shift Keys")).row();
        infoTable.addSeparator();
        infoTable.add(new VisLabel("Release Mouse: Escape Key")).row();
        infoTable.add(new VisLabel("Engage Mouse: Mouse Left-Click 3d area")).row();
        infoTable.add(new VisLabel("Green Cursor: Mouse is locked into the game camera.")).row();
        infoTable.add(new VisLabel("Red Cursor: Mouse is unlocked and free to move.")).row();
        infoTable.addSeparator();
        infoTable.add(new VisLabel("User Interface: 'Release' the mouse to use it.")).row();

        add(infoTable).align(Alignment.LEFT.getAlignment()).growX();
        add(new VisTable()).row();
        addListener(new OpenControlsWindowEventListener() {
            @Override
            protected void handleEvent() {
                fadeIn();
                setVisible(true);
            }
        });
        addListener(new StageHandler.ResizeWindowEventListener() {
            @Override
            protected void handleEvent(float width, float height) {
                setPosition();
            }
        });

        addCloseButton();
        pack();
        setPosition();
        setVisible(true);
        stopWindowClickThrough();
        return this;
    }

    public void setPosition() {
        // Put to left of screen under the main menu bar.
        setPosition(UiConstants.PADDING, Gdx.graphics.getHeight() - mainMenuBar.getHeight() - getHeight() - UiConstants.PADDING);
    }

    public static class OpenControlsWindowEvent extends Event {
    }

    public abstract static class OpenControlsWindowEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (!(event instanceof OpenControlsWindowEvent)) return false;
            handleEvent();
            return true;
        }

        protected abstract void handleEvent();
    }
}
