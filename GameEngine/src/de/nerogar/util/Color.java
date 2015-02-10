package de.nerogar.util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Color {

	float[] colors;

	//private FloatBuffer colorBuffer;

	public Color() {
		this(0.0f, 0.0f, 0.0f, 1.0f);
	}

	public Color(int red, int green, int blue, int alpha) {
		this(red / 255f, blue / 255f, green / 255f, alpha / 255f);
	}

	public Color(int rgba) {
		this((rgba >>> 24) & 0xff, (rgba >>> 16) & 0xff, (rgba >>> 8) & 0xff, (rgba) & 0xff);
	}

	public Color(float red, float green, float blue, float alpha) {
		colors = new float[] { red, green, blue, alpha };
	}

	public float getR() {
		return colors[0];
	}

	public float getG() {
		return colors[1];
	}

	public float getB() {
		return colors[2];
	}

	public float getA() {
		return colors[3];
	}

	/*public FloatBuffer getFloatBuffer() {
		if (colorBuffer == null) {
			colorBuffer = BufferUtils.createFloatBuffer(4);
			colorBuffer.put(colors);
			colorBuffer.flip();
		}

		return colorBuffer;
	}*/
}