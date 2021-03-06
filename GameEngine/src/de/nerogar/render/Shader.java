package de.nerogar.render;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

import java.io.*;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;

public class Shader {

	private int shaderHandle;

	private String vertexShaderFile, vertexShader;
	private String fragmentShaderFile, fragmentShader;
	private int vertexShaderHandle, fragmentShaderHandle;

	private Map<String, Integer> uniformCache;

	private boolean active;
	private boolean compiled;

	public Shader(String vertexShaderFile, String fragmentShaderFile) {
		this.vertexShaderFile = vertexShaderFile;
		this.fragmentShaderFile = fragmentShaderFile;

		uniformCache = new HashMap<String, Integer>();

		reloadFiles();
		reCompile();
	}

	//uniforms

	private int getUniformLocation(String name) {
		Integer location = uniformCache.get(name);
		if (location != null) return location;

		location = glGetUniformLocation(shaderHandle, name);
		uniformCache.put(name, location);
		return location;
	}

	//float
	public void setUniformf(String name, float[] values) {
		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(values.length);
		floatBuffer.put(values);
		floatBuffer.flip();
		setUniformf(name, floatBuffer);
	}

	public void setUniformf(String name, FloatBuffer floatBuffer) {
		glUniform1(getUniformLocation(name), floatBuffer);
	}

	public void setUniform1f(String name, float f0) {
		glUniform1f(getUniformLocation(name), f0);
	}

	public void setUniform2f(String name, float f0, float f1) {
		glUniform2f(getUniformLocation(name), f0, f1);
	}

	public void setUniform3f(String name, float f0, float f1, float f2) {
		glUniform3f(getUniformLocation(name), f0, f1, f2);
	}

	public void setUniform4f(String name, float f0, float f1, float f2, float f3) {
		glUniform4f(getUniformLocation(name), f0, f1, f2, f3);
	}

	public void setUniformMat4f(String name, FloatBuffer buffer) {
		glUniformMatrix4(getUniformLocation(name), true, buffer);
	}

	//int
	public void setUniform1i(String name, int i0) {
		glUniform1i(getUniformLocation(name), i0);
	}

	public void setUniform2i(String name, int i0, int i1) {
		glUniform2i(getUniformLocation(name), i0, i1);
	}

	public void setUniform3i(String name, int i0, int i1, int i2) {
		glUniform3i(getUniformLocation(name), i0, i1, i2);
	}

	public void setUniform4i(String name, int i0, int i1, int i2, int i3) {
		glUniform4i(getUniformLocation(name), i0, i1, i2, i3);
	}

	//boolean
	public void setUniform1bool(String name, boolean b0) {
		glUniform1i(getUniformLocation(name), b0 ? 1 : 0);
	}

	//end uniforms

	protected int getShaderHandle() {
		return shaderHandle;
	}

	public void reCompile() {
		if (compiled) {
			cleanup();
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
				System.err.println("Shader program wasn't linked correctly. Error log:");
				System.err.println(glGetProgramInfoLog(shaderHandle, 1024));
				compileError = true;
			}
		}

		glDeleteShader(vertexShaderHandle);
		glDeleteShader(fragmentShaderHandle);

		if (!compileError) {
			compiled = true;
		} else {
			cleanup();
		}
	}

	private String readFile(String filename) {
		filename = filename.replaceAll("\\\\", "/"); //replace \ with /

		String folder = filename.replaceAll("[^/]*$", "");

		StringBuilder text = new StringBuilder();

		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = fileReader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#insert ")) {
					line = folder + line.substring(8);

					text.append(readFile(line));

				} else {
					text.append(line).append("\n");
				}
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
		} else if (!compiled) {
			//System.err.println("Shader is not compiled. " + toString());
		}
	}

	public void deactivate() {
		if (active) {
			active = false;
			glUseProgram(0);
		}
	}

	public void cleanup() {
		glDeleteProgram(shaderHandle);
		uniformCache.clear();
		compiled = false;
	}

	@Override
	public String toString() {
		return "[" + shaderHandle + "; " + vertexShaderFile + "; " + fragmentShaderFile + "]";
	}

	@Override
	protected void finalize() throws Throwable {
		if (compiled) System.err.println("Shader not cleaned up. " + toString());
	}

}
