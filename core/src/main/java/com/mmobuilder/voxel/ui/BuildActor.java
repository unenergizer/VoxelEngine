package com.mmobuilder.voxel.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface BuildActor {

    /**
     * This will build a user interface {@link Actor}.
     *
     * @param stageHandler The main stage wrapper class
     * @return A built actor
     */
    Actor buildActor(final StageHandler stageHandler);
}
