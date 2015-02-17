package de.nerogar.render;

import java.util.Locale;

import org.lwjgl.opengl.GL13;

import de.nerogar.util.*;

public class Camera extends BaseCamera {

	private float yaw, pitch, roll;
	private float x, y, z;
	private Ray<Vector3f> camRay;

	private Matrix4f positionMatrix;
	private Matrix4f yawMatrix;
	private Matrix4f pitchMatrix;
	private Matrix4f rollMatrix;

	private boolean finalMatrixDirty = true;
	private Matrix4f finalMatrix;

	public Camera() {
		positionMatrix = new Matrix4f();
		yawMatrix = new Matrix4f();
		pitchMatrix = new Matrix4f();
		rollMatrix = new Matrix4f();
		finalMatrix = new Matrix4f();

		setPositionMatrix();
		setYawMatrix();
		setPitchMatrix();
		setRollMatrix();
	}

	@Override
	protected void transformGL() {
		if (finalMatrixDirty) setFinalMatrix();

		/*GL11.glRotatef(roll, 0f, 0f, 1f);
		GL11.glRotatef(pitch, 1f, 0f, 0f);
		GL11.glRotatef(yaw, 0f, 1f, 0f);

		GL11.glTranslatef(-x, -y, -z);*/

		GL13.glMultTransposeMatrix(finalMatrix.asBuffer());

	}

	private void setPositionMatrix() {
		Matrix4fUtils.setPositionMatrix(positionMatrix, -x, -y, -z);
		finalMatrixDirty = true;
	}

	private void setYawMatrix() {
		Matrix4fUtils.setYawMatrix(yawMatrix, -yaw);
		finalMatrixDirty = true;
	}

	private void setPitchMatrix() {
		Matrix4fUtils.setPitchMatrix(pitchMatrix, -pitch);
		finalMatrixDirty = true;
	}

	private void setRollMatrix() {
		Matrix4fUtils.setRollMatrix(rollMatrix, -roll);
		finalMatrixDirty = true;
	}

	private void setFinalMatrix() {
		finalMatrix.set(positionMatrix);
		finalMatrix.multiplyRight(yawMatrix);
		finalMatrix.multiplyRight(pitchMatrix);
		finalMatrix.multiplyRight(rollMatrix);

		finalMatrixDirty = false;
	}

	private void recalcCameraRay() {
		float dirX = (float) (Math.cos((pitch)) * Math.sin(yaw));
		float dirY = (float) (-Math.sin((pitch)));
		float dirZ = (float) (-Math.cos((pitch)) * Math.cos(yaw));

		camRay = new Ray<Vector3f>(new Vector3f(x, y, z), new Vector3f(dirX, dirY, dirZ));
	}

	public Ray<Vector3f> getCameraRay() {
		if (camRay == null) recalcCameraRay();
		return camRay;
	}

	public Matrix4f getViewMatrix() {
		return finalMatrix;
	}

	/**
	 * Sets the camera yaw in radiants 
	 */
	public void setYaw(float yaw) {
		this.yaw = yaw;
		camRay = null;
		setYawMatrix();
	}

	/**
	 * @return The camera yaw in radiants 
	 */
	public float getYaw() {
		return yaw;
	}

	/**
	 * Sets the camera pitch in radiants 
	 */
	public void setPitch(float pitch) {
		this.pitch = pitch;
		camRay = null;
		setPitchMatrix();
	}

	/**
	 * @return The camera pitch in radiants 
	 */
	public float getPitch() {
		return pitch;
	}

	/**
	 * Sets the camera roll in radiants 
	 */
	public void setRoll(float roll) {
		this.roll = roll;
		camRay = null;
		setRollMatrix();
	}

	/**
	 * @return The camera roll in radiants 
	 */
	public float getRoll() {
		return roll;
	}

	public void setX(float x) {
		this.x = x;
		camRay = null;
		setPositionMatrix();
	}

	public float getX() {
		return x;
	}

	public void setY(float y) {
		this.y = y;
		camRay = null;
		setPositionMatrix();
	}

	public float getY() {
		return y;
	}

	public void setZ(float z) {
		this.z = z;
		camRay = null;
		setPositionMatrix();
	}

	public float getZ() {
		return z;
	}

	@Override
	public String toString() {
		return String.format(Locale.US, "Camera(yaw:%.2f, pitch:%.2f, roll:%.2f, x:%.2f, y:%.2f, z:%.2f)", yaw, pitch, roll, x, y, z);
	}

}
