package de.nerogar.util;

public class Matrix4fUtils {

	//@formatter:off
	public static void setUnitMatrix(Matrix4f m) {
		m.set(new float[] {
				1.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 1.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 1.0f
		});
	}

	public static void setPositionMatrix(Matrix4f m, float x, float y, float z) {
		m.set(new float[] {
				1.0f, 0.0f, 0.0f, x,
				0.0f, 1.0f, 0.0f, y,
				0.0f, 0.0f, 1.0f, z,
				0.0f, 0.0f, 0.0f, 1.0f
		});
	}

	public static void setYawMatrix(Matrix4f m, float radiants) {
		m.set(new float[] {
				(float) Math.cos(-radiants),	0.0f,					(float) Math.sin(-radiants),	0.0f,
				0.0f,						1.0f,					0.0f,						0.0f,
				(float)						-Math.sin(-radiants),	0.0f,						(float) Math.cos(-radiants), 0.0f,
				0.0f,						0.0f,					0.0f,						1.0f
		});
	}

	public static void setPitchMatrix(Matrix4f m, float radiants) {
		m.set(new float[] {
				1.0f,	0.0f,						0.0f,							0.0f,
				0.0f,	(float) Math.cos(-radiants),	(float) -Math.sin(-radiants),	0.0f,
				0.0f,	(float) Math.sin(-radiants),	(float) Math.cos(-radiants),		0.0f,
				0.0f,	0.0f,						0.0f,							1.0f
		});
	}

	public static void setRollMatrix(Matrix4f m, float radiants) {
		m.set(new float[] {
				(float) Math.cos(-radiants),	(float) -Math.sin(-radiants),	0.0f, 0.0f,
				(float) Math.sin(-radiants),	(float) Math.cos(-radiants),		0.0f, 0.0f,
				0.0f,						0.0f,							1.0f, 0.0f,
				0.0f,						0.0f,							0.0f, 1.0f
		});
	}
	//@formatter:on

}
