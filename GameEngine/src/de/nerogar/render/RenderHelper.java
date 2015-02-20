package de.nerogar.render;

import static org.lwjgl.opengl.GL11.*;

public class RenderHelper {

	public static void renderFullscreenQuad(ScreenProperties screenProperties) {
		glBegin(GL_QUADS);

		glTexCoord2f(1f, 1f);
		glVertex3f(screenProperties.getRenderWidth(), screenProperties.getRenderHeight(), -1f);

		glTexCoord2f(0f, 1f);
		glVertex3f(0f, screenProperties.getRenderHeight(), -1f);

		glTexCoord2f(0f, 0f);
		glVertex3f(0f, 0f, -1f);

		glTexCoord2f(1f, 0f);
		glVertex3f(screenProperties.getRenderWidth(), 0f, -1f);

		glEnd();
	}

}
