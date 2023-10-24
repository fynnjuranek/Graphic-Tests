package de.eder.engine;

import de.eder.Window;
import de.eder.renderer.Mesh;
import de.eder.renderer.Render;
import de.eder.scenes.Scene;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.glClearColor;

public class Engine {
    private final Window window;
    private Render render;
    private boolean running;
    private Scene scene;
    private int targetFps;
    private int targetUps;

    private long glfwWindow;

    public Engine() {
        window = Window.get();
        glfwWindow = window.getGlfwWindow();

        try {
            render = new Render();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        scene = new Scene();
        running = true;
    }

    public void run() {

        float[] positions = new float[] {
                -0.5f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f,
        };

        float[] colors = new float[] {
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };

        int[] indices = new int[] {
                0, 1, 3, 3, 1, 2,
        };


        Mesh mesh = new Mesh(positions, colors, indices);
        scene.addMesh("quad", mesh);

        loop();

        cleanup();
    }

    private void loop() {
        double startTime = glfwGetTime();

        // set the clear color
        glClearColor(0.1f, 0.3f, 0.0f, 0.0f);

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            render.render(window, scene);
            // Swap front and back buffers / color buffers
            glfwSwapBuffers(glfwWindow);

            // Poll for and process events
        }

    }

    public void cleanup() {

        render.cleanup();
        scene.cleanup();
        window.cleanup();
    }
}
