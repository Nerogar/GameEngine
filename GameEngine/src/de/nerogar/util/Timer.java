package de.nerogar.util;

public class Timer {

	private long lastFrameTime;
	private float timeDelta;

	private int frameCount;
	private long lastFramePrint;

	private long startTime;
	private double runTime;

	public Timer() {
		startTime = System.nanoTime();
		lastFrameTime = startTime;
		lastFramePrint = startTime;
		runTime = 0D;
		timeDelta = 1F;
	}

	public void update() {
		long currentTime = System.nanoTime();
		timeDelta = (float) ((double) (currentTime - lastFrameTime) * 0.000000001D);

		//broke capping
		/*if (timeDelta > lastTimeDelta * 2) {
			System.out.println("time capped: " + timeDelta + " | " + lastTimeDelta*2);
			timeDelta = lastTimeDelta * 2;
		} else {
			System.out.println("not capped: " + timeDelta);
		}*/

		if (currentTime - lastFramePrint > 1000000000L) {
			lastFramePrint += 1000000000L;
			System.out.println("fps: " + frameCount + "; time: " + (1f / frameCount * 1000f) + "ms");
			frameCount = 0;
		}
		frameCount++;

		lastFrameTime = currentTime;
		runTime += (double) timeDelta;

	}

	public float getTimeDelta() {
		return timeDelta;
	}

	public double getRunTime() {
		return runTime;
	}

}
