package de.nerogar.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_8_8_8_8_REV;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;

import java.nio.IntBuffer;

public class Texture2D {

	private static int[] texturePositions = {
			GL_TEXTURE0, GL_TEXTURE1, GL_TEXTURE2, GL_TEXTURE3,
			GL_TEXTURE4, GL_TEXTURE5, GL_TEXTURE6, GL_TEXTURE7,
			GL_TEXTURE8, GL_TEXTURE9, GL_TEXTURE10, GL_TEXTURE11,
			GL_TEXTURE12, GL_TEXTURE13, GL_TEXTURE14, GL_TEXTURE15,
			GL_TEXTURE16, GL_TEXTURE17, GL_TEXTURE18, GL_TEXTURE19,
			GL_TEXTURE20, GL_TEXTURE21, GL_TEXTURE22, GL_TEXTURE23,
			GL_TEXTURE24, GL_TEXTURE25, GL_TEXTURE26, GL_TEXTURE27,
			GL_TEXTURE28, GL_TEXTURE29, GL_TEXTURE30, GL_TEXTURE31,
	};

	public enum InterpolationType {
		LINEAR(GL_LINEAR),
		NEAREST(GL_NEAREST);

		public final int openglConstant;

		InterpolationType(int openglConstant) {
			this.openglConstant = openglConstant;
		}

	}

	public enum DataType {
		BGRA_8_8_8_8I(GL_RGBA8, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV),
		BGRA_32_32_32_32F(GL_RGBA32F, GL_BGRA, GL_FLOAT),
		DEPTH(GL_DEPTH_COMPONENT32, GL_DEPTH_COMPONENT, GL_FLOAT);

		public final int internal;
		public final int format;
		public final int type;

		DataType(int internal, int format, int type) {
			this.internal = internal;
			this.format = format;
			this.type = type;
		}

	}

	private int id;
	private String filename;
	private String name;
	private int width;
	private int height;
	private InterpolationType interpolationType;
	private DataType dataType;

	private boolean initialized;

	public Texture2D(String name, int width, int height) {
		this(name, width, height, null, InterpolationType.LINEAR, DataType.BGRA_8_8_8_8I);
	}

	public Texture2D(String name, int width, int height, IntBuffer colorBuffer, InterpolationType interpolationType, DataType dataType) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.interpolationType = interpolationType;
		this.dataType = dataType;

		createTexture(colorBuffer);
	}

	protected void createTexture(IntBuffer colorBuffer) {
		if (initialized) cleanup();

		id = glGenTextures();

		bind();

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, interpolationType.openglConstant);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, interpolationType.openglConstant);

		glTexImage2D(GL_TEXTURE_2D, 0, dataType.internal, width, height, 0, dataType.format, dataType.type, colorBuffer);

		initialized = true;
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getFilename() {
		return filename;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void bind(int slot) {
		glActiveTexture(texturePositions[slot]);
		glBindTexture(GL_TEXTURE_2D, id);
		glActiveTexture(texturePositions[0]);
	}

	public void cleanup() {
		glDeleteTextures(id);
		Texture2DLoader.unloadTexture(filename);
		initialized = false;
	}

	@Override
	protected void finalize() throws Throwable {
		if (initialized) System.err.println("Texture not cleaned up. name: " + name);
	}

}
