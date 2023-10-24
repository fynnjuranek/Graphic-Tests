package de.eder.renderer;

import de.eder.scenes.Scene;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class SceneRender {
    private Shader shader;

    public SceneRender() {
        List<Shader.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new Shader.ShaderModuleData("assets/shaders/scene.vert", GL_VERTEX_SHADER));
        shaderModuleDataList.add(new Shader.ShaderModuleData("assets/shaders/scene.frag", GL_FRAGMENT_SHADER));
        try {
            shader = new Shader(shaderModuleDataList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanup() {
        shader.cleanup();
    }

    public void render(Scene scene) {
        shader.bind();
        scene.getMeshMap().values().forEach(mesh -> {
            glBindVertexArray(mesh.getVaoId());
            glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
        });
        glBindVertexArray(0);
    }
}
