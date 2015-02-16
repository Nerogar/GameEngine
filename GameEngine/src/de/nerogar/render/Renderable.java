package de.nerogar.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public abstract class Renderable {
	private int vboHandle;
	private boolean vboInitialized;

	private int vertexDataSize;
	private int vertexCount;

	private int vertexComponentCount;
	private int textureComponentCount;
	//private int normalComponentCount; //unused

	//private Shader transformShader;

	protected void initVBO(float[] vertexData, int vertexComponentCount, float[] textureData, int textureComponentCount, float[] normalData, int normalComponentCount) {
		this.vertexComponentCount = vertexComponentCount;
		this.textureComponentCount = textureComponentCount;
		//this.normalComponentCount = normalComponentCount; //unused

		if (vboInitialized) cleanup();

		float[] vboData = new float[vertexData.length + textureData.length + normalData.length];
		vertexCount = vertexData.length / vertexComponentCount;
		vertexDataSize = vertexComponentCount + textureComponentCount + normalComponentCount;

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
		}

		vboHandle = glGenBuffers();

		FloatBuffer vboBuffer = BufferUtils.createFloatBuffer(vboData.length);
		vboBuffer.put(vboData);
		vboBuffer.flip();

		glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
		glBufferData(GL_ARRAY_BUFFER, vboBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vertexData = null;
		textureData = null;
		normalData = null;

		vboInitialized = true;

	}

	/*public void setShader(Shader transformShader){
		this.transformShader = transformShader;
	}*/

	public void render(RenderProperties renderProperties) {
		if (!vboInitialized) throw new RuntimeException("Renderable not initialized");

		glPushMatrix();
		if (renderProperties.position != null) glTranslatef(renderProperties.position.get(0), renderProperties.position.get(1), renderProperties.position.get(2));
		if (renderProperties.rotation != null) {
			glRotatef(renderProperties.rotation.get(0), 1f, 0f, 0f);
			glRotatef(renderProperties.rotation.get(1), 0f, 1f, 0f);
			glRotatef(renderProperties.rotation.get(2), 0f, 0f, 1f);
		}
		if (renderProperties.scale != null) glScalef(renderProperties.scale.get(0), renderProperties.scale.get(1), renderProperties.scale.get(2));

		//render
		glBindBuffer(GL_ARRAY_BUFFER, vboHandle);

		glVertexPointer(vertexComponentCount, GL_FLOAT, vertexDataSize * 4, 0L);
		glTexCoordPointer(textureComponentCount, GL_FLOAT, vertexDataSize * 4, vertexComponentCount * 4);
		glNormalPointer(GL_FLOAT, vertexDataSize * 4, (vertexComponentCount + textureComponentCount) * 4);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
		//render end

		glPopMatrix();

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
