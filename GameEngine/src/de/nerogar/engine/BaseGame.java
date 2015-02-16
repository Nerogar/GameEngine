package de.nerogar.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import de.nerogar.render.GameDisplay;
import de.nerogar.util.Timer;

public abstract class BaseGame {

	private boolean running;
	private Timer timer;
	protected GameDisplay display;
	private int targetFPS;

	public BaseGame() {
		timer = new Timer();

		try {
			display = new GameDisplay();
		} catch (LWJGLException e) {
			System.err.println("Could not create Display");
			e.printStackTrace();
		}
	}

	public void start() {
		running = true;
		startup();
		run();
		cleanup();
		display.cleanup();
	}

	protected void run() {
		while (running) {

			timer.update();
			update(timer.getTimeDelta());
			render();
			display.update();
			if (targetFPS > 0) Display.sync(targetFPS);
			if (Display.isCloseRequested()) running = false;
		}
	}

	public abstract void startup();

	protected abstract void update(float timeDelta);

	protected abstract void render();

	protected abstract void cleanup();

	public void stop(){
		running = false;
	}
	
	public void setTargetFPS(int targetFPS) {
		this.targetFPS = targetFPS;
	}

}
