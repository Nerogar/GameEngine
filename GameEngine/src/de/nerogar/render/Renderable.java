package de.nerogar.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glMultTransposeMatrix;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

public abstract class Renderable {
	private int vboHandle;
	private boolean vboInitialized;

	private int vertexDataSize;
	private int vertexCount;

	private int vertexComponentCount;
	private int textureComponentCount;
	//private int normalComponentCount; //unused

	private List<String> attributeName;
	private List<float[]> attributeData;
	private List<Integer> attributeComponentCount;
	private List<Integer> attributeLocation;
	private int attributeComponentCountTotal;

	private Shader shader;

	public Renderable() {
		attributeName = new ArrayList<String>();
		attributeData = new ArrayList<float[]>();
		attributeComponentCount = new ArrayList<Integer>();
		attributeLocation = new ArrayList<Integer>();
	}

	public void addAttributeArray(String attributeName, float[] attributeData, int attributeComponentCount, Shader shader) {
		this.attributeName.add(attributeName);
		this.attributeData.add(attributeData);
		this.attributeComponentCount.add(attributeComponentCount);
		this.attributeComponentCountTotal += attributeComponentCount;
	}

	protected void initVBO(float[] vertexData, int vertexComponentCount, float[] textureData, int textureComponentCount, float[] normalData, int normalComponentCount) {
		this.vertexComponentCount = vertexComponentCount;
		this.textureComponentCount = textureComponentCount;
		//this.normalComponentCount = normalComponentCount; //unused

		if (vboInitialized) cleanup();

		float[] vboData = new float[vertexData.length + textureData.length + normalData.length];
		vertexCount = vertexData.length / vertexComponentCount;
		vertexDataSize = vertexComponentCount + textureComponentCount + normalComponentCount + attributeComponentCountTotal;

		for (int vertexIndex = 0; vertexIndex < vertexData.length / vertexComponentCount; vertexIndex++) {
			int vboOffset = 0;

			for (int i = vertexIndex * vertexComponentCount; i < (vertexIndex + 1) * vertexComponentCount; i++) {
				vboData[vertexDataSize * vertexIndex + vboOffset] = vertexData[i];
				vboOffset++;
			}

			for (int i = vertexIndex * textureComponentCount; i < (vertexIndex + 1) * textureComponentCount; i++) {
				vboData[vertexDataSize * vertexIndex + vboOffset] = textureData[i];
				vboOffset++;
			}

			for (int i = vertexIndex * normalComponentCount; i < (vertexIndex + 1) * normalComponentCount; i++) {
				vboData[vertexDataSize * vertexIndex + vboOffset] = normalData[i];
				vboOffset++;
			}

			for (int attIndex = 0; attIndex < attributeData.size(); attIndex++) {
				for (int i = vertexIndex * attributeComponentCount.get(attIndex); i < (vertexIndex + 1) * attributeComponentCount.get(attIndex); i++) {
					vboData[vertexDataSize * vertexIndex + vboOffset] = attributeData.get(attIndex)[i];
					vboOffset++;
				}
			}
		}

		vboHandle = glGenBuffers();

		FloatBuffer vboBuffer = BufferUtils.createFloatBuffer(vboData.length);
		vboBuffer.put(vboData);
		vboBuffer.flip();

		glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
		glBufferData(GL_ARRAY_BUFFER, vboBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		for (int i = 0; i < attributeName.size(); i++) {
			attributeLocation.add(8 + i);
			glBindAttribLocation(shader.getShaderHandle(), attributeLocation.get(i), attributeName.get(i));
		}

		vertexData = null;
		textureData = null;
		normalData = null;

		vboInitialized = true;

	}

	/**
	 * @param renderProperties renderProperties for transforming the object
	 *        or <b>null</b>, if you wat to handle transformation yourself
	 */
	public void render(RenderProperties renderProperties) {
		if (!vboInitialized) throw new RuntimeException("Renderable not initialized");

		if (renderProperties != null) {
			glPushMatrix();
			glMultTransposeMatrix(renderProperties.getModelMatrix().asBuffer());
		}

		//render
		glBindBuffer(GL_ARRAY_BUFFER, vboHandle);

		glVertexPointer(vertexComponentCount, GL_FLOAT, vertexDataSize * Float.BYTES, 0L);
		glTexCoordPointer(textureComponentCount, GL_FLOAT, vertexDataSize * Float.BYTES, vertexComponentCount * Float.BYTES);
		glNormalPointer(GL_FLOAT, vertexDataSize * Float.BYTES, (vertexComponentCount + textureComponentCount) * Float.BYTES);
		//glVertexAttribPointer(TILE_ID11_LOCATION, 1, GL_FLOAT, true, 20, 16);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
		//render end

		if (renderProperties != null) glPopMatrix();

	}

	public void cleanup() {
		glDeleteBuffers(vboHandle);
		vboInitialized = false;
	}

	@Override
	protected void finalize() throws Throwable {
		if (vboInitialized) System.err.println("VBO not cleaned up. id: " + vboHandle);
	}

}
