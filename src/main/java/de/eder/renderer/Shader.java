package de.eder.renderer;

import de.eder.Utils;
import org.lwjgl.opengl.GL30;

import java.awt.desktop.SystemSleepEvent;
import java.util.*;


import static org.lwjgl.opengl.GL30.*;

public class Shader {

    private final int programId;

    public Shader(List<ShaderModuleData> shaderModuleDataList) throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create shader");
        }
        List<Integer> shaderModules = new ArrayList<>();
        shaderModuleDataList.forEach(s -> {
            try {
                shaderModules.add(createShader(Utils.readFile(s.shaderFile), s.shaderType));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        link(shaderModules);
    }

    protected int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link(List<Integer> shaderModules) throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        shaderModules.forEach(shader -> glDetachShader(programId, shader));
        shaderModules.forEach(GL30::glDeleteShader);
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    public void validate() {
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            throw new RuntimeException("Error validating shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    public record ShaderModuleData(String shaderFile, int shaderType) {

    }
}
