package de.nerogar.render;

import static org.lwjgl.opengl.GL11.*;

public class RenderHelper {

	public static void renderFBOFullscreenQuad(ScreenProperties screenProperties) {
		glBegin(GL_QUADS);

		glTexCoord2f(0f, 0f);
		glVertex3f(0f, 0f, -1f);

		glTexCoord2f(1f, 0f);
		glVertex3f(screenProperties.getRenderWidth(), 0f, -1f);

		glTexCoord2f(1f, 1f);
		glVertex3f(screenProperties.getRenderWidth(), screenProperties.getRenderHeight(), -1f);

		glTexCoord2f(0f, 1f);
		glVertex3f(0f, screenProperties.getRenderHeight(), -1f);

		glEnd();
	}

	public static void renderTextureFullscreenQuad(ScreenProperties screenProperties) {
		glBegin(GL_QUADS);

		glTexCoord2f(0f, 1f);
		glVertex3f(0f, 0f, -1f);

		glTexCoord2f(1f, 1f);
		glVertex3f(screenProperties.getRenderWidth(), 0f, -1f);

		glTexCoord2f(1f, 0f);
		glVertex3f(screenProperties.getRenderWidth(), screenProperties.getRenderHeight(), -1f);

		glTexCoord2f(0f, 0f);
		glVertex3f(0f, screenProperties.getRenderHeight(), -1f);

		glEnd();
	}
	
	public static void renderFBOQuad(ScreenProperties screenProperties, float width, float height, float posX, float posY) {
		glBegin(GL_QUADS);

		glTexCoord2f(0f, 0f);
		glVertex3f(screenProperties.getRenderWidth() * (posX), screenProperties.getRenderHeight() * (posY), -1f);

		glTexCoord2f(1f, 0f);
		glVertex3f(screenProperties.getRenderWidth() * (posX + width), screenProperties.getRenderHeight() * (posY), -1f);

		glTexCoord2f(1f, 1f);
		glVertex3f(screenProperties.getRenderWidth() * (posX + width), screenProperties.getRenderHeight() * (posY + height), -1f);

		glTexCoord2f(0f, 1f);
		glVertex3f(screenProperties.getRenderWidth() * (posX), screenProperties.getRenderHeight() * (posY + height), -1f);

		glEnd();
	}

	public static void renderTextureQuad(ScreenProperties screenProperties, float width, float height, float posX, float posY) {
		glBegin(GL_QUADS);

		glTexCoord2f(0f, 1f);
		glVertex3f(screenProperties.getRenderWidth() * (posX), screenProperties.getRenderHeight() * (posY), -1f);

		glTexCoord2f(1f, 1f);
		glVertex3f(screenProperties.getRenderWidth() * (posX + width), screenProperties.getRenderHeight() * (posY), -1f);

		glTexCoord2f(1f, 0f);
		glVertex3f(screenProperties.getRenderWidth() * (posX + width), screenProperties.getRenderHeight() * (posY + height), -1f);

		glTexCoord2f(0f, 0f);
		glVertex3f(screenProperties.getRenderWidth() * (posX), screenProperties.getRenderHeight() * (posY + height), -1f);

		glEnd();
	}

	public static void renderFBOQuadAbsolute(float width, float height, float posX, float posY) {
		glBegin(GL_QUADS);

		glTexCoord2f(0f, 0f);
		glVertex3f((posX), (posY), -1f);

		glTexCoord2f(1f, 0f);
		glVertex3f((posX + width), (posY), -1f);

		glTexCoord2f(1f, 1f);
		glVertex3f((posX + width), (posY + height), -1f);

		glTexCoord2f(0f, 1f);
		glVertex3f((posX), (posY + height), -1f);

		glEnd();
	}
	
	public static void renderTextureQuadAbsolute(float width, float height, float posX, float posY) {
		glBegin(GL_QUADS);

		glTexCoord2f(0f, 1f);
		glVertex3f((posX), (posY), -1f);

		glTexCoord2f(1f, 1f);
		glVertex3f((posX + width), (posY), -1f);

		glTexCoord2f(1f, 0f);
		glVertex3f((posX + width), (posY + height), -1f);

		glTexCoord2f(0f, 0f);
		glVertex3f((posX), (posY + height), -1f);

		glEnd();
	}

}
