package de.nerogar.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

public class GameDisplay {
	private DisplayMode[] availableDisplayModes;
	private int currentDisplayMode;

	private boolean depthTestEnabled;

	public GameDisplay() throws LWJGLException {
		initDisplay();

		setDefaultDisplayMode();

		Display.create();

		initGL();
	}

	private void initDisplay() throws LWJGLException {

		DisplayMode[] allDisplayModes = Display.getAvailableDisplayModes();
		ArrayList<DisplayMode> allDisplayModesSorted = new ArrayList<DisplayMode>();

		for (DisplayMode dm : allDisplayModes) {
			if (dm.getFrequency() == Display.getDesktopDisplayMode().getFrequency() && dm.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) {
				allDisplayModesSorted.add(dm);
			}
		}

		availableDisplayModes = allDisplayModesSorted.toArray(new DisplayMode[allDisplayModesSorted.size()]);

		Arrays.sort(availableDisplayModes, (a, b) -> {
			if (a.getWidth() != b.getWidth()) {
				return a.getWidth() - b.getWidth();
			} else {
				return a.getHeight() - b.getHeight();
			}
		});

	}

	private void initGL() {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		depthTestEnabled = true;
		glClearColor(0f, 0f, 0f, 1f);
		setScreenProperties(ScreenProperties.defaultInstance, true);
	}

	public void setScreenProperties(ScreenProperties screenProperties, boolean clearScreen) {
		if (screenProperties.orthographic) {
			setOrtho(screenProperties);
		} else {
			setPerspective(screenProperties);
		}

		glViewport(0, 0, screenProperties.getRenderWidth(), screenProperties.getRenderHeight());

		glLoadIdentity();

		if (screenProperties.renderTarget != null) {
			screenProperties.renderTarget.bind();
		} else {
			RenderTarget.bindDefault();
			if (screenProperties.getRenderWidth() != getDisplayMode().getWidth() && screenProperties.getRenderHeight() != getDisplayMode().getHeight()) {
				setDisplayMode(screenProperties.getRenderWidth(), screenProperties.getRenderHeight());
			}
		}

		if (screenProperties.camera != null) screenProperties.camera.transformGL();

		if (clearScreen) clearScreen();

		if (screenProperties.enableDepthTest != depthTestEnabled) {
			if (screenProperties.enableDepthTest) glDisable(GL_DEPTH_TEST);
			else glEnable(GL_DEPTH_TEST);
			depthTestEnabled = screenProperties.enableDepthTest;
		}
	}

	private void setOrtho(ScreenProperties renderProperties) {
		glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
		glLoadIdentity(); // Reset The Projection Matrix
		glOrtho(0.0, renderProperties.getRenderWidth(), 0.0, renderProperties.getRenderHeight(), 1.0, -1.0);
		glMatrixMode(GL_MODELVIEW);
	}

	private void setPerspective(ScreenProperties renderProperties) {
		glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
		glLoadIdentity(); // Reset The Projection Matrix
		GLU.gluPerspective(renderProperties.fov, (float) renderProperties.getRenderWidth() / (float) renderProperties.getRenderHeight(), 0.01f, 10000.0f);
		glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix
	}

	private void clearScreen() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public boolean setDisplayMode(int width, int height) {

		System.out.println(width + " : " + height);

		for (int i = 0; i < availableDisplayModes.length; i++) {
			DisplayMode dm = availableDisplayModes[i];

			if (dm.getWidth() == width && dm.getHeight() == height) {
				try {
					Display.setDisplayMode(dm);

					currentDisplayMode = i;
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
				return true;
			}
		}

		return false;
	}

	public DisplayMode getDisplayMode() {
		return availableDisplayModes[currentDisplayMode];
	}

	private void setDefaultDisplayMode() throws LWJGLException {
		Display.setDisplayMode(availableDisplayModes[0]);
	}

	public void update() {
		Display.update();
		clearScreen();
		glLoadIdentity();
	}

	public void cleanup() {
		Display.destroy();
	}

}
