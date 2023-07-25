package com.mmobuilder.voxel.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mmobuilder.voxel.Main;

/**
 * Launches the desktop (LWJGL3) application.
 */
public class Lwjgl3Launcher {

    private static final String aaSamples = "aaSamples:";

    public static void main(String[] args) {

        boolean uncapFPS = false;
        int antialiasingSamples = 0;

        for (String arg : args) {
            if (arg.equalsIgnoreCase("uncapFPS")) uncapFPS = true;
            if (arg.contains(aaSamples)) antialiasingSamples = Integer.parseInt(arg.replace(aaSamples, ""));
        }

        new Lwjgl3Application(new Main(), getDefaultConfiguration(uncapFPS, antialiasingSamples));
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration(boolean uncapFPS, int antialiasingSamples) {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("VoxelEngine");
        configuration.setWindowedMode(1280, 720);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");

        // Uncap FPS option
        if (uncapFPS) {
            configuration.useVsync(false);
        } else {
            configuration.useVsync(true);
            configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate);
        }

        // Antialiasing samples. You can also play around with higher values like 4
        configuration.setBackBufferConfig(8, 8, 8, 8, 16, 0, antialiasingSamples); // 8, 8, 8, 8, 16, 0 are default values
        return configuration;
    }
}
