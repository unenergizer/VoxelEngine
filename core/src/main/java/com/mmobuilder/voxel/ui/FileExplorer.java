package com.mmobuilder.voxel.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Disposable;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;
import com.kotcrab.vis.ui.widget.file.StreamingFileChooserListener;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class FileExplorer extends VisTable implements BuildActor {

    @Override
    public Actor buildActor(StageHandler stageHandler) {
        // Set the file chooser preferences directory
        FileChooser.setDefaultPrefsName(getClass().getPackage().toString());
        FileChooser.setSaveLastDirectory(true);

        addListener(new OpenFileChooserEventListener() {
            @Override
            protected void handleFileChooserEvent(FileChooser.Mode mode) {
                stageHandler.addActor(new FileExplorerWindow(FileChooser.Mode.OPEN));
            }
        });

        setVisible(false);
        return this;
    }

    static class FileExplorerWindow extends FileChooser implements BuildActor, Disposable {

        private HighResFileChooserIconProvider highResFileChooserIconProvider;

        public FileExplorerWindow(Mode mode) {
            super(mode);
        }

        @Override
        public Actor buildActor(StageHandler stageHandler) {
            // Setup file filters
            final FileTypeFilter fileTypeFilter = new FileTypeFilter(false);
            fileTypeFilter.addRule("Image files (*.gltf)", "gltf");

            // Configure file chooser
            setFileTypeFilter(fileTypeFilter);
            setSize(800, 600);
            setSelectionMode(SelectionMode.FILES);
            setMultiSelectionEnabled(true);
            setFavoriteFolderButtonVisible(true);
            setShowSelectionCheckboxes(false);
            setIconProvider(highResFileChooserIconProvider = new HighResFileChooserIconProvider(this));

            setListener(new StreamingFileChooserListener() {
                @Override
                public void selected(FileHandle file) {
                    System.out.println(file.path());
                    stageHandler.fireEvent(new FileSelectedEvent(file));
                }
            });
            TableUtils.setSpacingDefaults(this);
            centerWindow();
            fadeIn();
            setVisible(true);
            toFront();
            return this;
        }

        @Override
        public void dispose() {
            // TODO: Figure out how to dispose of this...
            highResFileChooserIconProvider.dispose();
        }
    }

    /**
     * This event is called when the file explorer needs to be opened.
     */
    @AllArgsConstructor
    public static class OpenFileChooserEvent extends Event {
        @Getter
        private final FileChooser.Mode mode;
    }

    /**
     * Here we listen for incoming {@link OpenFileChooserEvent} and then open the window.
     */
    public abstract static class OpenFileChooserEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (!(event instanceof OpenFileChooserEvent)) return false;
            handleFileChooserEvent(((OpenFileChooserEvent) event).mode);
            return true;
        }

        protected abstract void handleFileChooserEvent(FileChooser.Mode mode);
    }

    /**
     * When a file is selected we call this event.
     */
    @AllArgsConstructor
    public static class FileSelectedEvent extends Event {
        @Getter
        private final FileHandle fileHandle;
    }

    /**
     * This is fired when a user has selected a file.
     */
    public abstract static class FileSelectedEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (!(event instanceof FileSelectedEvent)) return false;
            handleFileSelectionEvent(((FileSelectedEvent) event).fileHandle);
            return true;
        }

        protected abstract void handleFileSelectionEvent(FileHandle fileHandle);
    }
}
