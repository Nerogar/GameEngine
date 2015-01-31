package de.nerogar.render;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

import java.io.*;
import java.nio.FloatBuffer;
import java.util.HashMap;

public class Shader {

	public int shaderHandle;
	public HashMap<String, Integer> uniforms;
	public HashMap<String, Integer> attributes;

	private String vertexShaderFile, vertexShader;
	private String fragmentShaderFile, fragmentShader;
	private int vertexShaderHandle, fragmentShaderHandle;

	private boolean active;
	private boolean compiled;

	public Shader(String vertexShaderFile, String fragmentShaderFile) {
		this.vertexShaderFile = vertexShaderFile;
		this.fragmentShaderFile = fragmentShaderFile;

		reloadFiles();
		reCompile();
	}

	public void setUniformf(String name, float[] values) {
		FloatBuffer floatBuffer = FloatBuffer.allocate(values.length);
		floatBuffer.put(values);
		floatBuffer.flip();
		glUniform1(glGetUniformLocation(shaderHandle, name), floatBuffer);
	}

	public void reCompile() {
		if (compiled) {
			glDeleteProgram(shaderHandle);
			compiled = false;
		}

		shaderHandle = glCreateProgram();
		vertexShaderHandle = glCreateShader(GL_VERTEX_SHADER);
		fragmentShaderHandle = glCreateShader(GL_FRAGMENT_SHADER);

		boolean compileError = false;

		glShaderSource(vertexShaderHandle, vertexShader);
		glCompileShader(vertexShaderHandle);
		if (glGetShaderi(vertexShaderHandle, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Vertex shader wasn't able to be compiled correctly. Error log:");
			System.err.println(glGetShaderInfoLog(vertexShaderHandle, 1024));
			compileError = true;
		}

		if (!compileError) {
			glShaderSource(fragmentShaderHandle, fragmentShader);
			glCompileShader(fragmentShaderHandle);
			if (glGetShaderi(fragmentShaderHandle, GL_COMPILE_STATUS) == GL_FALSE) {
				System.err.println("Fragment shader wasn't able to be compiled correctly. Error log:");
				System.err.println(glGetShaderInfoLog(fragmentShaderHandle, 1024));
			}
		}

		if (!compileError) {
			glAttachShader(shaderHandle, vertexShaderHandle);
			glAttachShader(shaderHandle, fragmentShaderHandle);

			glLinkProgram(shaderHandle);
			if (glGetProgrami(shaderHandle, GL_LINK_STATUS) == GL_FALSE) {
				System.err.println("Shader program wasn't linked correctly.");
				System.err.println(glGetProgramInfoLog(shaderHandle, 1024));
				compileError = true;
			}
			glDeleteShader(vertexShaderHandle);
			glDeleteShader(fragmentShaderHandle);
		}

		if (!compileError) {
			compiled = true;
		}
	}

	public String readFile(String filename) {
		StringBuilder text = new StringBuilder();

		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = fileReader.readLine()) != null) {
				text.append(line).append("\n");
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("loaded shader: " + filename);

		return text.toString();
	}

	public void reloadFiles() {
		vertexShader = readFile(vertexShaderFile);
		fragmentShader = readFile(fragmentShaderFile);
	}

	public void activate() {
		if (!active && compiled) {
			active = true;
			glUseProgram(shaderHandle);
		}
	}

	public void deactivate() {
		if (active) {
			active = false;
			glUseProgram(0);
		}
	}

	public void setResolution(float x, float y) {
		activate();

		glUniform2f(glGetUniformLocation(shaderHandle, "resolution"), x, y);

		deactivate();
	}
}
