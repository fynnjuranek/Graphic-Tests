package de.eder.engine;

import de.eder.Window;
import de.eder.models.Entity;
import de.eder.models.Model;
import de.eder.renderer.Mesh;
import de.eder.renderer.Render;
import de.eder.scenes.Scene;
import org.joml.Vector4f;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.glClearColor;

public class Engine {
    private final Window window;
    private Render render;
    private boolean running;
    private Scene scene;
    private Entity cubeEntity;
    private Vector4f displInc = new Vector4f();
    private float rotation;
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

        scene = new Scene(window.getWidth(), window.getHeight());
        running = true;
    }

    public void run() {

        float[] positions = new float[] {
                // VO
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,
        };

        float[] colors = new float[] {
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };

        int[] indices = new int[] {
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                4, 0, 3, 5, 4, 3,
                // Right face
                3, 2, 7, 5, 3, 7,
                // Left face
                6, 1, 0, 6, 0, 4,
                // Bottom face
                2, 1, 6, 2, 6, 7,
                // Back face
                7, 6, 4, 7, 4, 5,
        };

        List<Mesh> meshList = new ArrayList<>();
        Mesh mesh = new Mesh(positions, colors, indices);
        meshList.add(mesh);

        String cubeModelId = "cube-model";
        Model model = new Model(cubeModelId, meshList);
        scene.addModel(model);

        cubeEntity = new Entity("cube-entity", cubeModelId);
        cubeEntity.setPosition(0, 0, -2);
        scene.addEntity(cubeEntity);

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

    private void resize() {
        scene.resize(window.getWidth(), window.getHeight());
    }
}
