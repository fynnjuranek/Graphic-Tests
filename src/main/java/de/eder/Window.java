package de.eder;

import de.eder.engine.KeyListener;
import de.eder.renderer.Render;
import de.eder.scenes.Scene;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static Window window;
    private Render render;
    private Scene scene;

    private long glfwWindow;

    private int width;
    private int height;
    private String title;

    private Window() {
        this.width = 1280;
        this.height = 720;
        this.title = "Test Window";
        init();
    }

    public static Window get() {
        if (window == null) {
            window = new Window();

        }
        return window;
    }

    private void init() {
        // Error callback
        GLFWErrorCallback.createPrint(System.err).set();



        // To initialize glfw-library
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }


        glfwDefaultWindowHints();

        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if (glfwWindow == NULL) {
            glfwTerminate();
            System.out.println("Unable to create the window");
        }

        glfwSetKeyCallback(glfwWindow,KeyListener::keyCallback);

        glfwMakeContextCurrent(glfwWindow);

        // V-Sync
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);


    }




    public void cleanup() {
        // Free callbacks and memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // detach shader
        // TODO

        glfwTerminate();

        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null) {
            callback.free();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getGlfwWindow() {
        return glfwWindow;
    }
}
