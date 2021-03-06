package de.nerogar.render;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import de.nerogar.render.Texture2D.DataType;
import de.nerogar.render.Texture2D.InterpolationType;

public class RenderTarget {

	private static final int[] glColorAttachments = {
			GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1,
			GL_COLOR_ATTACHMENT2, GL_COLOR_ATTACHMENT3,
			GL_COLOR_ATTACHMENT4, GL_COLOR_ATTACHMENT5,
			GL_COLOR_ATTACHMENT6, GL_COLOR_ATTACHMENT7,
			GL_COLOR_ATTACHMENT8, GL_COLOR_ATTACHMENT9,
			GL_COLOR_ATTACHMENT10, GL_COLOR_ATTACHMENT11,
			GL_COLOR_ATTACHMENT12, GL_COLOR_ATTACHMENT13,
			GL_COLOR_ATTACHMENT14, GL_COLOR_ATTACHMENT15,
	};

	private boolean initialized;

	public ScreenProperties screenProperties;

	private int framebufferID; //, colorTextureID, normalTextureID, depthTextureID;

	private boolean useDepthTexture;
	private Texture2D depthTexture;

	private List<Texture2D> textures;

	public RenderTarget(boolean useDepthTexture, Texture2D... textures) {
		this.useDepthTexture = useDepthTexture;
		this.textures = new ArrayList<Texture2D>();
		if (useDepthTexture) activateDepthTexture();
		addTextures(textures);
	}

	protected void setScreenProperties(ScreenProperties screenProperties) {
		this.screenProperties = screenProperties;

		setResolution(screenProperties.getRenderWidth(), screenProperties.getRenderHeight());
	}

	private void activateDepthTexture() {
		useDepthTexture = true;
		depthTexture = new Texture2D("depth", 0, 0, null, InterpolationType.NEAREST, DataType.DEPTH);
	}

	private void addTextures(Texture2D... textures) {
		for (Texture2D texture : textures) {
			if (getTexture(texture.getName()) == null) {
				this.textures.add(texture);
			}
		}

		if (screenProperties != null) setResolution(screenProperties.getRenderWidth(), screenProperties.getRenderHeight());
	}

	public Texture2D getTexture(String name) {
		if (name.equals("depth")) return depthTexture;
		for (Texture2D texture : textures) {
			if (texture.getName().equals(name)) return texture;
		}

		return null;
	}

	public void removeTexture(String name) {
		for (int i = 0; i < textures.size(); i++) {
			if (textures.get(i).getName().equals(name)) {
				textures.remove(i);
				return;
			}
		}
	}

	private void setResolution(int width, int height) {
		if (initialized) cleanup();

		framebufferID = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, framebufferID);

		for (int i = 0; i < textures.size(); i++) {
			Texture2D texture = textures.get(i);
			texture.setWidth(width);
			texture.setHeight(height);
			texture.createTexture(null);
			glFramebufferTexture2D(GL_FRAMEBUFFER, glColorAttachments[i], GL_TEXTURE_2D, texture.getID(), 0);
		}

		if (useDepthTexture) {
			depthTexture.setWidth(width);
			depthTexture.setHeight(height);
			depthTexture.createTexture(null);

			/*glBindTexture(GL_TEXTURE_2D, depthTextureID);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (java.nio.ByteBuffer) null);*/

			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexture.getID(), 0);
		}

		setDrawBuffers();
		initialized = true;
	}

	private void setDrawBuffers() {
		int[] indexes = new int[textures.size()];
		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = glColorAttachments[i];
		}
		IntBuffer indexBuffer = BufferUtils.createIntBuffer(indexes.length);
		indexBuffer.put(indexes);
		indexBuffer.flip();
		glDrawBuffers(indexBuffer);
	}

	public void bind() {
		if (!initialized) throw new IllegalStateException("RenderScene not initialized");
		glBindFramebuffer(GL_FRAMEBUFFER, framebufferID);
	}

	

	public static void bindDefault() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public int getFramebufferID() {
		return framebufferID;
	}

	public void cleanup() {
		if (useDepthTexture) {
			depthTexture.cleanup();
			useDepthTexture = false;
		}

		for (Texture2D texture : textures) {
			texture.cleanup();
		}

		glDeleteFramebuffers(framebufferID);
		initialized = false;
	}
	@Override
	protected void finalize() throws Throwable {
		if (initialized) System.err.println("render Target not cleaned up. id: " + framebufferID);
	}

}
