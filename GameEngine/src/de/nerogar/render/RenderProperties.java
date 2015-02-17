package de.nerogar.render;

import de.nerogar.util.Matrix4f;

public interface RenderProperties {

	public void transformGL();

	public Matrix4f getModelMatrix();

}
