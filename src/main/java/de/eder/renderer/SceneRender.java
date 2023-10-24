package de.eder.renderer;

import de.eder.models.Entity;
import de.eder.models.Model;
import de.eder.scenes.Scene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class SceneRender {
    private Shader shader;
    private UniformsMap uniformsMap;

    public SceneRender() {
        List<Shader.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new Shader.ShaderModuleData("assets/shaders/scene.vert", GL_VERTEX_SHADER));
        shaderModuleDataList.add(new Shader.ShaderModuleData("assets/shaders/scene.frag", GL_FRAGMENT_SHADER));
        try {
            shader = new Shader(shaderModuleDataList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        createUniforms();
    }

    public void cleanup() {
        shader.cleanup();
    }

    public void render(Scene scene) {
        shader.bind();
        uniformsMap.setUniform("projectionMatrix", scene.getProjection().getProjMatrix());

        Collection<Model> models = scene.getModelMap().values();
        for (Model model : models) {
            model.getMeshList().stream().forEach(mesh -> {
                glBindVertexArray(mesh.getVaoId());
                List<Entity> entities = model.getEntitiesList();
                for (Entity entity : entities) {
                    uniformsMap.setUniform("modelMatrix", entity.getModelMatrix());
                    glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
                }
            });
        }

        glBindVertexArray(0);
        shader.unbind();
    }

    private void createUniforms() {
        uniformsMap = new UniformsMap(shader.getProgramId());
        uniformsMap.createUniform("modelMatrix");
        uniformsMap.createUniform("projectionMatrix");
    }
}
