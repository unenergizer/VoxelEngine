package com.mmobuilder.voxel.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntSet;
import lombok.Getter;

public class FirstPersonMovementController extends InputAdapter {
    private static final int STRAFE_LEFT_KEY = Input.Keys.A;
    private static final int STRAFE_RIGHT_KEY = Input.Keys.D;
    private static final int FORWARD_KEY = Input.Keys.W;
    private static final int BACKWARD_KEY = Input.Keys.S;
    private static final int UP_KEY = Input.Keys.SPACE;
    private static final int DOWN_KEY = Input.Keys.SHIFT_LEFT;
    private final Camera camera;
    private final IntSet keys = new IntSet();
    @Getter
    private float velocity = 5;
    private final Vector3 tmp = new Vector3();

    public FirstPersonMovementController(Camera camera) {
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {
        keys.add(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys.remove(keycode);
        return true;
    }

    /**
     * Sets the velocity in units per second for moving forward, backward and strafing left/right.
     *
     * @param velocity the velocity in units per second
     */
    public void setVelocity(float velocity) {
        if (velocity <= 1) velocity = 1;
        this.velocity = velocity;
    }

    /**
     * Update camera position based on input and time elapsed since last frame.
     */
    public void update() {
        update(Gdx.graphics.getDeltaTime());
    }

    /**
     * Update camera position based on input and given time delta.
     *
     * @param deltaTime the time difference between the current frame and the last frame
     */
    public void update(float deltaTime) {
        tmp.setZero();
        if (keys.contains(FORWARD_KEY)) {
            tmp.add(camera.direction.cpy().nor().scl(deltaTime * velocity));
        }
        if (keys.contains(BACKWARD_KEY)) {
            tmp.add(camera.direction.cpy().nor().scl(-deltaTime * velocity));
        }
        if (keys.contains(STRAFE_LEFT_KEY)) {
            tmp.add(camera.direction.cpy().crs(camera.up).nor().scl(-deltaTime * velocity));
        }
        if (keys.contains(STRAFE_RIGHT_KEY)) {
            tmp.add(camera.direction.cpy().crs(camera.up).nor().scl(deltaTime * velocity));
        }
        if (keys.contains(UP_KEY)) {
            tmp.add(camera.up.cpy().nor().scl(deltaTime * velocity));
        }
        if (keys.contains(DOWN_KEY)) {
            tmp.add(camera.up.cpy().nor().scl(-deltaTime * velocity));
        }
        camera.position.add(tmp);
        camera.update(true);
    }
}

