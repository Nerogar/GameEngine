package de.nerogar.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public abstract class Renderable {
	private int vboHandle;
	private boolean vboInitialized;

	private float[] vertexData, textureData, normalData;
	private int vertexComponentCound, textureComponentCound, normalComponentCound;
	private int vertexDataSize;
	private int vertexCount;

	//private Shader transformShader;

	protected void setVertexData(float[] vertexData, int componentCount) {
		this.vertexData = vertexData;
		this.vertexComponentCound = componentCount;
	}

	protected void setTextureData(float[] textureData, int componentCount) {
		this.textureData = textureData;
		this.textureComponentCound = componentCount;
	}

	protected void setNormalData(float[] normalData, int componentCount) {
		this.normalData = normalData;
		this.normalComponentCound = componentCount;
	}

	protected void initVBO() {
		if (vboInitialized) cleanup();

		float[] vboData = new float[vertexData.length + textureData.length + normalData.length];
		vertexCount = vertexData.length / vertexComponentCound;
		vertexDataSize = vertexComponentCound + textureComponentCound + normalComponentCound;

		for (int vertexIndex = 0; vertexIndex < vertexData.length / vertexComponentCound; vertexIndex++) {
			int vboOffset = 0;

			for (int i = vertexIndex * vertexComponentCound; i < (vertexIndex + 1) * vertexComponentCound; i++) {
				vboData[vertexDataSize * vertexIndex + vboOffset] = vertexData[i];
				vboOffset++;
			}

			for (int i = vertexIndex * textureComponentCound; i < (vertexIndex + 1) * textureComponentCound; i++) {
				vboData[vertexDataSize * vertexIndex + vboOffset] = textureData[i];
				vboOffset++;
			}

			for (int i = vertexIndex * normalComponentCound; i < (vertexIndex + 1) * normalComponentCound; i++) {
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
		glVertexPointer(vertexComponentCound, GL_FLOAT, vertexDataSize * 4, 0L);
		glTexCoordPointer(textureComponentCound, GL_FLOAT, vertexDataSize * 4, vertexComponentCound * 4);
		glNormalPointer(GL_FLOAT, vertexDataSize * 4, (vertexComponentCound + textureComponentCound) * 4);

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

	private void cleanup() {
		glDeleteBuffers(vboHandle);
	}

}
