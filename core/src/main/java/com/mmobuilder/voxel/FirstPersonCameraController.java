package com.mmobuilder.voxel;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

public class FirstPersonCameraController extends InputAdapter {

    private final Camera camera;

    // Constants
    private static final float MAX_PITCH = 89f;
    private static final float MIN_PITCH = -89f;
    private static final float ROTATE_SPEED = 0.2f;


    // Camera control variables
    private int lastMouseX, lastMouseY;
    private float pitch = 0;

    public FirstPersonCameraController(Camera camera) {
        this.camera = camera;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        float deltaX = lastMouseX - screenX;
        float deltaY = lastMouseY - screenY;

        // Rotating camera around Y axis
        camera.rotate(Vector3.Y, deltaX * ROTATE_SPEED);

        // Calculating the cross product of the direction and up vectors to get the right vector
        Vector3 right = camera.direction.cpy().crs(Vector3.Y).nor();

        // Calculate new pitch
        float newPitch = pitch - deltaY * ROTATE_SPEED;

        // Check if the new pitch would be within the range defined by MIN_PITCH and MAX_PITCH
        if (newPitch > MIN_PITCH && newPitch < MAX_PITCH) {
            // Rotating camera around this right vector to look up/down
            camera.rotate(right, deltaY * ROTATE_SPEED);
            pitch = newPitch;
        }

        // Enforce the up vector to be the Y-axis
        // This ensures the camera won't roll to the sides
        camera.up.set(Vector3.Y);

        // Update the camera's position
        camera.update();

        // Save the current mouse position for the next frame
        lastMouseX = screenX;
        lastMouseY = screenY;

        return true;
    }
}
