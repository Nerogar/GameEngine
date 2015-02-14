package de.nerogar.render;

import java.util.Locale;

import org.lwjgl.opengl.GL11;

import de.nerogar.util.Ray;
import de.nerogar.util.Vector3f;

public class Camera extends BaseCamera {

	private float yaw, pitch, roll;
	private float x, y, z;
	private Ray camRay;

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

	private void recalcCameraRay() {
		double conv = Math.PI / 180;

		float dirX = (float) (Math.cos((pitch) * conv) * Math.sin(yaw * conv));
		float dirY = (float) (-Math.sin((pitch) * conv));
		float dirZ = (float) (-Math.cos((pitch) * conv) * Math.cos(yaw * conv));

		camRay = new Ray(new Vector3f(x, y, z), new Vector3f(dirX, dirY, dirZ));
	}

	public Ray getCameraRay() {
		if(camRay == null) recalcCameraRay();
		return camRay;
	}

	public void setYaw(float yaw) {this.yaw = yaw; camRay = null;}
	public float getYaw() {return yaw;}

	public void setPitch(float pitch) {this.pitch = pitch; camRay = null;}
	public float getPitch() {return pitch;}

	public void setRoll(float roll) {this.roll = roll; camRay = null;}
	public float getRoll() {return roll;}

	public void setX(float x) {this.x = x; camRay = null;}
	public float getX() {return x;}

	public void setY(float y) {this.y = y; camRay = null;}
	public float getY() {return y;}

	public void setZ(float z) {this.z = z; camRay = null;}
	public float getZ() {return z;}

	@Override
	public String toString() {
		return String.format(Locale.US, "Camera(yaw:%.2f, pitch:%.2f, roll:%.2f, x:%.2f, y:%.2f, z:%.2f)", yaw, pitch, roll, x, y, z);
	}

}
