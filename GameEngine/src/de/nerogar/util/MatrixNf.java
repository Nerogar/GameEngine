package de.nerogar.util;

import java.util.Arrays;

public class MatrixNf implements Matrixf<MatrixNf> {

	private float[] components;
	private int componentCount;

	public MatrixNf(int componentCount) {
		this.componentCount = componentCount;
		components = new float[componentCount * componentCount];
	}

	private MatrixNf(float[] components, int componentCount) {
		this.components = components;
		this.componentCount = componentCount;
	}

	@Override
	public int getComponentCount() {
		return componentCount;
	}

	@Override
	public MatrixNf newInstance() {
		return new MatrixNf(componentCount);
	}

	@Override
	public float get(int componentLine, int componentCollumn) {
		return components[componentLine * componentCount + componentCollumn];
	}

	@Override
	public MatrixNf set(int componentLine, int componentCollumn, float f) {
		components[componentLine * componentCount + componentCollumn] = f;
		return this;
	}

	@Override
	public MatrixNf set(float allComponents) {
		for (int i = 0; i < components.length; i++) {
			components[i] = allComponents;
		}

		return this;
	}

	@Override
	public MatrixNf set(Matrixf<?> m) {
		for (int i = 0; i < componentCount; i++) {
			for (int j = 0; j < componentCount; j++) {
				set(i, j, m.get(i, j));
			}
		}

		return this;
	}

	@Override
	public MatrixNf set(float[] m) {
		for (int i = 0; i < components.length; i++) {
			components[i] = m[i];
		}

		return this;
	}

	@Override
	public MatrixNf add(Matrixf<?> m) {
		for (int i = 0; i < componentCount; i++) {
			for (int j = 0; j < componentCount; j++) {
				set(i, j, get(i, j) + m.get(i, j));
			}
		}

		return this;
	}

	@Override
	public MatrixNf added(Matrixf<?> m) {
		return clone().add(m);
	}

	@Override
	public MatrixNf subtract(Matrixf<?> m) {
		for (int i = 0; i < componentCount; i++) {
			for (int j = 0; j < componentCount; j++) {
				set(i, j, get(i, j) - m.get(i, j));
			}
		}

		return this;
	}

	@Override
	public MatrixNf subtracted(Matrixf<?> m) {
		return clone().subtract(m);
	}

	@Override
	public MatrixNf multiply(float f) {
		for (int i = 0; i < components.length; i++) {
			components[i] *= f;
		}

		return this;
	}

	@Override
	public MatrixNf multiplied(float f) {
		float[] newMatrix = new float[componentCount * componentCount];
		for (int i = 0; i < components.length; i++) {
			newMatrix[i] = components[i] * f;
		}

		return new MatrixNf(newMatrix, componentCount);
	}

	@Override
	public MatrixNf multiplyRight(Matrixf<?> m) {
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
	public MatrixNf multipliedRight(Matrixf<?> m) {
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

		return new MatrixNf(newMatrix, componentCount);
	}

	@Override
	public MatrixNf multiplyLeft(Matrixf<?> m) {
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
	public MatrixNf multipliedLeft(Matrixf<?> m) {
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

		return new MatrixNf(newMatrix, componentCount);
	}

	@Override
	public MatrixNf clone() {
		return new MatrixNf(Arrays.copyOf(components, components.length), componentCount);
	}

}
