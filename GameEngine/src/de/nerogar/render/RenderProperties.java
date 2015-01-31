package de.nerogar.render;

import de.nerogar.util.Vectorf;

public class RenderProperties {

	public static final RenderProperties defaultInstance;

	public Vectorf<?> position = null;
	public Vectorf<?> rotation = null;
	public Vectorf<?> scale = null;

	public RenderProperties() {
	}

	public RenderProperties(Vectorf<?> position, Vectorf<?> rotation, Vectorf<?> scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	static {
		defaultInstance = new RenderProperties();
	}
}
