package de.nerogar.util;

import java.util.Arrays;

public class Matrix4f implements Matrixf<Matrix4f> {

	private float[] components;
	private int componentCount;

	public Matrix4f() {
		componentCount = 4;
		components = new float[componentCount * componentCount];
	}

	private Matrix4f(float[] components) {
		this.components = components;
		this.componentCount = 4;
	}

	@Override
	public int getComponentCount() {
		return componentCount;
	}

	@Override
	public Matrix4f newInstance() {
		return new Matrix4f();
	}

	@Override
	public float get(int componentLine, int componentCollumn) {
		return components[componentLine * componentCount + componentCollumn];
	}

	@Override
	public Matrix4f set(int componentLine, int componentCollumn, float f) {
		components[componentLine * componentCount + componentCollumn] = f;
		return this;
	}

	@Override
	public Matrix4f set(float allComponents) {
		for (int i = 0; i < components.length; i++) {
			components[i] = allComponents;
		}

		return this;
	}

	@Override
	public Matrix4f set(Matrixf<?> m) {
		for (int i = 0; i < componentCount; i++) {
			for (int j = 0; j < componentCount; j++) {
				set(i, j, m.get(i, j));
			}
		}

		return this;
	}

	@Override
	public Matrix4f add(Matrixf<?> m) {
		for (int i = 0; i < componentCount; i++) {
			for (int j = 0; j < componentCount; j++) {
				set(i, j, get(i, j) + m.get(i, j));
			}
		}

		return this;
	}

	@Override
	public Matrix4f added(Matrixf<?> m) {
		return clone().add(m);
	}

	@Override
	public Matrix4f subtract(Matrixf<?> m) {
		for (int i = 0; i < componentCount; i++) {
			for (int j = 0; j < componentCount; j++) {
				set(i, j, get(i, j) - m.get(i, j));
			}
		}

		return this;
	}

	@Override
	public Matrix4f subtracted(Matrixf<?> m) {
		return clone().subtract(m);
	}

	@Override
	public Matrix4f multiply(float f) {
		for (int i = 0; i < components.length; i++) {
			components[i] *= f;
		}

		return this;
	}

	@Override
	public Matrix4f multiplied(float f) {
		float[] newMatrix = new float[componentCount * componentCount];
		for (int i = 0; i < components.length; i++) {
			newMatrix[i] = components[i] * f;
		}

		return new Matrix4f(newMatrix);
	}

	@Override
	public Matrix4f multiplyRight(Matrixf<?> m) {
		float[] newMatrix = new float[componentCount * componentCount];

		for (int i = 0; i < componentCount; i++) {
			for (int j = 0; j < componentCount; j++) {
				float sum = 0;

				for (int w = 0; w < componentCount; w++) {
					sum += get(i, w) * m.get(w, j);
				}

				newMatrix[j * componentCount + i] = sum;
			}
		}

		components = newMatrix;

		return this;
	}

	@Override
	public Matrix4f multipliedRight(Matrixf<?> m) {
		float[] newMatrix = new float[componentCount * componentCount];

		for (int i = 0; i < componentCount; i++) {
			for (int j = 0; j < componentCount; j++) {
				float sum = 0;

				for (int w = 0; w < componentCount; w++) {
					sum += get(i, w) * m.get(w, j);
				}

				newMatrix[j * componentCount + i] = sum;
			}
		}

		return new Matrix4f(newMatrix);
	}

	@Override
	public Matrix4f multiplyLeft(Matrixf<?> m) {
		float[] newMatrix = new float[componentCount * componentCount];

		for (int i = 0; i < componentCount; i++) {
			for (int j = 0; j < componentCount; j++) {
				float sum = 0;

				for (int w = 0; w < componentCount; w++) {
					sum += m.get(i, w) * get(w, j);
				}

				newMatrix[j * componentCount + i] = sum;
			}
		}

		components = newMatrix;

		return this;
	}

	@Override
	public Matrix4f multipliedLeft(Matrixf<?> m) {
		float[] newMatrix = new float[componentCount * componentCount];

		for (int i = 0; i < componentCount; i++) {
			for (int j = 0; j < componentCount; j++) {
				float sum = 0;

				for (int w = 0; w < componentCount; w++) {
					sum += m.get(i, w) * get(w, j);
				}

				newMatrix[j * componentCount + i] = sum;
			}
		}

		return new Matrix4f(newMatrix);
	}

	@Override
	public Matrix4f clone() {
		return new Matrix4f(Arrays.copyOf(components, components.length));
	}

}
