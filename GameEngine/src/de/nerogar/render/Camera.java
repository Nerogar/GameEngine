package de.nerogar.render;

import java.util.Locale;

import org.lwjgl.opengl.GL11;

public class Camera extends BaseCamera {

	public float yaw, pitch, roll;
	public float x, y, z;

	@Override
	protected void transformGL() {

		GL11.glRotatef(roll, 0f, 0f, 1f);
		GL11.glRotatef(pitch, 1f, 0f, 0f);
		GL11.glRotatef(yaw, 0f, 1f, 0f);

		GL11.glTranslatef(-x, -y, -z);

		//FIXME test only, no final result!!!!		
		/*
		//roll
		GL13.glMultTransposeMatrix(toFloatBuffer(new float[] {
				(float) Math.cos(roll),	(float) -Math.sin(roll),	0.0f,	0.0f,
				(float) Math.sin(roll),	(float) Math.cos(roll),	0.0f,	0.0f,
				0.0f,							0.0f,							1.0f,	0.0f,
				0.0f,							0.0f,							0.0f,	1.0f
		}));

		//pitch
		GL13.glMultTransposeMatrix(toFloatBuffer(new float[] {
				1.0f,	0.0f,							0.0f,							0.0f,
				0.0f,	(float) Math.cos(pitch),	(float) -Math.sin(pitch),	0.0f,
				0.0f,	(float) Math.sin(pitch),	(float) Math.cos(pitch),	0.0f,
				0.0f,	0.0f,							0.0f,							1.0f
		}));

		//yaw
		GL13.glMultTransposeMatrix(toFloatBuffer(new float[] {
				(float) Math.cos(yaw),	0.0f,	(float) Math.sin(yaw),	0.0f,
				0.0f,							1.0f,	0.0f,							0.0f,
				(float) -Math.sin(yaw),	0.0f,	(float) Math.cos(yaw),	0.0f,
				0.0f,							0.0f,	0.0f,							1.0f
		}));

		//translation
		GL13.glMultTransposeMatrix(toFloatBuffer(new float[] {
				1.0f, 0.0f, 0.0f, -x,
				0.0f, 1.0f, 0.0f, -y,
				0.0f, 0.0f, 1.0f, -z,
				0.0f, 0.0f, 0.0f, 1.0f
		}));*/

	}

	/*private FloatBuffer toFloatBuffer(float[] array) {
		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(array.length);
		floatBuffer.put(array);
		floatBuffer.flip();
		return floatBuffer;
	}*/

	@Override
	public String toString() {
		return String.format(Locale.US, "Camera(yaw:%.2f, pitch:%.2f, roll:%.2f, x:%.2f, y:%.2f, z:%.2f)", yaw, pitch, roll, x, y, z);
	}

}
