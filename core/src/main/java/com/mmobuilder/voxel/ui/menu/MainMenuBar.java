package com.mmobuilder.voxel.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.mmobuilder.voxel.ui.BuildActor;
import com.mmobuilder.voxel.ui.FileExplorer;
import com.mmobuilder.voxel.ui.StageHandler;
import com.mmobuilder.voxel.ui.menu.help.About;
import com.mmobuilder.voxel.ui.menu.help.Controls;

public class MainMenuBar extends VisTable implements BuildActor {

    @Override
    public Actor buildActor(StageHandler stageHandler) {
        setFillParent(true);

        MenuBar menuBar = new MenuBar();
        menuBar.addMenu(fileMenu(stageHandler));
        menuBar.addMenu(helpMenu(stageHandler));

        add(menuBar.getTable()).expandX().fillX().row();
        add().expand().fill();

        pack();
        return this;
    }

    private Menu fileMenu(StageHandler stageHandler) {
        Menu fileMenu = new Menu("File");

        MenuItem openFile = new MenuItem("Open File");
        openFile.addListener(new OpenFileButtonEventListener() {
            @Override
            protected void handleEvent() {
                openFile.setDisabled(false);
            }
        });
        openFile.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stageHandler.fireEvent(new FileExplorer.OpenFileChooserEvent(FileChooser.Mode.OPEN));
            }
        });

        MenuItem exit = new MenuItem("Exit");
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        fileMenu.addItem(openFile);
        fileMenu.addItem(exit);

        return fileMenu;
    }

    private Menu helpMenu(StageHandler stageHandler) {
        Menu helpMenu = new Menu("Help");
        helpMenu.addItem(new MenuItem("About", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stageHandler.fireEvent(new About.OpenAboutWindowEvent());
            }
        }));
        helpMenu.addItem(new MenuItem("Controls", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stageHandler.fireEvent(new Controls.OpenControlsWindowEvent());
            }
        }));

        return helpMenu;
    }

    public static class OpenFileButtonEvent extends Event {
    }

    public abstract static class OpenFileButtonEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (!(event instanceof OpenFileButtonEvent)) return false;
            handleEvent();
            return true;
        }

        protected abstract void handleEvent();
    }
}
